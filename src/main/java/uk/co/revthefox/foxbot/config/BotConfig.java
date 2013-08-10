package uk.co.revthefox.foxbot.config;

import uk.co.revthefox.foxbot.FoxBot;

import java.util.List;

public class BotConfig
{
    private FoxBot foxbot;

    private String botNick;
    private String botIdent;
    private String botRealName;

    private String serverAddress;
    private Integer serverPort;
    private Boolean serverSsl;
    private Boolean acceptInvalidSsl;
    private List<String> serverChannels;


    private Boolean useNickserv;
    private String nickservPassword;
    private Boolean usersMustBeVerified;

    private Boolean debug;
    private String commandPrefix;
    private Boolean autoNickChange;
    private Boolean autoReconnect;
    private Long messageDelay;


    public BotConfig(FoxBot foxBot)
    {
        this.foxbot = foxBot;
        loadConfig();
    }

    private void loadConfig()
    {
        botNick = foxbot.getConfigFile().getString("bot.nick");
        botIdent = foxbot.getConfigFile().getString("bot.ident");
        botRealName = foxbot.getConfigFile().getString("bot.realName");

        serverAddress = foxbot.getConfigFile().getString("server.address");
        serverPort = foxbot.getConfigFile().getInt("server.port");
        serverSsl = foxbot.getConfigFile().getBoolean("server.ssl");
        acceptInvalidSsl = foxbot.getConfigFile().getBoolean("server.acceptInvalidSslCert");
        serverChannels = foxbot.getConfigFile().getStringList("server.channels");


        useNickserv = foxbot.getConfigFile().getBoolean("auth.useNickserv");
        nickservPassword = foxbot.getConfigFile().getString("auth.nickservPassword");
        usersMustBeVerified = foxbot.getConfigFile().getBoolean("auth.usersMustBeVerified");

        debug = foxbot.getConfigFile().getBoolean("misc.debug");
        commandPrefix = foxbot.getConfigFile().getString("misc.commandPrefix");
        autoNickChange = foxbot.getConfigFile().getBoolean("misc.autoNickChange");
        autoReconnect = foxbot.getConfigFile().getBoolean("misc.autoReconnect");
        messageDelay = foxbot.getConfigFile().getLong("misc.messageDelay");

    }

    public void reload()
    {
        foxbot.loadConfigFiles();
        usersMustBeVerified = foxbot.getConfigFile().getBoolean("auth.usersMustBeVerified");
        debug = foxbot.getConfigFile().getBoolean("misc.debug");
        foxbot.getBot().setVerbose(foxbot.getConfigFile().getBoolean("misc.debug"));
        commandPrefix = foxbot.getConfigFile().getString("misc.commandPrefix");
        messageDelay = foxbot.getConfigFile().getLong("misc.messageDelay");
        foxbot.getBot().setMessageDelay(foxbot.getConfigFile().getLong("misc.messageDelay"));
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
}
