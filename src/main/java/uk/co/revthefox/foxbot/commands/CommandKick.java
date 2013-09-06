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
        final MessageEvent threadEvent = event;
        final String[] threadArgs = args;

        new Thread(new Runnable()
        {
            public void run()
            {
                User sender = threadEvent.getUser();
                Channel channel = threadEvent.getChannel();
                PircBotX bot = foxbot.getBot();
                User target;

                if (threadArgs.length != 0)
                {
                    target = bot.getUser(threadArgs[0]);

                    if (!channel.getUsers().contains(target))
                    {
                        bot.sendNotice(sender, "That user is not in this channel!");
                        return;
                    }

                    if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.kick") || threadArgs[0].equals(foxbot.getBot().getNick()))
                    {
                        bot.sendNotice(sender, "You cannot kick that user!");
                        return;
                    }

                    if (threadArgs.length > 1)
                    {
                        final StringBuilder reason = new StringBuilder(threadArgs[1]);

                        for (int arg = 2; arg < threadArgs.length; arg++)
                        {
                            reason.append(" ").append(threadArgs[arg]);
                        }

                        // Delay the kick to prevent whois throttling due to the permission check on both users
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException ex)
                        {
                            ex.printStackTrace();
                        }

                        bot.kick(channel, target, reason.toString());
                        return;
                    }

                    // Delay the kick to prevent whois throttling due to the permission check on both users
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }

                    bot.kick(channel, target);
                    return;
                }
                bot.sendNotice(sender, String.format("Wrong number of args! Use %skick <nick> [reason]", foxbot.getConfig().getCommandPrefix()));
            }
        }).start();
    }
}
