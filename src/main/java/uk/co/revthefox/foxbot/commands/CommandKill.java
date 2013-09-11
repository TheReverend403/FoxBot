package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
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
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 0)
        {
            for (Channel channel : foxbot.getChannels())
            {
                foxbot.partChannel(channel, "Killed by " + sender.getNick());
            }

            foxbot.getDatabase().disconnect();
            foxbot.disconnect();
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %skill", foxbot.getConfig().getCommandPrefix()));
    }
}