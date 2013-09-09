package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
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
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aI'm on GitHub! &rhttps://github.com/TheReverend403/FoxBot", foxbot.getUtils().munge(sender.getNick()))));
    }
}