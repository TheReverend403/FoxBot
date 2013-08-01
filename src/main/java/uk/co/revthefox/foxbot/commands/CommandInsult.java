package uk.co.revthefox.foxbot.commands;


import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandInsult  extends Command
{

    public CommandInsult()
    {
        super("insult", "command.insult");
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        channel.sendMessage("Command executed successfully!");
    }
}
