package uk.co.revthefox.foxbot.config;

import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.config.file.FileConfiguration;
import uk.co.revthefox.foxbot.config.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class BotConfig
{
    private File dataFolder = new File("config");
    private File file = null;
    private FileConfiguration newConfig = null;
    private File configFile = new File("config.yml");

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
    private long kickDelay;
    private boolean autoNickChange;
    private boolean autoReconnect;
    private Long messageDelay;
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
        saveResource("config.yml", false);
        saveResource("permissions.yml", false);
        saveResource("nickprotection.yml", false);

        try
        {
            file = new File(dataFolder + "/config.yml");
            botConfig.load(file);
            file = new File(dataFolder + "/permissions.yml");
            botPermissions.load(file);
            file = new File(dataFolder + "/nickprotection.yml");
            botNickProtection.load(file);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

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
        colourChar = botConfig.getString("misc.colourChar");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        kickDelay = botConfig.getLong("misc.kickDelay");
        autoNickChange = botConfig.getBoolean("misc.autoNickChange");
        autoReconnect = botConfig.getBoolean("misc.autoReconnect");
        messageDelay = botConfig.getLong("misc.messageDelay");
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
        greetingChannels = botConfig.getStringList("misc.channelsToGreet");
        greetingMessage = botConfig.getString("misc.greetingMessage");
        greetingNotice = botConfig.getBoolean("misc.sendGreetingAsNotice");

        unbanTimer = botConfig.getInt("bans.unbanTimer");
    }

    public void reload()
    {
        foxbot.loadConfigFiles();

        try
        {
            file = new File(dataFolder + "/config.yml");
            botConfig.load(file);
            file = new File(dataFolder + "/permissions.yml");
            botPermissions.load(file);
            file = new File(dataFolder + "/nickprotection.yml");
            botNickProtection.load(file);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        usersMustBeVerified = botConfig.getBoolean("auth.usersMustBeVerified");
        matchUsersByHostmask = botConfig.getBoolean("auth.matchUsersByHostmask");
        debug = botConfig.getBoolean("misc.debug");
        foxbot.setVerbose(botConfig.getBoolean("misc.debug"));
        commandPrefix = botConfig.getString("misc.commandPrefix");
        colourChar = botConfig.getString("misc.colourChar");
        autoJoinOnInvite = botConfig.getBoolean("misc.autoJoinOnInvite");
        kickDelay = botConfig.getLong("misc.kickDelay");
        messageDelay = botConfig.getLong("misc.messageDelay");
        foxbot.setMessageDelay(botConfig.getLong("misc.messageDelay"));
        mungeUsernames = botConfig.getBoolean("misc.mungeUsernames");
        greetingChannels = botConfig.getStringList("misc.channelsToGreet");
        greetingMessage = botConfig.getString("misc.greetingMessage");
        greetingNotice = botConfig.getBoolean("misc.sendGreetingAsNotice");
        unbanTimer = botConfig.getInt("bans.unbanTimer");
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

    public void saveDefaultConfig()
    {
        if (!configFile.exists())
        {
            saveResource("config.yml", false);
        }
    }

    public void saveResource(String resourcePath, boolean replace)
    {
        if (resourcePath == null || resourcePath.equals(""))
        {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);

        if (in == null)
        {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists())
        {
            outDir.mkdirs();
        }

        try
        {
            if (!outFile.exists() || replace)
            {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public InputStream getResource(String filename)
    {
        if (filename == null)
        {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try
        {
            URL url = getClassLoader().getResource(filename);

            if (url == null)
            {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public ClassLoader getClassLoader()
    {
        return FoxBot.class.getClassLoader();
    }

    public void reloadConfig()
    {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

            newConfig.setDefaults(defConfig);
        }
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