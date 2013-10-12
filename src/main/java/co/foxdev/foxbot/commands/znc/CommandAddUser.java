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

package co.foxdev.foxbot.commands.znc;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.commands.Command;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddUser extends Command
{
    private final FoxBot foxbot;

    public CommandAddUser(FoxBot foxbot)
    {
        super("zncadduser", "command.znc.adduser");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 3)
        {
            String user = args[0];
            String password = RandomStringUtils.randomAlphanumeric(6);
            String bindhost = args[1];
            String network = foxbot.getZncConfig().networkExists(args[2]) ? args[2] : "default";
            String networkName = foxbot.getZncConfig().getNetworkName(network);

            // ---------------
            // Basic user info
            // ---------------

            foxbot.sendMessage("*controlpanel", String.format("adduser %s %s", user, password));
            foxbot.sendMessage("*controlpanel", String.format("set nick %s %s", user, foxbot.getZncConfig().getNick().replace("{NAME}", user)));
            foxbot.sendMessage("*controlpanel", String.format("set altnick %s %s", user, foxbot.getZncConfig().getAltNick().replace("{NAME}", user)));
            foxbot.sendMessage("*controlpanel", String.format("set ident %s %s", user, foxbot.getZncConfig().getIdent().replace("{NAME}", user.toLowerCase())));
            foxbot.sendMessage("*controlpanel", String.format("set bindhost %s %s", user, bindhost));
            foxbot.sendMessage("*controlpanel", String.format("set quitmsg %s %s", user,foxbot.getZncConfig().getQuitMsg()));
            foxbot.sendMessage("*controlpanel", String.format("set buffercount %s %s", user, foxbot.getZncConfig().getBufferCount()));
            foxbot.sendMessage("*controlpanel", String.format("set denysetbindhost %s %s", user, foxbot.getZncConfig().isDenySetBindhost()));
            foxbot.sendMessage("*controlpanel", String.format("set prependtimestamp %s true", user));

            // -----------
            // Add servers
            // -----------

            foxbot.sendMessage("*controlpanel", String.format("addnetwork %s %s", user, networkName));

            for (String server : foxbot.getZncConfig().getServers(network))
            {
                String host = server.split(":")[0];
                String port = server.split(":")[1];

                foxbot.sendMessage("*controlpanel", String.format("addserver %s %s %s %s", user, networkName, host, port));
            }

            // ------------
            // Load modules
            // ------------

            for (String module : foxbot.getZncConfig().getModules())
            {
                foxbot.sendMessage("*controlpanel", String.format("loadmodule %s %s", user, module));
            }

            // ---------------------------------------
            // Send information to the adding user
            // ---------------------------------------

            foxbot.sendNotice(sender, String.format("User added! Send this info to the user - Username: %s - Password: %s", user, password));

            // ------------
            // Add channels
            // ------------

            // Give the account chance to connect
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }

            for (String channel : foxbot.getZncConfig().getChannels(network))
            {
                foxbot.sendMessage("*send_raw", String.format("server %s %s JOIN %s", user, networkName, channel));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncadduser <name> <bindhost> <Esper|Seion>", foxbot.getConfig().getCommandPrefix()));
    }
}
