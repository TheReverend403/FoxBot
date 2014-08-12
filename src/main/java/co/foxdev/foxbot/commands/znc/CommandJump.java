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

public class CommandJump extends Command
{
    private final FoxBot foxbot;

    public CommandJump(FoxBot foxbot)
    {
        super("zncjump", "command.znc.jump");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1 || args.length == 2)
        {
            String user = args[0];
            String network = args.length == 2 ? args[1] : foxbot.getZncConfig().getNetworkName("default");
            String controlPanel = "*controlpanel";

            foxbot.bot().sendIRC().message(controlPanel, String.format("reconnect %s %s", user, network));
            sender.send().notice(String.format("%s is reconnecting!", user));
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %szncjump <name> [network]", foxbot.getConfig().getCommandPrefix()));
    }
}
