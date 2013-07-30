package uk.co.revthefox.foxbot.config;

import uk.co.revthefox.foxbot.FoxBot;

public class Config
{
    private FoxBot foxbot;

    private boolean debugMode;

    private String botName;
    private String botIdent;
    private String botRealName;

    private String commandPrefix;

    private String serverHost;
    private String serverPort;
    private String serverUsername;
    private String serverPassword;

    private String nickservUsername;
    private String nickservPassword;


    public Config(FoxBot foxBot)
    {
        this.foxbot = foxBot;
    }

    private void loadConfig()
    {

    }

    public String getCommandPrefix()
    {
        return commandPrefix;
    }
}
