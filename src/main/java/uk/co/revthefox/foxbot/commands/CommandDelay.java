package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandDelay extends Command
{
    private FoxBot foxbot;

    public CommandDelay(FoxBot foxbot)
    {
        super("delay", "command.delay");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        PircBotX bot = foxbot.getBot();

        if (args.length == 1)
        {
            try
            {
                bot.setMessageDelay(Long.valueOf(args[0]));
                bot.sendNotice(sender, String.format("Message delay set to %sms", bot.getMessageDelay()));
            }
            catch (NumberFormatException ex)
            {
                bot.sendNotice(sender, "That is not a number!");
                bot.setMessageDelay(foxbot.getConfig().getMessageDelay());
            }
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %sdelay <number of milliseconds>", foxbot.getConfig().getCommandPrefix()));
    }
}
