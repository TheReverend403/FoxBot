package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

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
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                User sender = event.getUser();
                Channel channel = event.getChannel();
                PircBotX bot = foxbot.getBot();
                User target;

                if (args.length != 0)
                {
                    target = bot.getUser(args[0]);

                    if (!channel.getUsers().contains(target))
                    {
                        bot.sendNotice(sender, "That user is not in this channel!");
                        return;
                    }

                    if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.ban") || args[0].equals(foxbot.getBot().getNick()))
                    {
                        bot.sendNotice(sender, "You cannot ban that user!");
                        return;
                    }

                    if (args.length > 1)
                    {
                        final StringBuilder reason = new StringBuilder(args[1]);

                        for (int arg = 2; arg < args.length; arg++)
                        {
                            reason.append(" ").append(args[arg]);
                        }

                        // Delay the kick to prevent whois throttling due to the permission check on both users
                        try
                        {
                            Thread.sleep(foxbot.getConfig().getKickDelay());
                        }
                        catch (InterruptedException ex)
                        {
                            ex.printStackTrace();
                        }

                        bot.kick(channel, target, String.format("Ban requested by %s - %s", sender.getNick(), reason.toString()));
                        bot.ban(channel, target.getHostmask());
                        return;
                    }

                    // Delay the kick to prevent whois throttling due to the permission check on both users
                    try
                    {
                        Thread.sleep(foxbot.getConfig().getKickDelay());
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }

                    bot.kick(channel, target, String.format("Ban requested by %s", sender.getNick()));
                    bot.ban(channel, target.getHostmask());
                    return;
                }
                bot.sendNotice(sender, String.format("Wrong number of args! Use %sban <nick> [reason]", foxbot.getConfig().getCommandPrefix()));
            }
        }).start();
    }
}
