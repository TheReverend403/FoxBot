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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SpamHandler extends ListenerAdapter<FoxBot>
{
    private FoxBot foxbot;

    private HashMap<String, String> duplicateMap = new HashMap<>();
    public static Set<String> currentPunishments = new HashSet<>();

    LoadingCache<String, Integer> spamCounter = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(
            new CacheLoader<String, Integer>()
            {
                public Integer load(String hostmask)
                {
                    return spamCounter.asMap().get(hostmask);
                }
            });
    //private static HashMap<User, Integer> spamCounter = new HashMap<>();

    public SpamHandler(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        final User user = event.getUser();
        final Channel channel = event.getChannel();
        final String message = event.getMessage();
        final String hostmask = user.getHostmask();

        if (currentPunishments.contains(hostmask))

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
            spamPunisher(channel, user, spamCounter.asMap().get(hostmask) == null ? 0 : spamCounter.asMap().get(hostmask));
        }
    }

    public synchronized void spamPunisher(Channel channel, User user, int level)
    {
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
                currentPunishments.remove(hostmask);
                break;
            case 4:
                foxbot.kick(channel, user, "AntiSpam kick");
                foxbot.setMode(channel, "+q " + hostmask);
               // foxbot.sendRawLine(String.format("mode %s +q *!*@%s", channel.getName(), hostmask));
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 60);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been kicked and muted for 60 seconds. If you continue to spam, you may be banned.");
                currentPunishments.add(hostmask);
                break;
            case 2:
                foxbot.setMode(channel, "+q " + hostmask);
                //foxbot.sendRawLine(String.format("mode %s +q *!*@%s", channel.getName(), hostmask));
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 10);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been muted for 10 seconds. If you continue to spam, you may be kicked or even banned.");
                currentPunishments.add(hostmask);
                break;
            default: break;
        }
    }
}
