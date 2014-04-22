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

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandMessage extends Command
{
    private final FoxBot foxbot;

	/**
	 * Sends a private message to a specified user.
	 *
	 * Usage: .pm <user> <message>
	 */
    public CommandMessage(FoxBot foxbot)
    {
        super("pm", "command.message", "message");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length > 1)
        {
            User target = foxbot.getUser(args[0]);
            StringBuilder message = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                message.append(" ").append(args[arg]);
            }

            foxbot.sendMessage(target, Utils.colourise(message.toString()));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %spm <user> <message>", foxbot.getConfig().getCommandPrefix()));
    }
}