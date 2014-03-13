/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot;

import co.foxdev.foxbot.commands.Command;
import co.foxdev.foxbot.utils.CommandManager;
import co.foxdev.foxbot.config.Config;
import co.foxdev.foxbot.config.ZncConfig;
import co.foxdev.foxbot.database.Database;
import co.foxdev.foxbot.listeners.MessageListener;
import co.foxdev.foxbot.listeners.UserListener;
import co.foxdev.foxbot.logger.BotLogger;
import co.foxdev.foxbot.permissions.PermissionManager;
import com.maxmind.geoip.LookupService;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.reflections.Reflections;

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
 * @repo https://github.com/FoxDev/FoxBot
 */

public class FoxBot extends PircBotX
{
	private static FoxBot instance;
    private static Config config;
    private static PermissionManager permissions;
    private static ZncConfig zncConfig;
    private static CommandManager commandManager;
    private static Database database;
    private static LookupService lookupService;
    private static Reflections reflections = new Reflections("co.foxdev");

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {
	    instance = this;
        File path = new File("data/custcmds");

        if (!path.exists() && !path.mkdirs())
        {
            BotLogger.log(Level.WARNING, "STARTUP: Could not create required folders (data/custcmds/). Shutting down.");
            this.disconnect();
            return;
        }

        config = new Config(this);
        zncConfig = new ZncConfig(this);
        permissions = new PermissionManager(this);
        commandManager = new CommandManager(this);
        database = new Database(this);
        database.connect();

        try
        {
            lookupService = new LookupService(new File("data/GeoLiteCity.dat"), LookupService.GEOIP_STANDARD);
        }
        catch (IOException ex)
        {
            BotLogger.log(Level.INFO, "GeoIP database not found, this feature will be unavailable.");
        }

	    useShutdownHook(true);
	    registerListeners();
        registerCommands();
        setBotInfo();
        connectToServer();
    }

    private void setBotInfo()
    {
        BotLogger.log(Level.INFO, "STARTUP: Setting bot info");
        setVerbose(config.getDebug());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set verbose to %s", config.getDebug()));
        setAutoNickChange(config.getAutoNickChange());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set auto nick change to %s", config.getAutoNickChange()));
        setAutoReconnect(config.getAutoReconnect());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set auto-reconnect to %s", config.getAutoReconnect()));
        setMessageDelay(config.getMessageDelay());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set message delay to %s", config.getMessageDelay()));
        setVersion(String.format("FoxBot - A Java IRC bot written by FoxDev and owned by %s - https://github.com/FoxDev/FoxBot - Use %shelp for more info", config.getBotOwner(), config.getCommandPrefix()));
        BotLogger.log(Level.INFO, String.format("STARTUP: Set version to 'FoxBot - A Java IRC bot written by FoxDev and owned by %s - https://github.com/FoxDev/FoxBot - Use %shelp for more info'", config.getBotOwner(), config.getCommandPrefix()));
        setAutoSplitMessage(true);
        setName(config.getBotNick());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set nick to '%s'", config.getBotNick()));
        setLogin(config.getBotIdent());
        BotLogger.log(Level.INFO, String.format("STARTUP: Set ident to '%s'", config.getBotIdent()));
    }

    private void connectToServer()
    {
        try
        {
            if (config.getServerSsl())
            {
                BotLogger.log(Level.INFO, String.format("CONNECT: Trying address %s (SSL)", getConfig().getServerAddress()));
                connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword(), config.getAcceptInvalidSsl() ? new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman() : SSLSocketFactory.getDefault());
            }
            else
            {
                BotLogger.log(Level.INFO, String.format("CONNECT: Trying address %s", getConfig().getServerAddress()));
                connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword());
            }

            if (config.useNickserv())
            {
                identify(config.getNickservPassword());
            }
        }
        catch (IOException | IrcException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        BotLogger.log(Level.INFO, String.format("CONNECT: Connected to %s", getConfig().getServerAddress()));
        BotLogger.log(Level.INFO, String.format("STARTUP: Joining channels"));

        for (String channel : config.getChannels())
        {
	        if (channel.contains(":"))
	        {
		        joinChannel(channel, channel.split(":")[1]);
		        continue;
	        }
	        joinChannel(channel);
        }
    }

    private void registerListeners()
    {
        BotLogger.log(Level.INFO, String.format("STARTUP: Registering MessageListener"));
        getListenerManager().addListener(new MessageListener(this));
        BotLogger.log(Level.INFO, String.format("STARTUP: Registering UserListener"));
        getListenerManager().addListener(new UserListener(this));
    }

    private void registerCommands()
    {
        try
        {
            for (Class clazz : reflections.getSubTypesOf(Command.class))
            {
                ClassLoader.getSystemClassLoader().loadClass(clazz.getName());
                Constructor clazzConstructor = clazz.getConstructor(getClass());
                Command command = (Command) clazzConstructor.newInstance(this);

                BotLogger.log(Level.INFO, String.format("STARTUP: Registering command '%s'", command.getName()));
                commandManager.registerCommand(command);
            }
        }
        catch (Exception ex)
        {
            // This can never happen.
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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

    public ZncConfig getZncConfig()
    {
        return zncConfig;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public Database getDatabase()
    {
        return database;
    }

    public LookupService getLookupService()
    {
        return lookupService;
    }

	public static FoxBot getInstance()
	{
		return instance;
	}
}
