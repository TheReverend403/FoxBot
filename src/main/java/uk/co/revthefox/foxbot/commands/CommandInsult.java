package uk.co.revthefox.foxbot.commands;


import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandInsult extends Command
{
    private FoxBot foxbot;

    public CommandInsult(FoxBot foxbot)
    {
        super("insult", "command.insult");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        channel.sendMessage("Command executed successfully!");
    }
}
