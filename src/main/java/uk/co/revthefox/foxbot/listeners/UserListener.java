package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

        if (foxbot.getPermissionManager().isNickProtected(newNick))
        {
            for (Channel channel : foxbot.getChannels())
            {
                if (channel.getUsers().contains(user))
                {
                    if (!channel.getOps().contains(foxbot.getUser(foxbot.getNick())))
                    {
                        foxbot.partChannel(channel, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", newNick, newNick));
                        continue;
                    }
                    long kickTime = System.currentTimeMillis();
                    foxbot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick));
                    foxbot.getDatabase().addKick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick), foxbot.getUser(foxbot.getNick()), kickTime);
                }
            }
            return;
        }

        List<String> tells = foxbot.getDatabase().getTells(user.getNick(), false);

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

        if (nick.equals(foxbot.getNick()))
        {
            return;
        }

        if (foxbot.getPermissionManager().isNickProtected(nick))
        {
            for (Channel chan : foxbot.getChannels())
            {
                if (chan.getUsers().contains(user))
                {
                    if (!chan.getOps().contains(foxbot.getUser(foxbot.getNick())))
                    {
                        foxbot.partChannel(chan, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", nick, nick));
                        continue;
                    }
                    long kickTime = System.currentTimeMillis();
                    foxbot.kick(chan, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick));
                    foxbot.getDatabase().addKick(chan, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick), foxbot.getUser(foxbot.getNick()), kickTime);
                }
            }
            return;
        }

        if (!foxbot.getConfig().getGreetingChannels().isEmpty() && !foxbot.getPermissionManager().userHasQuietPermission(user, "greetings.ignore") && foxbot.getConfig().getGreetingChannels().contains(channel.getName()))
        {
            if (foxbot.getConfig().getGreetingNotice())
            {
                foxbot.sendNotice(user, foxbot.getUtils().colourise(foxbot.getConfig().getGreetingMessage().replace("{USER}", nick).replace("{CHANNEL}", channel.getName()).replace("{CHANUSERS}", String.valueOf(channel.getUsers().size()))));
            }
            else
            {
                channel.sendMessage(foxbot.getUtils().colourise(foxbot.getConfig().getGreetingMessage().replace("{USER}", nick).replace("{CHANNEL}", channel.getName()).replace("{CHANUSERS}", String.valueOf(channel.getUsers().size()))));
            }
        }

        List<String> tells = foxbot.getDatabase().getTells(nick, false);

        if (!tells.isEmpty())
        {
            for (String tell : tells)
            {
                foxbot.sendMessage(user, foxbot.getUtils().colourise(tell));
            }
        }
    }

    @Override
    public void onKick(final KickEvent event)
    {
        final Channel channel = event.getChannel();
        final User kickedUser = event.getRecipient();
        final User kicker = event.getSource();

        if (kickedUser.getNick().equals(foxbot.getNick()))
        {
            /*
            if (foxbot.getConfig().getPunishUsersOnKick() && !foxbot.getPermissionManager().userHasQuietPermission(kicker, "bot.bypasspunishment"))
            {
                foxbot.sendNotice("ChanServ", String.format("kick %s %s %s", channel.getName(), kicker.getNick(), foxbot.getConfig().getPunishmentKickReason() == null ? "" : foxbot.getConfig().getPunishmentKickReason()));
                //foxbot.kick(channel, kicker, foxbot.getConfig().getPunishmentKickReason() == null ? "" : foxbot.getConfig().getPunishmentKickReason());
            }
            */

            if (foxbot.getConfig().getAutoRejoinOnKick() && !foxbot.getPermissionManager().userHasQuietPermission(kicker, "bot.allowkick"))
            {
                new Timer().schedule(
                        new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                foxbot.joinChannel(channel.getName());
                            }
                        },
                        TimeUnit.SECONDS.toMillis(foxbot.getConfig().getAutoRejoinDelay())
                );
            }
        }
    }
}