package uk.co.revthefox.foxbot.commands;


import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandDevoice extends Command
{
    private FoxBot foxbot;

    public CommandDevoice(FoxBot foxbot)
    {
        super("devoice", "command.devoice");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();

        if (args.length > 0)
        {
            if (foxbot.getPermissionManager().userHasQuietPermission(sender, "command.devoice.others"))
            {
                for (String target : args)
                {
                    if (channel.getVoices().contains(foxbot.getUser(target)))
                    {
                        foxbot.deVoice(channel, foxbot.getUser(target));
                    }
                }
            }
            foxbot.sendNotice(sender, "You do not have permission to devoice other users!");
            return;
        }
        if (channel.getVoices().contains(sender))
        {
            foxbot.deVoice(channel, sender);
            return;
        }
        foxbot.sendNotice(sender, "You are not voice!");
    }
}
