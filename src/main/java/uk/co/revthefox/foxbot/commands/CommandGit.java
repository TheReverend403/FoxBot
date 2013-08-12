package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;


public class CommandGit extends Command
{
    private FoxBot foxbot;

    public CommandGit(FoxBot foxbot)
    {
        super("git", "command.git");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        channel.sendMessage(String.format("(%s) %sI'm on GitHub! %shttps://github.com/TheReverend403/FoxBot", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL));
    }
}
