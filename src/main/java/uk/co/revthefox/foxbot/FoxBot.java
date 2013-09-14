package uk.co.revthefox.foxbot;

import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.reflections.Reflections;
import uk.co.revthefox.foxbot.commands.Command;
import uk.co.revthefox.foxbot.config.Config;
import uk.co.revthefox.foxbot.database.Database;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.listeners.UserListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;
import uk.co.revthefox.foxbot.plugin.PluginManager;
import uk.co.revthefox.foxbot.listeners.spamhandler.SpamHandler;
import uk.co.revthefox.foxbot.utils.Utils;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoxBot - An IRC bot written in Java
 *
 * @author TheReverend403 (Lee Watson)
 * @website http://revthefox.co.uk
 * @repo https://github.com/TheReverend403/FoxBot
 */

public class FoxBot extends PircBotX
{
    private static Config config;
    private static PermissionManager permissions;
    private static PluginManager pluginManager;
    private static Utils utils;
    private static Database database;
    private static Reflections reflections = new Reflections("uk.co.revthefox");

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {
        File path = new File("data/customcmds/");

        if (!path.exists() && !path.mkdirs())
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "Couldn't create data folder. Shutting down.");
            this.disconnect();
            return;
        }

        config = new Config(this);
        permissions = new PermissionManager(this);
        pluginManager = new PluginManager(this);
        utils = new Utils(this);
        database = new Database(this);
        database.connect();
        registerListeners();
        registerCommands();
        setBotInfo();
        connectToServer();
    }

    private void setBotInfo()
    {
        this.setVerbose(config.getDebug());
        this.setAutoNickChange(config.getAutoNickChange());
        this.setAutoReconnect(config.getAutoReconnect());
        this.setMessageDelay(config.getMessageDelay());
        this.setVersion("FoxBot - A Java IRC bot written by TheReverend403 - https://github.com/TheReverend403/FoxBot");
        this.setAutoSplitMessage(true);
        this.setName(config.getBotNick());
        this.setLogin(config.getBotIdent());
    }

    private void connectToServer()
    {
        try
        {
            if (config.getServerSsl())
            {
                this.connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword(), config.getAcceptInvalidSsl() ? new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman() : SSLSocketFactory.getDefault());
            }
            else
            {
                this.connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword());
            }

            if (config.useNickserv())
            {
                this.identify(config.getNickservPassword());
            }
        }
        catch (IOException | IrcException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        for (String channel : config.getChannels())
        {
            this.joinChannel(channel);
        }
    }

    private void registerListeners()
    {
        this.getListenerManager().addListener(new MessageListener(this));
        this.getListenerManager().addListener(new UserListener(this));
        this.getListenerManager().addListener(new SpamHandler(this));
    }

    private void registerCommands()
    {
        try
        {
            for (Class clazz : reflections.getSubTypesOf(Command.class))
            {
                ClassLoader.getSystemClassLoader().loadClass(clazz.getName());
                Constructor clazzConstructor = clazz.getConstructor(this.getClass());
                Command command = (Command) clazzConstructor.newInstance(this);
                this.getPluginManager().registerCommand(command);
            }
        }
        catch (Exception ex)
        {
            // This can never happen.
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Config getConfig()
    {
        return config;
    }

    public PermissionManager getPermissionManager()
    {
        return permissions;
    }

    public PluginManager getPluginManager()
    {
        return pluginManager;
    }

    public Utils getUtils()
    {
        return utils;
    }

    public Database getDatabase()
    {
        return database;
    }
}