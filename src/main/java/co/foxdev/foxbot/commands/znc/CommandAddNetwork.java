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

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.commands.Command;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddNetwork extends Command
{
    private final FoxBot foxbot;

    public CommandAddNetwork(FoxBot foxbot)
    {
        super("zncaddnetwork", "command.znc.addnetwork");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 2)
        {
            String user = args[0];
            String network = foxbot.getZncConfig().networkExists(args[1]) ? args[1] : "default";
            String networkName = foxbot.getZncConfig().getNetworkName(network);

            foxbot.sendMessage("*controlpanel", String.format("addnetwork %s %s", user, networkName));

            for (String server : foxbot.getZncConfig().getServers(network))
            {
                String host = server.split(":")[0];
                String port = server.split(":")[1];

                foxbot.sendMessage("*controlpanel", String.format("addserver %s %s %s %s", user, networkName, host, port));
            }
            // Send a message to the partyline user
            foxbot.sendMessage("?" + user, String.format("The network '%s' has been added to your account!", networkName));

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
	            foxbot.sendNotice(sender, String.format("Network '%s' added to %s's account!", networkName, user));
                foxbot.sendMessage("*send_raw", String.format("server %s %s JOIN %s", user, networkName, channel));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncaddnetwork <name> <network>", foxbot.getConfig().getCommandPrefix()));
    }
}
