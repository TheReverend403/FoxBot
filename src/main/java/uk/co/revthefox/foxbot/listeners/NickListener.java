package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.NickChangeEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class NickListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public NickListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onNickChange(NickChangeEvent event)
    {
        //foxbot.getPermissionManager().removeAuthedUser(event.getUser());
    }
}
