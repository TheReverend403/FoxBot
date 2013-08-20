package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class InviteListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public InviteListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onInvite(InviteEvent event)
    {
        PircBotX bot = foxbot.getBot();

        if (foxbot.getConfig().getAutoJoinOnInvite() && foxbot.getPermissionManager().userHasPermission(bot.getUser(event.getUser()), "bot.invite"))
        {
            bot.joinChannel(event.getChannel());
            bot.sendNotice(bot.getUser(event.getUser()), String.format("Joined %s", event.getChannel()));
        }
    }
}
