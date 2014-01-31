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
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter<FoxBot>
{
    private final FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private static final Pattern URL_PATTERN = Pattern.compile(".*((https?)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*");

    public void onMessage(MessageEvent<FoxBot> event)
    {
        String message = event.getMessage();
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (!foxbot.getConfig().getIgnoredChannels().contains(channel.getName()))
        {
            BotLogger.log(Level.INFO, String.format("MESSAGE: [%s] <%s%s> %s", channel.getName(), Utils.getPrefix(user, channel), user.getNick(), Colors.removeFormattingAndColors(message)));

            if (message.length() > 0 && (message.charAt(0) == foxbot.getConfig().getCommandPrefix() || message.startsWith(foxbot.getNick() + ", ")))
            {
                foxbot.getPluginManager().dispatchCommand(event, message.substring(message.charAt(0) == foxbot.getConfig().getCommandPrefix() ? 1 : foxbot.getConfig().getBotNick().length() + 2));
	            return;
            }

            Matcher matcher = URL_PATTERN.matcher(message);

            if (matcher.matches() && !user.getNick().equals(foxbot.getNick()) && foxbot.getPermissionManager().userHasQuietPermission(user, "chat.urls"))
            {
                message = Utils.parseChatUrl(matcher.group(1), user);

                if (!message.isEmpty() && !message.equalsIgnoreCase("null"))
                {
                    channel.sendMessage(message);
                }
            }
            return;
        }
        BotLogger.log(Level.INFO, String.format("MESSAGE: [%s (IGNORED)] <%s%s> %s", channel.getName(), Utils.getPrefix(user, channel), user.getNick(), Colors.removeFormattingAndColors(message)));
    }
}