package uk.co.revthefox.foxbot.commands;


import org.pircbotx.Channel;
import org.pircbotx.User;
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
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length != 1)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! Use %sdelay <number of milliseconds>", foxbot.getConfig().getCommandPrefix()));
            return;
        }
        try
        {
            foxbot.getBot().setMessageDelay(Long.valueOf(args[0]));
            foxbot.getBot().sendNotice(sender, String.format("Message delay set to %sms",
                    foxbot.getBot().getMessageDelay()));
        }
        catch (NumberFormatException ex)
        {
            foxbot.getBot().sendNotice(sender, "That is not a number!");
            foxbot.getBot().setMessageDelay(foxbot.getConfig().getMessageDelay());
        }
    }
}
