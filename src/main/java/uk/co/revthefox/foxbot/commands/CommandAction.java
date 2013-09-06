package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

public class CommandAction extends Command
{
    private FoxBot foxbot;

    public CommandAction(FoxBot foxbot)
    {
        super("action", "command.action");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();

        if (args.length > 0 && !(args[0].startsWith("#") && args.length == 1))
        {
            StringBuilder message;

            if (args[0].startsWith("#"))
            {
                message = new StringBuilder(args[1]);

                for (int arg = 2; arg < args.length; arg++)
                {
                    if (!args[arg].equalsIgnoreCase("-s"))
                    {
                        message.append(" ").append(args[arg]);
                    }
                }

                if (bot.getChannel(args[0]).isInviteOnly())
                {
                    bot.sendNotice(sender, String.format("%s is invite only!", args[0]));
                    return;
                }

                bot.joinChannel(args[0]);

                if (!args[args.length - 1].equalsIgnoreCase("-s"))
                {
                    bot.sendAction(args[0], Utils.colourise(message.toString()));
                    bot.partChannel(bot.getChannel(args[0]));
                    bot.sendNotice(sender, String.format("Action sent to %s, and channel has been left", args[0]));
                    return;
                }
                bot.sendAction(args[0], Utils.colourise(message.toString()));
                bot.sendNotice(sender, String.format("Action sent to %s", args[0]));
                return;
            }

            message = new StringBuilder(args[0]);

            for (int arg = 1; arg < args.length; arg++)
            {
                if (!args[arg].equalsIgnoreCase("-s"))
                {
                    message.append(" ").append(args[arg]);
                }
            }
            bot.sendAction(channel, Utils.colourise(message.toString()));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %saction [#channel] <action> [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}
