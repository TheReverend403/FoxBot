package uk.co.revthefox.foxbot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import uk.co.revthefox.foxbot.commands.*;
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
    private Utils utils;
    private CommandManager commandManager;

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
        /*
        for (Class clazz : reflections.getSubTypesOf(Command.class))
        {
            try
            {
                this.getCommandManager().registerCommand((Command) clazz.getConstructor().newInstance(FoxBot.class));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        */

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
