package uk.co.revthefox.foxbot;

import org.pircbotx.PircBotX;
import uk.co.revthefox.foxbot.config.Config;
import uk.co.revthefox.foxbot.listeners.MessageListener;

public class FoxBot
{

    private PircBotX bot;
    private Config config;

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    public void start(String[] args)
    {
        bot = new PircBotX();
        config = new Config(this);
    }

    public void registerListeners()
    {
        bot.getListenerManager().addListener(new MessageListener(this));
    }

    public PircBotX getBot()
    {
        return bot;
    }

    public Config getConfig()
    {
        return config;
    }
}
