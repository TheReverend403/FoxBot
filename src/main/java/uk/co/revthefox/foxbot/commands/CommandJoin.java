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
        if (args.length != 0)
        {
            for (int arg = 0; arg < args.length; arg++)
            {
                if (args[arg].startsWith("#"))
                {
                    if (!foxbot.getBot().getChannel(args[arg]).isInviteOnly())
                    {
                        foxbot.getBot().joinChannel(args[arg]);
                        foxbot.getBot().sendNotice(sender, String.format("Joined %s", args[arg]));
                        continue;
                    }
                    foxbot.getBot().sendNotice(sender, String.format("%s is invite only!", args[arg]));
                    continue;
                }
                foxbot.getBot().sendNotice(sender, String.format("%s is not a channel...", args[arg]));
            }
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ...]",
                foxbot.getConfig().getCommandPrefix()));
    }
}
