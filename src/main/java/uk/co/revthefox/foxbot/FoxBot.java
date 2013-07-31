package uk.co.revthefox.foxbot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.PircBotX;
import uk.co.revthefox.foxbot.config.BotConfig;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;

public class FoxBot
{

    private PircBotX bot;
    private Config configFile;
    private Config permissionsFile;
    private BotConfig config;
    private PermissionManager permissions;

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    public void start(String[] args)
    {
        configFile = ConfigFactory.load("bot");
        permissionsFile = ConfigFactory.load("permissions");
        bot = new PircBotX();
        config = new BotConfig(this);
        permissions = new PermissionManager(this);
    }

    public void registerListeners()
    {
        bot.getListenerManager().addListener(new MessageListener(this));
    }

    public PircBotX getBot()
    {
        return bot;
    }

    public BotConfig getConfig()
    {
        return config;
    }

    public PermissionManager getPermissionManager()
    {
        return permissions;
    }

    public Config getConfigFile()
    {
        return configFile;
    }

    public Config getPermissionsFile()
    {
        return permissionsFile;
    }
}
