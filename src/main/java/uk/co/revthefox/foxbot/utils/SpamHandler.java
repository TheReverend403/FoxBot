package uk.co.revthefox.foxbot.utils;

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
    private FoxBot foxbot;

    private HashMap<String, String> duplicateMap = new HashMap<>();

    LoadingCache<String, Integer> spamCounter = CacheBuilder.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(
            new CacheLoader<String, Integer>()
            {
                public Integer load(String hostmask)
                {
                    return spamCounter.asMap().get(hostmask);
                }
            });

    public SpamHandler(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        final User user = event.getUser();
        final Channel channel = event.getChannel();

        /* Ideally, I'd use permissions here, but I won't for two reasons.
         *
         * 1. That would require a permissions check on every message from a potentially unverified user. Good way to get throttled.
         * 2. Voices+ would bypass the mutes anyway, regardless of perms. Might as well not spam the channel trying to mute them.
         */

        if (user.getNick().equals(foxbot.getNick()) || !channel.getNormalUsers().contains(user)
        {
            return;
        }

        final String message = event.getMessage();
        final String hostmask = user.getHostmask();
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

        // Easier way to get percentage?
        count = (count * 100) / length;

        // Make these values configurable
        if (message.length() > 5 && count > 75)
        {
            foxbot.kick(channel, user, "Caps spam (" + count + "%)");
        }

        if (!duplicateMap.containsKey(hostmask))
        {
            duplicateMap.put(hostmask, message);
            return;
        }

        if (message.equals(duplicateMap.get(hostmask)))
        {
            spamCounter.put(hostmask, spamCounter.asMap().get(hostmask) == null ? 1 : spamCounter.asMap().get(hostmask) + 1);
            duplicateMap.remove(hostmask);
            duplicateMap.put(hostmask, message);
            if (spamCounter.asMap().get(hostmask) != null && spamCounter.asMap().get(hostmask) != 0)
            {
                spamPunisher(channel, user, spamCounter.asMap().get(hostmask));
            }
            
        }
    }

    // Make most of the values here configurable
    public synchronized void spamPunisher(Channel channel, User user, int level)
    {
        // Help to prevent ban evasion
        String hostmask = "*" + user.getHostmask();

        switch (level)
        {
            case 9:
                foxbot.kick(channel, user, "AntiSpam kick");
                foxbot.ban(channel, hostmask);
                foxbot.getUtils().scheduleUnban(channel, hostmask, foxbot.getConfig().getUnbanTimer());
                foxbot.sendMessage(user, "You have been banned for 24 hours for spamming multiple times.");
                duplicateMap.remove(hostmask);
                spamCounter.asMap().remove(hostmask);
                break;
            case 4:
                foxbot.kick(channel, user, "AntiSpam kick");
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 60);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been kicked and muted for 60 seconds. If you continue to spam, you may be banned.");
                break;
            case 2:
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 10);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been muted for 10 seconds. If you continue to spam, you may be kicked or even banned.");
                break;
            default: break;
        }
    }
}
