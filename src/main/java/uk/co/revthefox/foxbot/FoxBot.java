package uk.co.revthefox.foxbot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import uk.co.revthefox.foxbot.config.BotConfig;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;

import java.io.IOException;

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

    private void start(String[] args)
    {
        configFile = ConfigFactory.load("bot");
        permissionsFile = ConfigFactory.load("permissions");
        bot = new PircBotX();
        config = new BotConfig(this);
        permissions = new PermissionManager(this);
        registerListeners();
        setBotInfo();
        connectToServer();
        joinChannels();
    }

    private void setBotInfo()
    {
        bot.setVerbose(config.getDebug());
        bot.setAutoNickChange(config.getAutoNickChange());
        bot.setAutoReconnect(config.getAutoReconnect());
        bot.setName(config.getBotNick());
    }

    private void connectToServer()
    {
        try
        {
            bot.connect(config.getServerAddress(), config.getServerPort());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (NickAlreadyInUseException ex)
        {

        }
        catch (IrcException ex)
        {
            ex.printStackTrace();
        }
    }

    private void joinChannels()
    {
        for (String channel : config.getChannels())
        {
            bot.joinChannel(channel);
        }
    }

    private void registerListeners()
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
