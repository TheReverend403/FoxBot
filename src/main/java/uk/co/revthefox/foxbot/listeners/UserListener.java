package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import uk.co.revthefox.foxbot.FoxBot;

public class UserListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public UserListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onQuit(QuitEvent event)
    {
        foxbot.getPermissionManager().removeAuthedUser(event.getUser());
    }

    @Override
    public void onInvite(InviteEvent event)
    {
        PircBotX bot = foxbot.getBot();

        if (foxbot.getConfig().getAutoJoinOnInvite() && foxbot.getPermissionManager().userHasPermission(bot.getUser(event.getUser()), "bot.invite"))
        {
            bot.joinChannel(event.getChannel());
            bot.sendNotice(event.getUser(), String.format("Joined %s", event.getChannel()));
        }
    }
}