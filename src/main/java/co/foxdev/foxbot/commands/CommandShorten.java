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

		try
		{
			URL url = new URL("https://www.googleapis.com/urlshortener/v1/url");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			String postData = "{\"longUrl\": \"" + longUrl + "\"}";

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
			connection.connect();

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(postData);
			wr.flush();
			wr.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = rd.readLine()) != null)
			{
				sb.append(line).append('\n');
			}

			String json = sb.toString();

			connection.disconnect();
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