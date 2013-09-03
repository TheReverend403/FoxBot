package uk.co.revthefox.foxbot.config;

import com.typesafe.config.Config;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.List;

public class BotConfig
{
    private FoxBot foxbot;
    private Config botConfig;

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
    private boolean autoJoinOnInvite;
    private boolean autoNickChange;
    private boolean autoReconnect;
    private Long messageDelay;
    private boolean mungeUsernames;
    private List<String> greetingChannels;
    private String greetingMessage;
    private boolean greetingNotice;

    public BotConfig(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        botConfig = foxbot.getConfigFile();
        loadConfig();
    }

    private void loadConfig()
    {
        botNick = botConfig.getString("bot.nick");
        botIdent = botConfig.getString("bot.ident");
        botRealName = botConfig.getString("bot.realName");

        serverAddress = botConfig.getString("server.address");
        serverPort = botConfig.getInt("server.port");
        serverSsl = botConfig.getBoolean("server.ssl");
        acceptInvalidSsl = botConfig.getBoolean("server.acceptInvalidSslCert");
        serverPassword = botConfig.getString("server.password");
        serverChannels = botConfig.getStringList("server.channels");


        useNickserv = botConfig.getBoolean("auth.useNickserv");
        nickservPassword = botConfig.getString("auth.nickservPassword");
        usersMustBeVerified = botConfig.getBoolean("auth.usersMustBeVerified");
        matchUsersByHostmask = botConfig.getBoolean("auth.matchUsersByHostmask");

        debug = botConfig.getBoolean("misc.debug");
        commandPrefix = botConfig.getString("misc.commandPrefix");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        autoNickChange = botConfig.getBoolean("misc.autoNickChange");
        autoReconnect = botConfig.getBoolean("misc.autoReconnect");
        messageDelay = botConfig.getLong("misc.messageDelay");
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
        greetingChannels = botConfig.getStringList("misc.channelsToGreet");
        greetingMessage = botConfig.getString("misc.greetingMessage");
        greetingNotice = botConfig.getBoolean("misc.sendGreetingAsNotice");
    }

    public void reload()
    {
        foxbot.loadConfigFiles();
        botConfig = foxbot.getConfigFile();
        usersMustBeVerified = botConfig.getBoolean("auth.usersMustBeVerified");
        matchUsersByHostmask = botConfig.getBoolean("auth.matchUsersByHostmask");
        debug = botConfig.getBoolean("misc.debug");
        foxbot.getBot().setVerbose(botConfig.getBoolean("misc.debug"));
        commandPrefix = botConfig.getString("misc.commandPrefix");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        messageDelay = botConfig.getLong("misc.messageDelay");
        foxbot.getBot().setMessageDelay(botConfig.getLong("misc.messageDelay"));
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
        greetingChannels = botConfig.getStringList("misc.channelsToGreet");
        greetingMessage = botConfig.getString("misc.greetingMessage");
        greetingNotice = botConfig.getBoolean("misc.sendGreetingAsNotice");
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

    public boolean getAutoJoinOnInvite()
    {
        return autoJoinOnInvite;
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
}
