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

    private static HashMap<User, String> duplicateMap = new HashMap<>();
    LoadingCache<User, Integer> spamCounter = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(
            new CacheLoader<User, Integer>()
            {
                public Integer load(User user)
                {
                    return spamCounter.asMap().get(user);
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

        if (!duplicateMap.containsKey(user))
        {
            duplicateMap.put(user, message);
            return;
        }

        if (message.equals(duplicateMap.get(user)))
        {
            spamCounter.put(user, spamCounter.asMap().get(user) == null ? 1 : spamCounter.asMap().get(user) + 1);
            duplicateMap.remove(user);
            duplicateMap.put(user, message);
        }
        spamPunisher(channel, user, spamCounter.asMap().get(user) == null ? 0 : spamCounter.asMap().get(user));
    }

    public synchronized void spamPunisher(Channel channel, User user, int level)
    {
        String hostmask = user.getHostmask();
        switch (level)
        {
            case 9:
                //foxbot.setMode(channel, "+q", user);
                foxbot.kick(channel, user, "AntiSpam kick");
                foxbot.ban(channel, hostmask);
                foxbot.getUtils().scheduleUnban(channel, hostmask, foxbot.getConfig().getUnbanTimer());
                foxbot.sendMessage(user, "You have been banned for 24 hours for spamming multiple times.");
                break;
            case 4:
                foxbot.kick(channel, user, "AntiSpam kick");
                foxbot.sendRawLine(String.format("mode %s +q *!*@*%s", channel.getName(), hostmask));
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 60);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been kicked and muted for 60 seconds. If you continue to spam, you may be banned.");
                break;
            case 2:
                //foxbot.setMode(channel, "+q", user);
                foxbot.sendRawLine(String.format("mode %s +q *!*@*%s", channel.getName(), hostmask));
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 10);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been muted for 10 seconds. If you continue to spam, you may be kicked or even banned.");
                break;
            default: break;
        }
    }
}
