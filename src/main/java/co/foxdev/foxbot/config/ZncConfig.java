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
        return zncConfig.getString("users.nick");
    }

    public String getAltNick()
    {
        return zncConfig.getString("users.alt-nick");
    }

    public String getIdent()
    {
        return zncConfig.getString("users.ident");
    }

    public String getQuitMsg()
    {
        return zncConfig.getString("users.quit-message");
    }

    public int getBufferCount()
    {
        return zncConfig.getInt("users.buffercount");
    }

    public boolean isDenySetBindhost()
    {
        return zncConfig.getBoolean("users.deny-set-bindhost");
    }

    public List<String> getModules()
    {
        return zncConfig.getStringList("users.modules");
    }

    public String getDefaultChanmodes()
    {
        return zncConfig.getString("users.default-chan-modes");
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
