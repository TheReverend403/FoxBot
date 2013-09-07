package uk.co.revthefox.foxbot.commands;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandJoin extends Command
{
    private FoxBot foxbot;

    public CommandJoin(FoxBot foxbot)
    {
        super("join", "command.join");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length != 0)
        {
            for (String chan : args)
            {
                if (chan.startsWith("#"))
                {
                    if (!foxbot.getChannel(chan).isInviteOnly())
                    {
                        foxbot.joinChannel(chan);
                        foxbot.sendNotice(sender, String.format("Joined %s", chan));
                        continue;
                    }
                    foxbot.sendNotice(sender, String.format("%s is invite only!", chan));
                    continue;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", chan));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ... ]", foxbot.getConfig().getCommandPrefix()));
    }
}
