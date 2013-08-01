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
    private List<String> serverChannels;

    private String serverUsername;
    private String serverPassword;

    private String nickservUsername;
    private String nickservPassword;
    private Boolean usersMustMatchHostmask;

    private Boolean debug;
    private Boolean autoNickChange;
    private Boolean autoReconnect;
    private String commandPrefix;


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
        serverChannels = foxbot.getConfigFile().getStringList("server.channels");

        usersMustMatchHostmask = foxbot.getConfigFile().getBoolean("auth.usersMustMatchHostmask");

        debug = foxbot.getConfigFile().getBoolean("misc.debug");
        commandPrefix = foxbot.getConfigFile().getString("misc.commandPrefix");
        autoNickChange = foxbot.getConfigFile().getBoolean("misc.autoNickChange");
        autoReconnect = foxbot.getConfigFile().getBoolean("misc.autoReconnect");
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

    public List<String> getChannels()
    {
        return serverChannels;
    }

    public Boolean getUsersMustMatchHostmask()
    {
        return usersMustMatchHostmask;
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

    public Boolean getDebug()
    {
        return debug;
    }
}
