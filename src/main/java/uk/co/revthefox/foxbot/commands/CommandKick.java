package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKick extends Command
{
    private FoxBot foxbot;

    public CommandKick(FoxBot foxbot)
    {
        super("kick", "command.kick");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %skick <nick> [reason]",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }
        if (foxbot.getBot().getUser(args[0]) == null)
        {
            foxbot.getBot().sendNotice(sender, "That user is not in this channel");
            return;
        }
        if (args.length > 1)
        {
            StringBuilder reason = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                reason.append(" ").append(args[arg]);
            }

            foxbot.getBot().kick(channel, foxbot.getBot().getUser(args[0]), reason.toString());
            return;
        }
        foxbot.getBot().kick(channel, foxbot.getBot().getUser(args[0]));
    }
}
