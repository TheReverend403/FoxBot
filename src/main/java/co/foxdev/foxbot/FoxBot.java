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

import co.foxdev.foxbot.logger.BotLogger;
import co.foxdev.foxbot.logger.LogLevel;
import co.foxdev.foxbot.utils.PingTask;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.managers.BackgroundListenerManager;
import org.reflections.Reflections;
import co.foxdev.foxbot.commands.Command;
import co.foxdev.foxbot.config.Config;
import co.foxdev.foxbot.database.Database;
import co.foxdev.foxbot.listeners.MessageListener;
import co.foxdev.foxbot.listeners.UserListener;
import co.foxdev.foxbot.permissions.PermissionManager;
import co.foxdev.foxbot.plugin.PluginManager;
import co.foxdev.foxbot.listeners.spamhandler.SpamHandler;
import co.foxdev.foxbot.utils.Utils;

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
    private static Reflections reflections = new Reflections("co.foxdev");
    private static BackgroundListenerManager blm = new BackgroundListenerManager();

    public static void main(String[] args)
    {
        FoxBot me = new FoxBot();
        me.start(args);
    }

    private void start(String[] args)
    {
        BotLogger.log(LogLevel.INFO, "STARTUP: FoxBot starting...");

        File path = new File("data/customcmds/");

        if (!path.exists() && !path.mkdirs())
        {
            BotLogger.log(LogLevel.WARNING, "STARTUP: Could not create required folders. Shutting down.");
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

        if (config.isStatusCheckEnabled())
        {
            BotLogger.log(LogLevel.INFO, "STARTUP: Scheduling new PingTask");
            new PingTask(this);
        }
    }

    private void setBotInfo()
    {
        BotLogger.log(LogLevel.INFO, "STARTUP: Setting bot info");
        this.setVerbose(config.getDebug());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set verbose to %s", config.getDebug()));
        this.setAutoNickChange(config.getAutoNickChange());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set auto nick change to %s", config.getAutoNickChange()));
        this.setAutoReconnect(config.getAutoReconnect());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set auto-reconnect to %s", config.getAutoReconnect()));
        this.setMessageDelay(config.getMessageDelay());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set message delay to %s", config.getMessageDelay()));
        this.setVersion("FoxBot - A Java IRC bot written by FoxDev - https://github.com/FoxDev/FoxBot");
        BotLogger.log(LogLevel.INFO, "STARTUP: Set version to 'FoxBot - A Java IRC bot written by FoxDev - https://github.com/FoxDev/FoxBot'");
        this.setAutoSplitMessage(true);
        this.setName(config.getBotNick());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set nick to '%s'", config.getBotNick()));
        this.setLogin(config.getBotIdent());
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Set ident to '%s'", config.getBotIdent()));
    }

    private void connectToServer()
    {
        try
        {
            if (config.getServerSsl())
            {

                BotLogger.log(LogLevel.INFO, String.format("CONNECT: Trying address %s (SSL)", this.getConfig().getServerAddress()));
                this.connect(config.getServerAddress(), config.getServerPort(), config.getServerPassword(), config.getAcceptInvalidSsl() ? new UtilSSLSocketFactory().trustAllCertificates().disableDiffieHellman() : SSLSocketFactory.getDefault());
            }
            else
            {
                BotLogger.log(LogLevel.INFO, String.format("CONNECT: Trying address %s", this.getConfig().getServerAddress()));
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

        BotLogger.log(LogLevel.INFO, String.format("CONNECT: Connected to %s", this.getConfig().getServerAddress()));
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Joining channels"));
        for (String channel : config.getChannels())
        {
            this.joinChannel(channel);
        }
    }

    private void registerListeners()
    {
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Registering MessageListener"));
        blm.addListener(new MessageListener(this), true);
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Registering UserListener"));
        blm.addListener(new UserListener(this), true);
        BotLogger.log(LogLevel.INFO, String.format("STARTUP: Registering SpamHandler"));
        blm.addListener(new SpamHandler(this), true);

        this.setListenerManager(blm);

        /*
        this.getListenerManager().addListener(new MessageListener(this));
        this.getListenerManager().addListener(new UserListener(this));
        this.getListenerManager().addListener(new SpamHandler(this));
        */
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

                BotLogger.log(LogLevel.INFO, String.format("STARTUP: Registering command '%s'", command.getName()));
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