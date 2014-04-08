package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.pircbotx.*;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandMcStatus extends Command
{
    private final FoxBot foxbot;
    private String online = Colors.DARK_GREEN + "✔" + Colors.NORMAL;
    private String offline = Colors.RED + "✘" + Colors.NORMAL;

    public CommandMcStatus(FoxBot foxbot)
    {
        super("mcstatus", "command.mcstatus", "mcs");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 0)
        {
            StringBuilder statusString = new StringBuilder(String.format("(%s) ", Utils.munge(sender.getNick())));

	        Connection conn = Jsoup.connect("http://status.mojang.com/check").timeout(500).followRedirects(true).ignoreContentType(true);
	        String json;

	        try
	        {
		        json = conn.get().text();
	        }
	        catch (IOException ex)
	        {
		        foxbot.log(ex);
		        channel.sendMessage(Utils.colourise(String.format("(%s) &cAn error occurred while querying Mojang's status page!", Utils.munge(sender.getNick()))));
		        return;
	        }

	        JSONArray jsonArray = new JSONArray(json);

	        for (int i = 0; i < jsonArray.length(); i++)
	        {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        String key = (String) jsonObject.keys().next();
		        statusString.append("| ").append(key).append(" ").append(jsonObject.getString(key).equals("green") ? online : offline).append(" ");
	        }

            statusString.append("|");
            channel.sendMessage(statusString.toString());
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smcstatus", foxbot.getConfig().getCommandPrefix()));
    }
}
