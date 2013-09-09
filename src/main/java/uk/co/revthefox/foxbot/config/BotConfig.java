package uk.co.revthefox.foxbot.config;

import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.config.yamlconfig.file.FileConfiguration;
import uk.co.revthefox.foxbot.config.yamlconfig.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class BotConfig
{
    private FileConfiguration newConfig = null;
    private File configFile = new File("config.yml");
    private File permsFile = new File("permissions.yml");
    private File nickProtectionFile = new File("nickprotection.yml");

    private FoxBot foxbot;
    private FileConfiguration botConfig;
    private FileConfiguration botPermissions;
    private FileConfiguration botNickProtection;

    private String botNick;
    private String botIdent;
    private String botRealName;

    private String serverAddress;
    private int serverPort;
    private boolean serverSsl;
    private boolean acceptInvalidSsl;
    private String serverPassword;
    private List<String> serverChannels;


    private boolean useNickserv;
    private String nickservPassword;
    private boolean usersMustBeVerified;
    private boolean matchUsersByHostmask;

    private boolean debug;
    private String commandPrefix;
    private String colourChar;
    private boolean autoJoinOnInvite;
    private boolean punishUsersOnKick;
    private String punishmentKickReason;
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

    private int unbanTimer;

    public BotConfig(FoxBot foxbot)
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

        botNick = botConfig.getString("bot.nick");
        botIdent = botConfig.getString("bot.ident");
        botRealName = botConfig.getString("bot.realname");

        serverAddress = botConfig.getString("server.address");
        serverPort = botConfig.getInt("server.port");
        serverSsl = botConfig.getBoolean("server.ssl");
        acceptInvalidSsl = botConfig.getBoolean("server.accept-invalid-ssl-cert");
        serverPassword = botConfig.getString("server.password");
        serverChannels = botConfig.getStringList("server.channels");

        useNickserv = botConfig.getBoolean("auth.use-nickserv");
        nickservPassword = botConfig.getString("auth.nickserv-password");
        usersMustBeVerified = botConfig.getBoolean("auth.users-must-be-verified");
        matchUsersByHostmask = botConfig.getBoolean("auth.match-users-by-hostmask");

        debug = botConfig.getBoolean("misc.debug");
        commandPrefix = botConfig.getString("misc.command-prefix");
        colourChar = botConfig.getString("misc.colour-char");
        autoJoinOnInvite = botConfig.getBoolean("misc.auto-join-on-invite");
        punishUsersOnKick = botConfig.getBoolean("misc.punish-users-on-kick");
        punishmentKickReason = botConfig.getString("misc.punishment-kick-reason");
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

        unbanTimer = botConfig.getInt("bans.unban-timer");
    }

    public void reload()
    {
        loadConfig();

        foxbot.setVerbose(botConfig.getBoolean("misc.debug"));
        foxbot.setMessageDelay(botConfig.getLong("misc.message-delay"));
    }

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

    public boolean getPunishUsersOnKick()
    {
        return punishUsersOnKick;
    }

    public String getPunishmentKickReason()
    {
        return punishmentKickReason;
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

    public int getUnbanTimer()
    {
        return unbanTimer;
    }

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