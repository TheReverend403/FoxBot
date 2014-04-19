/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.config;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.config.yamlconfig.file.FileConfiguration;
import co.foxdev.foxbot.config.yamlconfig.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ZncConfig
{
    private FoxBot foxbot;

    private File configFile = new File("znc.yml");
    private FileConfiguration zncConfig;

    private String nick;
    private String altNick;
    private String ident;
    private String quitMsg;
    private int bufferCount;
    private boolean denySetBindhost;
    private List<String> modules;
    public String defaultChanmodes;

    public ZncConfig(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        zncConfig = new YamlConfiguration();
        loadConfig();
    }

    private void loadConfig()
    {
        zncConfig.saveResource("znc.yml", false);

        try
        {
            zncConfig.load(configFile);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        nick = zncConfig.getString("users.nick");
        altNick = zncConfig.getString("users.alt-nick");
        ident = zncConfig.getString("users.ident");
        quitMsg = zncConfig.getString("users.quit-message");
        bufferCount = zncConfig.getInt("users.buffercount");
        denySetBindhost = zncConfig.getBoolean("users.deny-set-bindhost");
        modules = zncConfig.getStringList("users.modules");
        defaultChanmodes = zncConfig.getString("users.default-chan-modes");
    }

    public boolean networkExists(String network)
    {
        return zncConfig.contains("networks." + network);
    }

    // -------------
    // Users section
    // -------------

    public String getNick()
    {
        return nick;
    }

    public String getAltNick()
    {
        return altNick;
    }

    public String getIdent()
    {
        return ident;
    }

    public String getQuitMsg()
    {
        return quitMsg;
    }

    public int getBufferCount()
    {
        return bufferCount;
    }

    public boolean isDenySetBindhost()
    {
        return denySetBindhost;
    }

    public List<String> getModules()
    {
        return modules;
    }

    public String getDefaultChanmodes()
    {
        return defaultChanmodes;
    }

    // ---------------
    // Servers section
    // ---------------

    public List<String> getServers(String network)
    {
        return zncConfig.getStringList("networks." + network + ".servers");
    }

    public String getNetworkName(String network)
    {
        return zncConfig.getString("networks." + network + ".name");
    }

    public List<String> getChannels(String network)
    {
        return zncConfig.getStringList("networks." + network + ".channels");
    }

	protected void reload()
	{
		loadConfig();
	}
}
