package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandPart extends Command
{
    private FoxBot foxbot;

    public CommandPart(FoxBot foxbot)
    {
        super("part", "command.part", "leave");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length != 0)
        {
            for (String chan : args)
            {
                if (chan.startsWith("#"))
                {
                    foxbot.partChannel(foxbot.getChannel(chan), String.format("Part command used by %s", sender.getNick()));
                    foxbot.sendNotice(sender, String.format("Left %s", chan));
                    continue;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", chan));
            }
            return;
        }
        foxbot.partChannel(channel);
        foxbot.sendNotice(sender, String.format("Left %s", channel.getName()));
    }
}