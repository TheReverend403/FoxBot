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
import co.foxdev.foxbot.config.Config;
import co.foxdev.foxbot.config.ZncConfig;
import co.foxdev.foxbot.database.Database;
import co.foxdev.foxbot.listeners.MessageListener;
import co.foxdev.foxbot.listeners.UserListener;
import co.foxdev.foxbot.permissions.PermissionManager;
import co.foxdev.foxbot.utils.CommandManager;
import com.maxmind.geoip.LookupService;
import lombok.Getter;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.impl.SimpleLogger;
import org.slf4j.impl.SimpleLoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;

/**
 * FoxBot - An IRC bot written in Java
 *
 * @author TheReverend403 (Lee Watson)
 * @website http://revthefox.co.uk
 * @repo https://github.com/FoxDev/FoxBot
 */

public class FoxBot extends PircBotX
{
	@Getter
	private static FoxBot instance;
	@Getter
	private Config config;
	@Getter
	private Logger logger;
	@Getter
	private PermissionManager permissionManager;
	@Getter
	private ZncConfig zncConfig;
	@Getter
	private CommandManager commandManager;
	@Getter
	private Database database;
	@Getter
	private LookupService lookupService;
	@Getter
	private Reflections reflections = new Reflections("co.foxdev");

	public static void main(String[] args)
	{
		System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
		System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "false");
		System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
		System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "[HH:MM:ss]");
		System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");

		FoxBot me = new FoxBot();
		me.start(args);
	}

	private void start(String[] args)
	{
		instance = this;
		logger = new SimpleLoggerFactory().getLogger(getClass().getName());
		File path = new File("data/custcmds");

		if (!path.exists() && !path.mkdirs())
		{
			log("STARTUP: Could not create required folders (data/custcmds/). Shutting down.");
			disconnect();
			return;
		}

		config = new Config(this);
		zncConfig = new ZncConfig(this);
		permissionManager = new PermissionManager(this);
		commandManager = new CommandManager(this);
		database = new Database(this);
		database.connect();

		try
		{
			lookupService = new LookupService(new File("data/GeoLiteCity.dat"), LookupService.GEOIP_STANDARD);
		}
		catch (IOException ex)
		{
			log("GeoIP database not found, geoip feature will be unavailable.");
		}

		useShutdownHook(true);
		registerListeners();
		registerCommands();
		setBotInfo();
		connectToServer();
	}

	private void setBotInfo()
	{
		log("Setting bot info");
		setVerbose(config.getDebug());
		log(String.format("Set verbose to %s", config.getDebug()));
		setAutoNickChange(config.getAutoNickChange());
		log(String.format("Set auto nick change to %s", config.getAutoNickChange()));
		setAutoReconnect(config.getAutoReconnect());
		log(String.format("Set auto-reconnect to %s", config.getAutoReconnect()));
		setMessageDelay(config.getMessageDelay());
		log(String.format("Set message delay to %s", config.getMessageDelay()));
		setVersion(String.format("FoxBot - A Java IRC bot written by FoxDev and owned by %s - http://foxbot.foxdev.co - Use %shelp for more info", config.getBotOwner(), config.getCommandPrefix()));
		log(String.format("Set version to 'FoxBot - A Java IRC bot written by FoxDev and owned by %s - https://github.com/FoxDev/FoxBot - Use %shelp for more info'", config.getBotOwner(), config.getCommandPrefix()));
		setAutoSplitMessage(true);
		setName(config.getBotNick());
		log(String.format("Set nick to '%s'", config.getBotNick()));
		setLogin(config.getBotIdent());
		log(String.format("Set ident to '%s'", config.getBotIdent()));
	}

	private void connectToServer()
	{
		try
		{
			if (config.getServerSsl())
			{
				log(String.format("Trying address %s (SSL)", getConfig().getServerAddress()));
				connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword(), config.getAcceptInvalidSsl() ? new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman() : SSLSocketFactory.getDefault());
			}
			else
			{
				log(String.format("Trying address %s", getConfig().getServerAddress()));
				connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword());
			}

			if (config.useNickserv())
			{
				identify(config.getNickservPassword());
			}
		}
		catch (IOException | IrcException ex)
		{
			log(ex);
		}

		log(String.format("Connected to %s", getConfig().getServerAddress()));
		log(String.format("Joining channels"));

		for (String channel : config.getChannels())
		{
			if (channel.contains(":"))
			{
				String[] parts = channel.split(":");

				joinChannel(parts[0], parts[1]);
				continue;
			}
			joinChannel(channel);
		}
	}

	private void registerListeners()
	{
		log(String.format("Registering MessageListener"));
		getListenerManager().addListener(new MessageListener(this));
		log(String.format("Registering UserListener"));
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

				log(String.format("Registering command '%s'", command.getName()));
				commandManager.registerCommand(command);
			}
		}
		catch (Exception ex)
		{
			log(ex);
		}
	}

	@Override
	public void log(String line)
	{
		logger.info(line);
	}

	public void log(Exception ex)
	{
		for (StackTraceElement element : ex.getStackTrace())
		{
			log(Level.SEVERE, ex.getLocalizedMessage());
			log(Level.SEVERE, element.toString());
		}
	}

	public void log(Level level, String line)
	{
		if (level == Level.INFO)
		{
			logger.info(line);
		}

		if (level == Level.WARNING)
		{
			logger.warn(line);
		}

		if (level == Level.SEVERE)
		{
			logger.error(line);
		}

		if (level == Level.FINE)
		{
			logger.debug(line);
		}
	}
}
