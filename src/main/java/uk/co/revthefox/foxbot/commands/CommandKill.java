package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKill extends Command
{

    private FoxBot foxbot;

    public CommandKill(FoxBot foxbot)
    {
        super("kill", "command.kill");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length != 0)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %skill",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }
        for (Channel botChannel : foxbot.getBot().getChannels())
        {
            foxbot.getBot().partChannel(botChannel, "Killed by " + sender.getNick());
        }
        foxbot.getBot().disconnect();
    }
}
