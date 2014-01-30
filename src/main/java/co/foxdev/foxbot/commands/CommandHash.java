package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommandHash extends Command
{
	private final FoxBot foxbot;

	public CommandHash(FoxBot foxbot)
	{
		super("hash", "command.hash");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(final MessageEvent event, final String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length > 1)
		{
			String hashType = args[0];
			MessageDigest digest = null;

			try
			{
				digest = MessageDigest.getInstance(hashType);
			}
			catch (NoSuchAlgorithmException ex)
			{
				channel.sendMessage(String.format("(%s) Invalid hash. Valid types are SHA-1, SHA-256 and MD5", Utils.munge(sender.getNick())));
				return;
			}

			StringBuilder stringToHash = new StringBuilder(args[1]);

			for (int i = 2; i < args.length; i++)
			{
				stringToHash.append(" ").append(args[i]);
			}

			digest.reset();
			channel.sendMessage(String.format("(%s) %s", Utils.munge(sender.getNick()), byteArrayToHexString(digest.digest(stringToHash.toString().getBytes()))));
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %shash <SHA-1|SHA-256|MD5> <text>", foxbot.getConfig().getCommandPrefix()));
	}

	private String byteArrayToHexString(byte[] b)
	{
		String result = "";

		for (byte aB : b)
		{
			result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}