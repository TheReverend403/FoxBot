package uk.co.revthefox.foxbot.listeners;

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
        if (foxbot.getConfig().getAutoJoinOnInvite() && foxbot.getPermissionManager().userHasPermission(foxbot.getBot().getUser(event.getUser()), "bot.invite"))
        {
            foxbot.getBot().joinChannel(event.getChannel());
            foxbot.getBot().sendNotice(foxbot.getBot().getUser(event.getUser()), String.format("Joined %s", event.getChannel()));
        }
    }

    /*
    @Override
    public void onNickChange(NickChangeEvent event)
    {
        String beforeNick = event.getOldNick();
        String afterNick = event.getNewNick();
        String[] split = beforeNick.split("\\|");

        if (split[1].equalsIgnoreCase("afk") || split[1].equalsIgnoreCase("away") && (!afterNick.contains("|afk") & !afterNick.contains("|afk")))
        {
            // code here
        }
    }
    */
}
