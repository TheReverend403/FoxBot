package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKill
{
    private FoxBot foxbot;

    public CommandKill(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void execute(Channel channel, User commandSender, String[] args)
    {
        if (!foxbot.getPermissionManager().userHasPermission(commandSender, "command.kill"))
        {
            foxbot.getBot().sendNotice(commandSender, "You do not have permission to do this.");
            return;
        }
        if (args.length != 0)
        {
            foxbot.getBot().sendNotice(commandSender, String.format("Wrong number of args! use %skill", foxbot.getConfig().getCommandPrefix()));
            return;
        }
        for (Channel botChannel : foxbot.getBot().getChannels())
        {
            foxbot.getBot().partChannel(botChannel, "Killed by: " + commandSender.getNick());
        }
        foxbot.getBot().disconnect();
    }
}
