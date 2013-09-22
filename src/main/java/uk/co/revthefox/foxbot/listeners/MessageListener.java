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

package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.listeners.spamhandler.SpamHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter<FoxBot>
{
    private final FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private Pattern urlPattern = Pattern.compile(".*((https?)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*");

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        String message = event.getMessage();
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (foxbot.getConfig().getIgnoredChannels().contains(channel.getName()))
        {
            return;
        }

        if (message.length() > 0 && (message.startsWith(foxbot.getConfig().getCommandPrefix()) || message.startsWith(foxbot.getNick() + ", ")))
        {
            foxbot.getPluginManager().dispatchCommand(event, message.substring(message.startsWith(foxbot.getConfig().getCommandPrefix()) ? 1 : foxbot.getConfig().getBotNick().length() + 2));
        }

        Matcher matcher = urlPattern.matcher(message);

        if (matcher.matches() && !user.getNick().equals(foxbot.getNick()) && foxbot.getPermissionManager().userHasQuietPermission(user, "chat.urls"))
        {
            message = foxbot.getUtils().parseChatUrl(matcher.group(1), user);

            if (!message.isEmpty() && !message.equalsIgnoreCase("null"))
            {
                channel.sendMessage(message);
            }
        }
    }
}