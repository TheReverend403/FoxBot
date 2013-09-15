package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandVoice extends Command
{
    private FoxBot foxbot;

    public CommandVoice(FoxBot foxbot)
    {
        super("voice", "command.voice");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();

        if (args.length > 0)
        {
            if (foxbot.getPermissionManager().userHasQuietPermission(sender, "command.voice.others"))
            {
                for (String target : args)
                {
                    if (!channel.getVoices().contains(foxbot.getUser(target)))
                    {
                        foxbot.voice(channel, foxbot.getUser(target));
                    }
                }
                return;
            }
            foxbot.sendNotice(sender, "You do not have permission to voice other users!");
            return;
        }

        if (!channel.getVoices().contains(sender))
        {
            foxbot.voice(channel, sender);
            return;
        }
        foxbot.sendNotice(sender, "You are already voice!");
    }
}
//