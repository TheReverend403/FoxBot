package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandPart extends Command
{
    private FoxBot foxbot;

    public CommandPart(FoxBot foxbot)
    {
        super("part", "command.part");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! Use %spart <#channel> [#channel2 #channel3 ...]",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }

        Outer:
        for (int arg = 1; arg < args.length; arg++)
        {
            if (args[arg].startsWith("#"))
            {
                if (foxbot.getBot().getChannels().contains(args[arg]))
                {
                        foxbot.getBot().partChannel(foxbot.getBot().getChannel(args[arg]), String.format("Part command used by %s", sender.getNick()));
                        foxbot.getBot().sendNotice(sender, String.format("Successfully left %s", args[arg]));
                        break Outer;
                }
                foxbot.getBot().sendNotice(sender, String.format("I am not in the channel %s", args[arg]));
                break Outer;
            }
            foxbot.getBot().sendNotice(sender, String.format("%s is not a channel...", args[arg]));
        }
    }
}
