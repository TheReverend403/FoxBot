package uk.co.revthefox.foxbot.config;

import lombok.Getter;
import lombok.Setter;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.config.yamlconfig.file.FileConfiguration;
import uk.co.revthefox.foxbot.config.yamlconfig.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config
{
    private File configFile = new File("config.yml");
    private File permsFile = new File("permissions.yml");
    private File nickProtectionFile = new File("nickprotection.yml");

    private FoxBot foxbot;
    private FileConfiguration botConfig;
    private FileConfiguration botPermissions;
    private FileConfiguration botNickProtection;

    // -----------
    // Bot section
    // -----------

    private String botNick;
    private String botIdent;
    private String botRealName;

    // --------------
    // Server section
    // --------------

    private String serverAddress;
    private int serverPort;
    private boolean serverSsl;
    private boolean acceptInvalidSsl;
    private String serverPassword;
    private List<String> serverChannels;

    // ------------
    // Auth section
    // ------------

    private boolean useNickserv;
    private String nickservPassword;
    private boolean usersMustBeVerified;
    private boolean matchUsersByHostmask;

    // ------------
    // Misc section
    // ------------

    private boolean debug;
    private String commandPrefix;
    private String colourChar;
    private boolean autoJoinOnInvite;
    private boolean autoRejoinOnKick;
    private long autoRejoinDelay;
    private long kickDelay;
    private boolean autoNickChange;
    private boolean autoReconnect;
    private long messageDelay;
    private boolean mungeUsernames;
    private List<String> greetingChannels;
    private String greetingMessage;
    private boolean greetingNotice;

    // -----------------------
    // User-punishment section
    // -----------------------

    private boolean punishUsersOnKick;
    private String punishmentKickReason;
    private int unbanTimer;

    public Config(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        botConfig = new YamlConfiguration();
        botPermissions = new YamlConfiguration();
        botNickProtection = new YamlConfiguration();
        loadConfig();
    }

    private void loadConfig()
    {
        botConfig.saveResource("config.yml", false);
        botConfig.saveResource("permissions.yml", false);
        botConfig.saveResource("nickprotection.yml", false);

        try
        {
            botConfig.load(configFile);
            botPermissions.load(permsFile);
            botNickProtection.load(nickProtectionFile);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        // -----------
        // Bot section
        // -----------

        botNick = botConfig.getString("bot.nick");
        botIdent = botConfig.getString("bot.ident");
        botRealName = botConfig.getString("bot.realname");

        // --------------
        // Server section
        // --------------

        serverAddress = botConfig.getString("server.address");
        serverPort = botConfig.getInt("server.port");
        serverSsl = botConfig.getBoolean("server.ssl");
        acceptInvalidSsl = botConfig.getBoolean("server.accept-invalid-ssl-cert");
        serverPassword = botConfig.getString("server.password");
        serverChannels = botConfig.getStringList("server.channels");

        // ------------
        // Auth section
        // ------------

        useNickserv = botConfig.getBoolean("auth.use-nickserv");
        nickservPassword = botConfig.getString("auth.nickserv-password");
        usersMustBeVerified = botConfig.getBoolean("auth.users-must-be-verified");
        matchUsersByHostmask = botConfig.getBoolean("auth.match-users-by-hostmask");

        // ------------
        // Misc section
        // ------------

        debug = botConfig.getBoolean("misc.debug");
        commandPrefix = botConfig.getString("misc.command-prefix");
        colourChar = botConfig.getString("misc.colour-char");
        autoJoinOnInvite = botConfig.getBoolean("misc.auto-join-on-invite");
        autoRejoinOnKick = botConfig.getBoolean("misc.auto-rejoin-on-kick");
        autoRejoinDelay = botConfig.getLong("misc.auto-rejoin-delay");
        kickDelay = botConfig.getLong("misc.kick-delay");
        autoNickChange = botConfig.getBoolean("misc.auto-nick-change");
        autoReconnect = botConfig.getBoolean("misc.auto-reconnect");
        messageDelay = botConfig.getLong("misc.message-delay");
        mungeUsernames = botConfig.getBoolean("misc.munge-usernames");
        greetingChannels = botConfig.getStringList("misc.channels-to-greet");
        greetingMessage = botConfig.getString("misc.greeting-message");
        greetingNotice = botConfig.getBoolean("misc.send-greeting-as-notice");

        // -----------------------
        // User-punishment section
        // -----------------------

        punishUsersOnKick = botConfig.getBoolean("user-punishment.punish-users-on-kick");
        punishmentKickReason = botConfig.getString("user-punishment.punishment-kick-reason");
        unbanTimer = botConfig.getInt("user-punishment.unban-timer");
    }

    public void reload()
    {
        loadConfig();

        foxbot.setVerbose(botConfig.getBoolean("misc.debug"));
        foxbot.setMessageDelay(botConfig.getLong("misc.message-delay"));
    }

    // -----------
    // Bot section
    // -----------

    public String getBotNick()
    {
        return botNick;
    }

    public String getBotIdent()
    {
        return botIdent;
    }

    public String getBotRealName()
    {
        return botRealName;
    }

    // --------------
    // Server section
    // --------------

    public String getServerAddress()
    {
        return serverAddress;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public boolean getServerSsl()
    {
        return serverSsl;
    }

    public boolean getAcceptInvalidSsl()
    {
        return acceptInvalidSsl;
    }

    public String getServerPassword()
    {
        return serverPassword;
    }

    public List<String> getChannels()
    {
        return serverChannels;
    }

    // ------------
    // Auth section
    // ------------

    public boolean useNickserv()
    {
        return useNickserv;
    }

    public String getNickservPassword()
    {
        return nickservPassword;
    }

    public boolean getUsersMustBeVerified()
    {
        return usersMustBeVerified;
    }

    public boolean getMatchUsersByHostmask()
    {
        return matchUsersByHostmask;
    }

    // ------------
    // Misc section
    // ------------

    public boolean getDebug()
    {
        return debug;
    }

    public String getCommandPrefix()
    {
        return commandPrefix;
    }

    public String getColourChar()
    {
        return colourChar;
    }

    public boolean getAutoJoinOnInvite()
    {
        return autoJoinOnInvite;
    }

    public boolean getAutoRejoinOnKick()
    {
        return autoRejoinOnKick;
    }

    public long getAutoRejoinDelay()
    {
        return autoRejoinDelay;
    }

    public long getKickDelay()
    {
        return kickDelay;
    }

    public boolean getAutoNickChange()
    {
        return autoNickChange;
    }

    public boolean getAutoReconnect()
    {
        return autoReconnect;
    }

    public Long getMessageDelay()
    {
        return messageDelay;
    }

    public boolean getMungeUsernames()
    {
        return mungeUsernames;
    }

    public List<String> getGreetingChannels()
    {
        return greetingChannels;
    }

    public String getGreetingMessage()
    {
        return greetingMessage;
    }

    public boolean getGreetingNotice()
    {
        return greetingNotice;
    }

    // -----------------------
    // User-punishment section
    // -----------------------

    public boolean getPunishUsersOnKick()
    {
        return punishUsersOnKick;
    }

    public String getPunishmentKickReason()
    {
        return punishmentKickReason;
    }

    public int getUnbanTimer()
    {
        return unbanTimer;
    }

    // ------------
    // File objects
    // ------------

    public FileConfiguration getBotConfig()
    {
        return botConfig;
    }

    public FileConfiguration getBotPermissions()
    {
        return botPermissions;
    }

    public FileConfiguration getBotNickProtection()
    {
        return botNickProtection;
    }
}