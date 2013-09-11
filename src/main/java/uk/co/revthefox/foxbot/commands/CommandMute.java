package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.Calendar;

public class CommandMute extends Command
{
    private FoxBot foxbot;

    public CommandMute(FoxBot foxbot)
    {
        super("mute", "command.mute", "quiet");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 1)
        {
            User target = foxbot.getUser(args[0]);

            if (!channel.getUsers().contains(target))
            {
                foxbot.sendNotice(sender, "That user is not in this channel!");
                return;
            }

            // Delay the mute to prevent whois throttling due to the permission check on both users
            try
            {
                Thread.sleep(foxbot.getConfig().getKickDelay());
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.mute") || args[0].equals(foxbot.getNick()))
            {
                foxbot.sendNotice(sender, "You cannot mute that user!");
                return;
            }

            String hostmask = "*" + target.getHostmask();

            StringBuilder reason = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                reason.append(" ").append(args[arg]);
            }

            long muteTime = Calendar.getInstance().getTimeInMillis();

            foxbot.setMode(channel, "+q ", hostmask);
            foxbot.getDatabase().addMute(channel, target, reason.toString(), sender, muteTime);

            if (foxbot.getConfig().getUnbanTimer() != 0)
            {
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", foxbot.getConfig().getUnbanTimer());
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smute <user> <reason>", foxbot.getConfig().getCommandPrefix()));
    }
}