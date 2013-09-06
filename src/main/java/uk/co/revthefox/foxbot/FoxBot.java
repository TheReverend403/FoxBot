package uk.co.revthefox.foxbot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.managers.BackgroundListenerManager;
import org.reflections.Reflections;
import uk.co.revthefox.foxbot.commands.Command;
import uk.co.revthefox.foxbot.config.BotConfig;
import uk.co.revthefox.foxbot.database.Database;
import uk.co.revthefox.foxbot.listeners.UserListener;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;
import uk.co.revthefox.foxbot.utils.Utils;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Arrays;
/**
 * FoxBot - An IRC bot written in Java
 *
 * @author TheReverend403 (Lee Watson)
 * @website http://revthefox.co.uk
 * @repo https://github.com/TheReverend403/FoxBot
 */

public class FoxBot
{
    private static PircBotX bot;
    private static BotConfig config;
    private static PermissionManager permissions;
    private static Utils utils;
    private static CommandManager commandManager;
    private static Database database;
    private static Reflections reflections = new Reflections("uk.co.revthefox");
    private static BackgroundListenerManager backgroundListenerManager = new BackgroundListenerManager();

    private Config configFile;
    private Config permissionsFile;
    private Config nickProtectionFile;

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {
        for (String file : Arrays.asList("bot.conf", "permissions.conf", "nickprotection.conf"))
        {
            if (!new File(file).exists())
            {
                System.out.println(String.format("Generating default %s!", file));
                InputStream confInStream = this.getClass().getResourceAsStream("/" + file);
                OutputStream confOutStream;
                int readBytes;
                byte[] buffer = new byte[4096];

                try
                {
                    confOutStream = new FileOutputStream(new File(file));
                    while ((readBytes = confInStream.read(buffer)) > 0)
                    {
                        confOutStream.write(buffer, 0, readBytes);
                    }
                    confInStream.close();
                    confOutStream.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    bot.disconnect();
                }
            }
        }

        File path = new File("data/customcmds/");

        if (!path.exists() && !path.mkdirs())
        {
            System.out.println("Couldn't create data folder. Shutting down.");
            bot.disconnect();
            return;
        }

        loadConfigFiles();
        bot = new PircBotX();
        config = new BotConfig(this);
        permissions = new PermissionManager(this);
        utils = new Utils(this);
        commandManager = new CommandManager(this);
        database = new Database(this);
        database.connect();
        registerListeners();
        registerCommands();
        setBotInfo();
        connectToServer();
    }

    private void setBotInfo()
    {
        bot.setVerbose(config.getDebug());
        bot.setAutoNickChange(config.getAutoNickChange());
        bot.setAutoReconnect(config.getAutoReconnect());
        bot.setMessageDelay(config.getMessageDelay());
        bot.setVersion("FoxBot - A Java IRC bot written by TheReverend403 - https://github.com/TheReverend403/FoxBot");
        bot.setAutoSplitMessage(true);
        bot.setName(config.getBotNick());
        bot.setLogin(config.getBotIdent());
    }

    private void connectToServer()
    {
        try
        {
            if (config.getServerSsl())
            {
                bot.connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword(), config.getAcceptInvalidSsl() ? new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman() : SSLSocketFactory.getDefault());
            }
            else
            {
                bot.connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword());
            }

            if (config.useNickserv())
            {
                bot.identify(config.getNickservPassword());
            }
        }
        catch (IOException | IrcException ex)
        {
            ex.printStackTrace();
        }

        for (String channel : config.getChannels())
        {
            bot.joinChannel(channel);
        }
    }

    private void registerListeners()
    {
        bot.getListenerManager().addListener(new MessageListener(this));
        bot.getListenerManager().addListener(new UserListener(this));
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
                this.getCommandManager().registerCommand(command);
            }
        }
        catch (Exception ex)
        {
            // This can never happen.
            ex.printStackTrace();
        }
    }

    public void loadConfigFiles()
    {
        configFile = ConfigFactory.load(ConfigFactory.parseFile(new File("bot.conf")));
        permissionsFile = ConfigFactory.load(ConfigFactory.parseFile(new File("permissions.conf")));
        nickProtectionFile = ConfigFactory.load(ConfigFactory.parseFile(new File("nickprotection.conf")));
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

    public Config getNickProtectionFile()
    {
        return nickProtectionFile;
    }

    public Utils getUtils()
    {
        return utils;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public Database getDatabase()
    {
        return database;
    }
}