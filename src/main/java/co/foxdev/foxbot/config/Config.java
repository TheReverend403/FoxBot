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

package co.foxdev.foxbot.config;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.config.yamlconfig.file.FileConfiguration;
import co.foxdev.foxbot.config.yamlconfig.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config
{
    private final FoxBot foxbot;

    private File configFile = new File("config.yml");
    private File permsFile = new File("permissions.yml");

    private FileConfiguration botConfig;
    private FileConfiguration botPermissions;

    public Config(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        botConfig = new YamlConfiguration();
        botPermissions = new YamlConfiguration();
        loadConfig();
    }

    private void loadConfig()
    {
        botConfig.saveResource("config.yml", false);
        botConfig.saveResource("permissions.yml", false);

        try
        {
            botConfig.load(configFile);
            botPermissions.load(permsFile);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void reload()
    {
        loadConfig();
    }

    // ---------
    // Bot owner
    // ---------

    public String getBotOwner()
    {
        return botConfig.getString("bot-owner");
    }

    // -----------
    // Bot section
    // -----------

    public String getBotNick()
    {
        return botConfig.getString("bot.nick");
    }

    public String getBotIdent()
    {
        return botConfig.getString("bot.ident");
    }

    public String getBotRealName()
    {
        return botConfig.getString("bot.realname");
    }

    // --------------
    // Server section
    // --------------

    public String getServerAddress()
    {
        return botConfig.getString("server.address");
    }

    public int getServerPort()
    {
        return botConfig.getInt("server.port");
    }

    public boolean getServerSsl()
    {
        return botConfig.getBoolean("server.ssl");
    }

    public boolean getAcceptInvalidSsl()
    {
        return botConfig.getBoolean("server.accept-invalid-ssl-cert");
    }

    public String getServerPassword()
    {
        return botConfig.getString("server.password");
    }

    public List<String> getChannels()
    {
        return botConfig.getStringList("server.channels");
    }

    // ------------
    // Auth section
    // ------------

    public boolean useNickserv()
    {
        return botConfig.getBoolean("auth.use-nickserv");
    }

    public String getNickservPassword()
    {
        return botConfig.getString("auth.nickserv-password");
    }

    public boolean getUsersMustBeVerified()
    {
        return botConfig.getBoolean("auth.users-must-be-verified");
    }

    public boolean getMatchUsersByHostmask()
    {
        return botConfig.getBoolean("auth.match-users-by-hostmask");
    }

    // -----------------------
    // User-punishment section
    // -----------------------

    public int getUnbanTimer()
    {
        return botConfig.getInt("user-punishment.unban-timer");
    }

    // ------------
    // Misc section
    // ------------

    public char getCommandPrefix()
    {
        return botConfig.getString("misc.command-prefix").toCharArray()[0];
    }

    public boolean getAutoJoinOnInvite()
    {
        return botConfig.getBoolean("misc.auto-join-on-invite");
    }

    public boolean getAutoRejoinOnKick()
    {
        return botConfig.getBoolean("misc.auto-rejoin-on-kick");
    }

    public long getAutoRejoinDelay()
    {
        return botConfig.getLong("misc.auto-rejoin-delay");
    }

    public long getKickDelay()
    {
        return botConfig.getLong("misc.kick-delay");
    }

    public boolean getAutoNickChange()
    {
        return botConfig.getBoolean("misc.auto-nick-change");
    }

    public boolean getAutoReconnect()
    {
        return botConfig.getBoolean("misc.auto-reconnect");
    }

    public Long getMessageDelay()
    {
        return botConfig.getLong("misc.message-delay");
    }

    public boolean getMungeUsernames()
    {
        return botConfig.getBoolean("misc.munge-usernames");
    }

    public List<String> getIgnoredChannels()
    {
        return botConfig.getStringList("misc.ignored-channels");
    }

    public List<String> getGreetingChannels()
    {
        return botConfig.getStringList("misc.channels-to-greet");
    }

    public String getGreetingMessage()
    {
        return botConfig.getString("misc.greeting-message");
    }

    public boolean getGreetingNotice()
    {
        return botConfig.getBoolean("misc.send-greeting-as-notice");
    }

    // --------------
    // Sounds Section
    // --------------

    public String getSoundURL()
    {
        return botConfig.getString("sounds.sound-url");
    }

    public String getSoundExtension()
    {
        return botConfig.getString("sounds.sound-extension");
    }

    // ----
    // Help
    // ----

    public List<String> getHelpLines()
    {
        return botConfig.getStringList("help");
    }

    // ------------
    // File objects
    // ------------

    public FileConfiguration getBotPermissions()
    {
        return botPermissions;
    }
}