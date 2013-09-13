package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CommandBan extends Command
{
    private FoxBot foxbot;

    public CommandBan(FoxBot foxbot)
    {
        super("ban", "command.ban");
        this.foxbot = foxbot;
    }

    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 1)
        {
            User target = foxbot.getUser(args[0]);
            String hostmask = "*" + target.getHostmask();

            if (!channel.getUsers().contains(target))
            {
                foxbot.sendNotice(sender, "That user is not in this channel!");
                return;
            }

            // Please don't throttle me ;_;
            try
            {
                Thread.sleep(foxbot.getConfig().getKickDelay());
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.ban") || args[0].equals(foxbot.getNick()))
            {
                foxbot.sendNotice(sender, "You cannot ban that user!");
                return;
            }

            StringBuilder reason = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                reason.append(" ").append(args[arg]);
            }

            foxbot.kick(channel, target, String.format("Ban requested by %s - %s", sender.getNick(), foxbot.getUtils().colourise(reason.toString()) + Colors.NORMAL));

            long banTime = System.currentTimeMillis();

            foxbot.ban(channel, hostmask);
            foxbot.getDatabase().addBan(channel, target, reason.toString(), sender, banTime);

            if (foxbot.getConfig().getUnbanTimer() != 0)
            {
                foxbot.getUtils().scheduleUnban(channel, hostmask, foxbot.getConfig().getUnbanTimer());
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sban <user> <reason>", foxbot.getConfig().getCommandPrefix()));
    }
}