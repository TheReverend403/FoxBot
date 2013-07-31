package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandBan
{
    private FoxBot foxbot;

    public CommandBan(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void execute(Channel channel, User commandSender, String[] args)
    {
        if (!foxbot.getPermissionManager().userHasPermission(commandSender, "command.ban"))
        {
            foxbot.getBot().sendNotice(commandSender, "You do not have permission to do this.");
            return;
        }
        if (args.length == 0)
        {
            channel.sendMessage("Ban command executed successfully without args!");
            return;
        }
        channel.sendMessage(String.format("Ban command executed successfully with %s args!", args.length));
    }
}
