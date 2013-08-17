package uk.co.revthefox.foxbot.config;

import com.google.common.collect.Lists;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private Boolean autoJoinOnInvite;
    private Boolean autoNickChange;
    private Boolean autoReconnect;
    private Long messageDelay;
    private Boolean mungeUsernames;

    List<String> files = Lists.newArrayList("bot.conf", "permissions.conf");

    public BotConfig(FoxBot foxBot)
    {
        this.foxbot = foxBot;
        loadConfig();
    }

    public void loadDefaultConfig()
    {
        for (String file : files)
        {
            if (!new File(file).exists())
            {
                System.out.println(String.format("Generating default %s!", file));
                InputStream confInStream = this.getClass().getResourceAsStream("/" + file);

                OutputStream confOutStream;
                int readBytes;
                byte[] buffer = new byte[4096];
                try
                {
                    confOutStream = new FileOutputStream(new File(file));
                    while ((readBytes = confInStream.read(buffer)) > 0)
                    {
                        confOutStream.write(buffer, 0, readBytes);
                    }
                    confInStream.close();
                    confOutStream.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    foxbot.getBot().disconnect();
                }
            }
        }
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
        autoJoinOnInvite = foxbot.getConfigFile().getBoolean("misc.autoJoinOnInvite");
        autoNickChange = foxbot.getConfigFile().getBoolean("misc.autoNickChange");
        autoReconnect = foxbot.getConfigFile().getBoolean("misc.autoReconnect");
        messageDelay = foxbot.getConfigFile().getLong("misc.messageDelay");
        mungeUsernames = foxbot.getConfigFile().getBoolean("misc.mungeUsernames");

    }

    public void reload()
    {
        foxbot.loadConfigFiles();
        usersMustBeVerified = foxbot.getConfigFile().getBoolean("auth.usersMustBeVerified");
        debug = foxbot.getConfigFile().getBoolean("misc.debug");
        foxbot.getBot().setVerbose(foxbot.getConfigFile().getBoolean("misc.debug"));
        commandPrefix = foxbot.getConfigFile().getString("misc.commandPrefix");
        autoJoinOnInvite = foxbot.getConfigFile().getBoolean("misc.autoJoinOnInvite");
        messageDelay = foxbot.getConfigFile().getLong("misc.messageDelay");
        foxbot.getBot().setMessageDelay(foxbot.getConfigFile().getLong("misc.messageDelay"));
        mungeUsernames = foxbot.getConfigFile().getBoolean("misc.mungeUsernames");
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
