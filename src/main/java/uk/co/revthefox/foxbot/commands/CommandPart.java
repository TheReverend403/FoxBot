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
        if (args.length != 0)
        {
            for (int arg = 0; arg < args.length; arg++)
            {
                if (args[arg].startsWith("#"))
                {
                    foxbot.getBot().partChannel(foxbot.getBot().getChannel(args[arg]), String.format("Part command used by %s", sender.getNick()));
                    foxbot.getBot().sendNotice(sender, String.format("Leaving %s", args[arg]));
                    continue;
                }
                foxbot.getBot().sendNotice(sender, String.format("%s is not a channel...", args[arg]));
            }
            return;
        }
        foxbot.getBot().partChannel(channel);
        foxbot.getBot().sendNotice(sender, String.format("Leaving %s", channel.getName()));
    }
}
