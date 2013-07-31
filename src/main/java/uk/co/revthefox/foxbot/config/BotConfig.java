package uk.co.revthefox.foxbot.config;

import uk.co.revthefox.foxbot.FoxBot;

public class BotConfig
{
    private FoxBot foxbot;

    private boolean debugMode;

    private String botNick;
    private String botIdent;
    private String botRealName;

    private String commandPrefix;

    private String serverHost;
    private String serverPort;
    private String serverUsername;
    private String serverPassword;

    private String nickservUsername;
    private String nickservPassword;


    public BotConfig(FoxBot foxBot)
    {
        this.foxbot = foxBot;
    }

    private void loadConfig()
    {
        debugMode = foxbot.getConfigFile().getBoolean("misc.debug");
    }

    public String getCommandPrefix()
    {
        return commandPrefix;
    }
}
