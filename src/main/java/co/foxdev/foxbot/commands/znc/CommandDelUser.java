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

public class CommandDelUser extends Command
{
    private final FoxBot foxbot;

    public CommandDelUser(FoxBot foxbot)
    {
        super("zncdeluser", "command.znc.deluser");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1)
        {
            String user = args[0];
            String controlPanel = "*controlpanel";

            foxbot.bot().sendIRC().message(controlPanel, String.format("deluser %s", user));
            sender.send().notice(String.format("%s has been deleted!", user));
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %szncdeluser <name>", foxbot.getConfig().getCommandPrefix()));
    }
}
