package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.List;

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

    @Override
    public void onNickChange(NickChangeEvent event)
    {
        User user = event.getUser();
        String newNick = event.getNewNick();
        PircBotX bot = foxbot.getBot();
        List<String> tells = foxbot.getDatabase().getTells(user.getNick(), false);

        if (foxbot.getPermissionManager().isNickProtected(newNick))
        {
            for (Channel channel : bot.getChannels())
            {
                bot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick));
            }
            return;
        }

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                bot.sendMessage(user, tell);
            }
        }
    }

    @Override
    public void onJoin(JoinEvent event)
    {
        User user = event.getUser();
        String nick = user.getNick();
        PircBotX bot = foxbot.getBot();
        List<String> tells = foxbot.getDatabase().getTells(nick, false);

        if (foxbot.getPermissionManager().isNickProtected(nick))
        {
            for (Channel channel : bot.getChannels())
            {
                bot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick));
            }
            return;
        }

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                bot.sendMessage(user, tell);
            }
        }
    }
}