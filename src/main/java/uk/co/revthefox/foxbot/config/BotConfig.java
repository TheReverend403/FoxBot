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
    private Integer serverPort;
    private Boolean serverSsl;
    private Boolean acceptInvalidSsl;
    private String serverPassword;
    private List<String> serverChannels;


    private Boolean useNickserv;
    private String nickservPassword;
    private Boolean usersMustBeVerified;

    private Boolean debug;
    private String commandPrefix;
    private Boolean autoJoinOnInvite;
    private Boolean autoNickChange;
    private Boolean autoReconnect;
    private Long messageDelay;
    private Boolean mungeUsernames;

    public BotConfig(FoxBot foxBot)
    {
        this.foxbot = foxBot;
        botConfig = foxBot.getConfigFile();
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

        debug = botConfig.getBoolean("misc.debug");
        commandPrefix = botConfig.getString("misc.commandPrefix");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        autoNickChange = botConfig.getBoolean("misc.autoNickChange");
        autoReconnect = botConfig.getBoolean("misc.autoReconnect");
        messageDelay = botConfig.getLong("misc.messageDelay");
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
    }

    public void reload()
    {
        foxbot.loadConfigFiles();
        usersMustBeVerified = botConfig.getBoolean("auth.usersMustBeVerified");
        debug = botConfig.getBoolean("misc.debug");
        foxbot.getBot().setVerbose(botConfig.getBoolean("misc.debug"));
        commandPrefix = botConfig.getString("misc.commandPrefix");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        messageDelay = botConfig.getLong("misc.messageDelay");
        foxbot.getBot().setMessageDelay(botConfig.getLong("misc.messageDelay"));
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
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

    public Integer getServerPort()
    {
        return serverPort;
    }

    public Boolean getServerSsl()
    {
        return serverSsl;
    }

    public Boolean getAcceptInvalidSsl()
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

    public Boolean useNickserv()
    {
        return useNickserv;
    }

    public String getNickservPassword()
    {
        return nickservPassword;
    }

    public Boolean getUsersMustBeVerified()
    {
        return usersMustBeVerified;
    }

    public Boolean getDebug()
    {
        return debug;
    }

    public String getCommandPrefix()
    {
        return commandPrefix;
    }

    public Boolean getAutoJoinOnInvite()
    {
        return autoJoinOnInvite;
    }

    public Boolean getAutoNickChange()
    {
        return autoNickChange;
    }

    public Boolean getAutoReconnect()
    {
        return autoReconnect;
    }

    public Long getMessageDelay()
    {
        return messageDelay;
    }

    public Boolean getMungeUsernames()
    {
        return mungeUsernames;
    }
}
