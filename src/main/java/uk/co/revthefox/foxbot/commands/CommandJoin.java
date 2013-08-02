package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
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
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ...]",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }

        Outer:
        for (int arg = 1; arg < args.length; arg++)
        {
            if (args[arg].startsWith("#"))
            {
                if (!foxbot.getBot().getChannels().contains(args[arg]))
                {
                    if (!foxbot.getBot().getChannel(args[arg]).isInviteOnly())
                    {
                        foxbot.getBot().joinChannel(args[arg]);
                        foxbot.getBot().sendNotice(sender, String.format("Successfully joined %s", args[arg]));
                        break Outer;
                    }
                    foxbot.getBot().sendNotice(sender, String.format("The channel %s is invite only!", args[arg]));
                }
                foxbot.getBot().sendNotice(sender, String.format("I am already in the channel %s", args[arg]));
                break Outer;
            }
            foxbot.getBot().sendNotice(sender, String.format("%s is not a channel...", args[arg]));
        }
    }
}
