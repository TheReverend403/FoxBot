package uk.co.revthefox.foxbot;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import org.reflections.Reflections;
import uk.co.revthefox.foxbot.commands.Command;
import uk.co.revthefox.foxbot.config.BotConfig;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoxBot
{

    private PircBotX bot;
    private Config configFile;
    private Config permissionsFile;
    private BotConfig config;
    private PermissionManager permissions;
    private Utils utils;
    private CommandManager commandManager;

    private Reflections reflections = new Reflections("uk.co.revthefox");

    List<String> files = Lists.newArrayList("bot.conf", "permissions.conf");

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {

        for (String file : files)
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

        loadConfigFiles();
        bot = new PircBotX();
        config = new BotConfig(this);
        permissions = new PermissionManager(this);
        utils = new Utils(this);
        commandManager = new CommandManager(this);
        registerListeners();
        registerCommands();
        setBotInfo();
        connectToServer();
        joinChannels();
    }

    private void setBotInfo()
    {
        bot.setVerbose(config.getDebug());
        bot.setAutoNickChange(config.getAutoNickChange());
        bot.setAutoReconnect(config.getAutoReconnect());
        bot.setMessageDelay(config.getMessageDelay());
        bot.setVersion("FoxBot - A Java IRC bot written by TheReverend403 - https://github.com/TheReverend403/FoxBot");
        bot.setName(config.getBotNick());
        bot.setLogin(config.getBotIdent());
    }

    private void connectToServer()
    {
        try
        {
            if (!config.getServerSsl())
            {
                bot.connect(config.getServerAddress(), config.getServerPort());
            }
            else if (config.getAcceptInvalidSsl())
            {
                bot.connect(config.getServerAddress(), config.getServerPort(), new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman());
            }
            else
            {
                bot.connect(config.getServerAddress(), config.getServerPort(), SSLSocketFactory.getDefault());
            }
            if (config.useNickserv())
            {
                bot.identify(config.getNickservPassword());
            }
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

    private void registerCommands()
    {
        try
        {
            for (Class clazz : reflections.getSubTypesOf(Command.class))
            {
                ClassLoader.getSystemClassLoader().loadClass(clazz.getName());
                Constructor clazzConstructor = clazz.getConstructor(FoxBot.class);
                Command command = (Command) clazzConstructor.newInstance(this);
                this.getCommandManager().registerCommand(command);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadConfigFiles()
    {
        configFile = ConfigFactory.load(ConfigFactory.parseFile(new File("bot.conf")));
        permissionsFile = ConfigFactory.load(ConfigFactory.parseFile(new File("permissions.conf")));
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

    public Utils getUtils()
    {
        return utils;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }
}
