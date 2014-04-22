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
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandCommands extends Command
{
	private final FoxBot foxbot;

	/**
	 * Displays a list of commands the bot currently has available.
	 *
	 * Usage: .commands
	 */
	public CommandCommands(FoxBot foxbot)
	{
		super("commands", "command.commands");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(final MessageEvent event, final String[] args)
	{
		User sender = event.getUser();

		StringBuilder sb = new StringBuilder();

		for (Command command : foxbot.getCommandManager().getCommands())
		{
			if (!sb.toString().contains(command.getName()))
			{
				sb.append(command.getName()).append(", ");
			}
		}

		String cmdList = sb.toString();
		foxbot.sendNotice(sender, cmdList.substring(0, cmdList.lastIndexOf(",")));
	}
}