package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandExec extends Command
{
    private FoxBot foxbot;

    public CommandExec(FoxBot foxbot)
    {
        super("exec", "command.exec");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {

    }
}
