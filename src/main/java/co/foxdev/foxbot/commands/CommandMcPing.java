package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.MinecraftPing;
import co.foxdev.foxbot.utils.MinecraftPingReply;
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
				mcping = new MinecraftPing().getPing(host, port);
			}
			catch (IOException ex)
			{
				channel.sendMessage(String.format("(%s) Looks like %s:%s isn't up. :<", sender.getNick(), host, port));
				return;
			}

			String motd = mcping.getMotd();
			String players = mcping.getOnlinePlayers() + "/" + mcping.getMaxPlayers();
			String version = mcping.getVersion();
			String finalPing = String.format("%s%s - %s - MC %s", motd, Colors.NORMAL, players, version);

			channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) %s", foxbot.getUtils().munge(sender.getNick()), finalPing), '\u00A7'));
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smcping <host> [port]", foxbot.getConfig().getCommandPrefix()));
	}
}