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

public abstract class SpamHandler extends ListenerAdapter<FoxBot>
{
    private FoxBot foxbot;

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

    public HashMap<String, String> getDuplicateMap()
    {
        return duplicateMap;
    }

    public LoadingCache<String, Integer> getSpamRating()
    {
        return spamRating;
    }

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        run(event);
    }

    public abstract void run(final MessageEvent<FoxBot> event);

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
                foxbot.getDatabase().addBan(channel, user, "Antispam ban", foxbot.getUser(foxbot.getNick()), banTime);
                break;
            case 4:
                long kickTime = System.currentTimeMillis();
                foxbot.kick(channel, user, "Antispam kick");
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 60);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been kicked and muted for 60 seconds. If you continue to spam, you may be banned.");
                foxbot.getDatabase().addKick(channel, user, "Antispam kick", foxbot.getUser(foxbot.getNick()), kickTime);
                foxbot.getDatabase().addMute(channel, user, "Antispam kickmute", foxbot.getUser(foxbot.getNick()), kickTime);
                break;
            case 2:
                long muteTime = System.currentTimeMillis();
                foxbot.setMode(channel, "+q " + hostmask);
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", 10);
                foxbot.sendMessage(user, "It seems like you are spamming. As such, you have been muted for 10 seconds. If you continue to spam, you may be kicked or even banned.");
                foxbot.getDatabase().addMute(channel, user, "Antispam mute", foxbot.getUser(foxbot.getNick()), muteTime);
                break;
            default:
                break;
        }
    }
}