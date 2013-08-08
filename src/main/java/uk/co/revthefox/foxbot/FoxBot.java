package uk.co.revthefox.foxbot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import org.reflections.Reflections;
import uk.co.revthefox.foxbot.commands.Command;
import uk.co.revthefox.foxbot.config.BotConfig;
import uk.co.revthefox.foxbot.listeners.MessageListener;
import uk.co.revthefox.foxbot.permissions.PermissionManager;

import java.io.*;
import java.lang.reflect.Constructor;

public class FoxBot
{

    private PircBotX bot;
    private Config configFile;
    private Config permissionsFile;
    private BotConfig config;
    private PermissionManager permissions;
    private Utils utils;
    private CommandManager commandManager;

    Reflections reflections = new Reflections("uk.co.revthefox");


    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {

        if (!new File("bot.conf").exists())
        {
            System.out.println("Generating default config!");
            InputStream confInStream = this.getClass().getResourceAsStream("/bot.conf");

            OutputStream confOutStream;
            int readBytes;
            byte[] buffer = new byte[4096];
            try
            {
                confOutStream = new FileOutputStream(new File("bot.conf"));
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
        if (!new File("permissions.conf").exists())
        {
            System.out.println("Generating default permissions!");
            InputStream permsInStream = this.getClass().getResourceAsStream("/permissions.conf");

            OutputStream permsOutStream;
            int readBytes;
            byte[] buffer = new byte[4096];
            try
            {
                permsOutStream = new FileOutputStream(new File("permissions.conf"));
                while ((readBytes = permsInStream.read(buffer)) > 0)
                {
                    permsOutStream.write(buffer, 0, readBytes);
                }
                permsInStream.close();
                permsOutStream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                bot.disconnect();
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
            bot.connect(config.getServerAddress(), config.getServerPort());

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
                Command c = (Command) clazzConstructor.newInstance(this);
                this.getCommandManager().registerCommand(c);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        /*
        this.getCommandManager().registerCommand(new CommandInsult(this));
        this.getCommandManager().registerCommand(new CommandKick(this));
        this.getCommandManager().registerCommand(new CommandBan(this));
        this.getCommandManager().registerCommand(new CommandKill(this));
        this.getCommandManager().registerCommand(new CommandPing(this));
        this.getCommandManager().registerCommand(new CommandDelay(this));
        this.getCommandManager().registerCommand(new CommandSay(this));
        this.getCommandManager().registerCommand(new CommandJoin(this));
        this.getCommandManager().registerCommand(new CommandPart(this));
        this.getCommandManager().registerCommand(new CommandUptime(this));
        this.getCommandManager().registerCommand(new CommandAction(this));
        this.getCommandManager().registerCommand(new CommandReload(this));
        */
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
