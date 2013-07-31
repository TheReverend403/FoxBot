package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKick
{
    private FoxBot foxbot;

    public CommandKick(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void execute(Channel channel, User commandSender, String[] args)
    {
        if (!foxbot.getPermissionManager().userHasPermission(commandSender, "command.kick"))
        {
            foxbot.getBot().sendNotice(commandSender, "You do not have permission to do this.");
            return;
        }
        if (args.length == 0)
        {
            foxbot.getBot().sendNotice(commandSender, String.format("Wrong number of args! use %skick <nick> [reason]", foxbot.getConfig().getCommandPrefix()));
        }
        if (foxbot.getBot().getUser(args[0]).equals(null))
        {
            foxbot.getBot().sendNotice(commandSender, "That user does not exist!");
            return;
        }
        if (args[1] != null)
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
