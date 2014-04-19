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
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.*;

public class CommandSystem extends Command
{
	private final FoxBot foxbot;
	private final Runtime runtime;

	public CommandSystem(FoxBot foxbot)
	{
		super("system", "command.system", "sys");
		this.foxbot = foxbot;
		runtime = Runtime.getRuntime();
	}

	@Override
	public void execute(MessageEvent event, String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length > 0)
		{
			StringBuilder command = new StringBuilder();
			boolean verbose = args[0].equals("-v");

			for (int i = verbose ? 1 : 0; i < args.length; i++)
			{
				command.append(args[i]).append(" ");
			}

			try
			{
				Process proc = runtime.exec(command.toString());
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

				// Prevent spam on long results
				int count = 0;
				String line;

				while ((line = stdInput.readLine()) != null)
				{
					if (!verbose && count >= 3)
					{
						channel.send().message("Max output reached. Use -v to show full output.");
						break;
					}

					if (!line.isEmpty())
					{
						channel.send().message(line);
						count++;
					}
				}
				stdInput.close();

				while ((line = stdError.readLine()) != null)
				{
					if (!verbose && count >= 3)
					{
						channel.send().message("Max output reached. Use -v to show full output.");
						break;
					}

					if (!line.isEmpty())
					{
						channel.send().message(line);
						count++;
					}
				}
				stdError.close();
				proc.destroy();
			}
			catch (IOException ex)
			{
				foxbot.log(ex);
			}
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %ssystem [-v] <command>", foxbot.getConfig().getCommandPrefix()));
	}
}
