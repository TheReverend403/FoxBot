package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.*;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.*;
import java.net.*;

public class CommandShorten extends Command
{
	private final FoxBot foxbot;

	public CommandShorten(FoxBot foxbot)
	{
		super("shorten", "command.shorten");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(final MessageEvent event, final String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length == 1)
		{
			String linkToShorten = args[0];

			channel.sendMessage(String.format("(%s) %s", Utils.munge(sender.getNick()), shorten(linkToShorten)));
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sshorten <link>", foxbot.getConfig().getCommandPrefix()));
	}

	private String shorten(String longUrl)
	{
		if (longUrl == null)
		{
			return Colors.RED + "Invalid link";
		}

		StringBuilder sb;
		String line;

		try
		{
			URL url = new URL("https://www.googleapis.com/urlshortener/v1/url");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "toolbar");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(String.format("{\"longUrl\": \"%s\"}", URLEncoder.encode(longUrl, "UTF-8")));
			writer.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null)
			{
				sb.append(line).append('\n');
			}

			String json = sb.toString();

			return json.substring(json.indexOf("http"), json.indexOf("\"", json.indexOf("http")));
		}
		catch (MalformedURLException ex)
		{
			return Colors.RED + "Invalid link";
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return Colors.RED + "Something went wrong...";
		}
	}
}