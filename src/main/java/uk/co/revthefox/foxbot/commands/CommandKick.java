package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKick extends Command
{
    private FoxBot foxbot;

    public CommandKick(FoxBot foxbot)
    {
        super("kick", "command.kick");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        final Channel channel = event.getChannel();
        final PircBotX bot = foxbot.getBot();
        final User target = args.length > 0 ? bot.getUser(args[0]) : null;


        if (args.length != 0)
        {
            if (!channel.getUsers().contains(target))
            {
                bot.sendNotice(sender, "That user is not in this channel");
                return;
            }

            if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.kick") || args[0].equals(foxbot.getBot().getNick()))
            {
                bot.sendNotice(sender, "You cannot kick that user!");
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

                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException ex)
                        {
                            ex.printStackTrace();
                        }
                        bot.kick(channel, target, reason.toString());
                    }
                }).start();
                return;
            }

            new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                    bot.kick(channel, target);
                }
            }).start();
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %skick <nick> [reason]", foxbot.getConfig().getCommandPrefix()));
    }
}
