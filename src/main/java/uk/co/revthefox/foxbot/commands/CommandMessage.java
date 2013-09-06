package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

public class CommandMessage extends Command
{
    private FoxBot foxbot;

    public CommandMessage(FoxBot foxbot)
    {
        super("pm", "command.message", "message");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        PircBotX bot = foxbot.getBot();

        if (args.length > 1)
        {
            User target = bot.getUser(args[0]);
            StringBuilder message = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                message.append(" ").append(args[arg]);
            }

            bot.sendMessage(target, Utils.colourise(message.toString()));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %spm <user> <message>", foxbot.getConfig().getCommandPrefix()));
    }
}