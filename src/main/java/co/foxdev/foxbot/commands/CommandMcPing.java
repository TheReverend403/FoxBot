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
import co.foxdev.foxbot.utils.minecraft.*;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.*;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandMcPing extends Command
{
	private final FoxBot foxbot;

	public CommandMcPing(FoxBot foxbot)
	{
		super("mcping", "command.mcping");
		this.foxbot = foxbot;
	}

	public void execute(final MessageEvent event, final String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length > 0 && args.length <= 2)
		{
			String host = args[0];
			int port = 25565;

			if (args.length == 2 && StringUtils.isNumeric(args[1]))
			{
				port = Integer.parseInt(args[1]);
			}

			MinecraftPingReply mcping;

			try
			{
				MinecraftPingOptions mcpo = new MinecraftPingOptions().setHostname(host).setPort(port).setTimeout(500);
				mcping = new MinecraftPing().getPing(mcpo);
			}
			catch (IOException ex)
			{
				foxbot.log(ex);
				channel.send().message(String.format("(%s) Looks like %s:%s isn't up.", Utils.munge(sender.getNick()), host, port));
				return;
			}

			String motd = mcping.getDescription().replace("\n", " ");
			String players = mcping.getPlayers().getOnline() + "/" + mcping.getPlayers().getMax();
			String version = mcping.getVersion().getName();
			String finalPing = String.format("%s%s - %s - %s", motd, Colors.NORMAL, players, version);

			channel.send().message(Utils.colourise(String.format("(%s) %s", Utils.munge(sender.getNick()), finalPing), '\u00A7'));
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smcping <host> [port]", foxbot.getConfig().getCommandPrefix()));
	}
}