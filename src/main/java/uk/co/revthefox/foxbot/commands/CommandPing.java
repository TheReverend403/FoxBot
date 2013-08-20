package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandPing extends Command
{
    private FoxBot foxbot;

    public CommandPing(FoxBot foxbot)
    {
        super("ping", "command.ping");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(MessageEvent event, String[] args)
    {
        Channel channel = event.getChannel();
        channel.sendMessage("Pong!");
    }
}
