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

package uk.co.revthefox.foxbot.listeners.spamhandler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SpamHandler extends ListenerAdapter<FoxBot>
{
    private final FoxBot foxbot;

    private final HashMap<String, String> duplicateMap = new HashMap<>();

    // Use a loading cache here so we can reset a certain user's spam rating after X minutes of not being increased.
    final LoadingCache<String, Integer> spamRating = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, Integer>()
                    {
                        @Override
                        public Integer load(String hostmask)
                        {
                            return spamRating.asMap().get(hostmask);
                        }
                    });

    public SpamHandler(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (!foxbot.getConfig().getIgnoredChannels().contains(channel.getName()))
        {
           /* Ideally, I'd use permissions here, but I won't for two reasons.
            *
            * 1. That would require a permissions check on every message from a potentially unverified user. Good way to get throttled.
            * 2. Voices+ would bypass the mutes anyway, regardless of perms. Might as well not spam the channel trying to mute them.
            */

            if (user.getNick().equals(foxbot.getNick()) || channel.getNormalUsers().contains(foxbot.getUserBot()) || !channel.getNormalUsers().contains(user))
            {
                return;
            }

            String message = event.getMessage();
            String hostmask = user.getHostmask();

            // -------------------
            // Caps spam detection
            // -------------------

            int count = 0;
            int length = 0;

            for (char character : message.toCharArray())
            {
                // Don't count spaces, it messes with the final percentage
                if (Character.isAlphabetic(character))
                {
                    length++;

                    if (Character.isUpperCase(character))
                    {
                        count++;
                    }
                }
            }

            // Prevent divide-by-zero errors
            if (length > 5)
            {
                count = (count * 100) / length;

                // Kick the user if the percentage of caps in their message was higher than the max value
                if (count > 75)
                {
                    long kickTime = System.currentTimeMillis();
                    foxbot.kick(channel, user, "Caps spam (" + count + "%)");
                    foxbot.getDatabase().addKick(channel, user, "Caps spam (" + count + "%)", foxbot.getUserBot(), kickTime);
                }
            }

            // -----------------------
            // End caps spam detection
            // -----------------------

            // ---------------------
            // Repeat spam detection
            // ---------------------

            if (!duplicateMap.containsKey(hostmask))
            {
                duplicateMap.put(hostmask, message);
                return;
            }

            if (message.equals(duplicateMap.get(hostmask)))
            {
                spamRating.put(hostmask, spamRating.asMap().get(hostmask) == null ? 1 : spamRating.asMap().get(hostmask) + 1);
                duplicateMap.put(hostmask, message);
            }

            // -------------------------
            // End repeat spam detection
            // -------------------------

            // Take action on the spam rating
            if (spamRating.asMap().get(hostmask) != null && spamRating.asMap().get(hostmask) != 0)
            {
                spamPunisher(channel, user, spamRating.asMap().get(hostmask));
            }
        }
    }

    // Make most of the values here configurable
    public synchronized void spamPunisher(final Channel channel, final User user, final int level)
    {
        // Help to prevent ban evasion
        String hostmask = "*" + user.getHostmask();

        switch (level)
        {
            case 9:
                foxbot.kick(channel, user, "Antispam ban");
                long banTime = System.currentTimeMillis();
                foxbot.ban(channel, hostmask);
                foxbot.getUtils().scheduleUnban(channel, hostmask, foxbot.getConfig().getUnbanTimer());
                foxbot.sendMessage(user, "You have been banned for 24 hours for spamming multiple times.");
                duplicateMap.remove(hostmask);
                spamRating.asMap().remove(hostmask);
                foxbot.getDatabase().addBan(channel, user, "Antispam ban", foxbot.getUserBot(), banTime);
                break;
            case 4:
                long kickTime = System.currentTimeMillis();
                foxbot.kick(channel, user, "Antispam kick");
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 60);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been kicked and muted for 60 seconds. If you continue to spam, you may be banned.");
                foxbot.getDatabase().addKick(channel, user, "Antispam kick", foxbot.getUserBot(), kickTime);
                foxbot.getDatabase().addMute(channel, user, "Antispam kickmute", foxbot.getUserBot(), kickTime);
                break;
            case 2:
                long muteTime = System.currentTimeMillis();
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 10);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been muted for 10 seconds. If you continue to spam, you may be kicked or even banned.");
                foxbot.getDatabase().addMute(channel, user, "Antispam mute", foxbot.getUserBot(), muteTime);
                break;
            default:
                break;
        }
    }
}