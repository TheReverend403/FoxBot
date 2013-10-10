/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.listeners;

import co.foxdev.foxbot.logger.BotLogger;
import co.foxdev.foxbot.logger.LogLevel;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import co.foxdev.foxbot.FoxBot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UserListener extends ListenerAdapter<FoxBot>
{
    private final FoxBot foxbot;

    public UserListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onQuit(QuitEvent<FoxBot> event)
    {
        BotLogger.log(LogLevel.INFO, String.format("QUIT: %s has quit", event.getUser().getNick()));
        foxbot.getPermissionManager().removeAuthedUser(event.getUser());
    }

    @Override
    public void onConnect(ConnectEvent<FoxBot> event)
    {
        BotLogger.log(LogLevel.INFO, String.format("CONNECT: Trying address %s", foxbot.getConfig().getServerAddress()));

        for (String line : foxbot.getServerInfo().getMotd().split("\n"))
        {
            BotLogger.log(LogLevel.INFO, String.format("CONNECT: %s", line));
        }
    }

    @Override
    public void onInvite(InviteEvent<FoxBot> event)
    {
        BotLogger.log(LogLevel.INFO, String.format("INVITE: Invite from %s to %s", event.getUser(), event.getChannel()));

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

        BotLogger.log(LogLevel.INFO, String.format("NICK: Nick change from %s to %s", user.getNick(), newNick));

        if (foxbot.getPermissionManager().isNickProtected(newNick))
        {
            for (Channel channel : foxbot.getChannels())
            {
                if (channel.getUsers().contains(user))
                {
                    if (!channel.isOp(foxbot.getUserBot()))
                    {
                        foxbot.partChannel(channel, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", newNick, newNick));
                        continue;
                    }

                    long kickTime = System.currentTimeMillis();

                    foxbot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick));
                    foxbot.getDatabase().addKick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", newNick), foxbot.getUserBot(), kickTime);
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
    public void onJoin(JoinEvent<FoxBot> event)
    {
        User user = event.getUser();
        String nick = user.getNick();
        Channel channel = event.getChannel();

        BotLogger.log(LogLevel.INFO, String.format("JOIN: %s has joined %s", user.getNick(), channel.getName()));

        if (nick.equals(foxbot.getNick()))
        {
            return;
        }

        if (foxbot.getPermissionManager().isNickProtected(nick))
        {
            if (!channel.isOp(foxbot.getUserBot()))
            {
                foxbot.partChannel(channel, String.format("'%s' is on my protected nick list. I am not able to kick '%s', so I am leaving this channel as a security measure.", nick, nick));
                return;
            }

            long kickTime = System.currentTimeMillis();

            foxbot.kick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick));
            foxbot.getDatabase().addKick(channel, user, String.format("The nick '%s' is protected. Either connect with the associated hostmask or do not use that nick.", nick), foxbot.getUserBot(), kickTime);
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
    public void onPart(PartEvent<FoxBot> event)
    {
        BotLogger.log(LogLevel.INFO, String.format("PART: %s has left %s", event.getUser().getNick(), event.getChannel().getName()));
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event)
    {
        BotLogger.log(LogLevel.INFO, String.format("PRIVMSG: Message from %s: %s", event.getUser().getNick(), Colors.removeFormattingAndColors(event.getMessage())));
    }

    @Override
    public void onNotice(NoticeEvent event)
    {
        BotLogger.log(LogLevel.INFO, String.format("NOTICE: Notice from %s: %s", event.getUser().getNick(), Colors.removeFormattingAndColors(event.getMessage())));
    }

    @Override
    public void onServerPing(ServerPingEvent event)
    {
        BotLogger.log(LogLevel.INFO, String.format("PING: Ping from %s", foxbot.getServerInfo().getServerName()));
        BotLogger.log(LogLevel.INFO, String.format("PING: Responding to ping from %s", foxbot.getServerInfo().getServerName()));
    }

    @Override
    public void onKick(KickEvent<FoxBot> event)
    {
        final Channel channel = event.getChannel();
        final User kickedUser = event.getRecipient();
        final User kicker = event.getSource();
        String reason = event.getReason();

        BotLogger.log(LogLevel.INFO, String.format("KICK: %s has kicked %s from %s %s", kicker.getNick(), kickedUser.getNick(), channel.getName(), reason.isEmpty() ? "" : "for " + event.getReason()));

        if (kickedUser.getNick().equals(foxbot.getNick()))
        {
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