package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

import java.util.List;

public class UserListener extends ListenerAdapter<FoxBot>
{
    private FoxBot foxbot;

    public UserListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onQuit(QuitEvent<FoxBot> event)
    {
        foxbot.getPermissionManager().removeAuthedUser(event.getUser());
    }

    @Override
    public void onInvite(InviteEvent<FoxBot> event)
    {
        if (foxbot.getConfig().getAutoJoinOnInvite() && foxbot.getPermissionManager().userHasPermission(foxbot.getUser(event.getUser()), "bot.invite"))
        {
            foxbot.joinChannel(event.getChannel());
            foxbot.sendNotice(event.getUser(), String.format("Joined %s", event.getChannel()));
        }
    }

    @Override
    public void onNickChange(NickChangeEvent<FoxBot> event)
    {
        User user = event.getUser();
        String newNick = event.getNewNick();
        List<String> tells = foxbot.getDatabase().getTells(user.getNick(), false);

        if (foxbot.getPermissionManager().isNickProtected(newNick))
        {
            for (Channel channel : foxbot.getChannels())
            {
                if (!channel.getOps().contains(foxbot.getUser(foxbot.getNick())))
                {
                    foxbot.partChannel(channel, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", newNick, newNick));
                    continue;
                }
                foxbot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick));
            }
            return;
        }

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                foxbot.sendMessage(user, foxbot.getUtils().colourise(tell));
            }
        }
    }

    @Override
    public void onJoin(JoinEvent event)
    {
        User user = event.getUser();
        String nick = user.getNick();
        Channel channel = event.getChannel();
        List<String> tells = foxbot.getDatabase().getTells(nick, false);

        if (nick.equals(foxbot.getNick()))
        {
            return;
        }

        if (foxbot.getPermissionManager().isNickProtected(nick))
        {
            for (Channel chan : foxbot.getChannels())
            {
                if (chan.getUsers().contains(user) && !chan.getOps().contains(foxbot.getUser(foxbot.getNick())))
                {
                    foxbot.partChannel(chan, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", nick, nick));
                    continue;
                }
                foxbot.kick(chan, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick));
            }
            return;
        }

        if (!foxbot.getConfig().getGreetingChannels().isEmpty() && !foxbot.getPermissionManager().userHasQuietPermission(user, "greetings.ignore") && foxbot.getConfig().getGreetingChannels().contains(channel.getName()))
        {
            if (foxbot.getConfig().getGreetingNotice())
            {
                foxbot.sendNotice(user, foxbot.getConfig().getGreetingMessage().replace("{USER}", nick).replace("{CHANNEL}", channel.getName()).replace("{CHANUSERS}", String.valueOf(channel.getUsers().size())));
            }
            else
            {
                channel.sendMessage(foxbot.getConfig().getGreetingMessage().replace("{USER}", nick).replace("{CHANNEL}", channel.getName()).replace("{CHANUSERS}", String.valueOf(channel.getUsers().size())));
            }
        }

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                foxbot.sendMessage(user, foxbot.getUtils().colourise(tell));
            }
        }
    }
}