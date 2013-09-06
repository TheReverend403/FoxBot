package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.Utils;

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
                if (!channel.getOps().contains(bot.getUser(bot.getNick())))
                {
                    bot.partChannel(channel, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", newNick, newNick));
                    continue;
                }
                bot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick));
            }
            return;
        }

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                bot.sendMessage(user, Utils.colourise(tell));
            }
        }
    }

    @Override
    public void onJoin(JoinEvent event)
    {
        User user = event.getUser();
        String nick = user.getNick();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();
        List<String> tells = foxbot.getDatabase().getTells(nick, false);

        if (nick.equals(bot.getNick()))
        {
            return;
        }

        if (foxbot.getPermissionManager().isNickProtected(nick))
        {
            for (Channel chan : bot.getChannels())
            {
                if (chan.getUsers().contains(user) && !chan.getOps().contains(bot.getUser(bot.getNick())))
                {
                    bot.partChannel(chan, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", nick, nick));
                    continue;
                }
                bot.kick(chan, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick));
            }
            return;
        }

        if (!foxbot.getConfig().getGreetingChannels().isEmpty() && !foxbot.getPermissionManager().userHasQuietPermission(user, "greetings.ignore") && foxbot.getConfig().getGreetingChannels().contains(channel.getName()))
        {
            if (foxbot.getConfig().getGreetingNotice())
            {
                bot.sendNotice(user, foxbot.getConfig().getGreetingMessage().replace("{USER}", nick).replace("{CHANNEL}", channel.getName()).replace("{CHANUSERS}", String.valueOf(channel.getUsers().size())));
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
                bot.sendMessage(user, Utils.colourise(tell));
            }
        }
    }
}