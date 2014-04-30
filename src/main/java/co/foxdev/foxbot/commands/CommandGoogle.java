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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandGoogle extends Command
{
	private final FoxBot foxbot;

	/**
	 * Performs a Google search and sends the first result to the channel.
	 *
	 * Usage: .google <search term>
	 */
	public CommandGoogle(FoxBot foxbot)
	{
		super("google", "command.google", "g");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(MessageEvent event, String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length != 0)
		{
			String query = StringUtils.join(args, " ");
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + query;

			Connection conn = Jsoup.connect(address).ignoreContentType(true).followRedirects(true).timeout(1000);

			JSONObject jsonObject;

			try
			{
				jsonObject = new JSONObject(conn.get().text());
			}
			catch (IOException ex)
			{
				foxbot.log(ex);
				channel.send().message(Utils.colourise(String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick()))));
				return;
			}

			JSONArray jsonArray = jsonObject.getJSONObject("responseData").getJSONArray("results");

			if (jsonArray.length() == 0)
			{
				channel.send().message(Utils.colourise(String.format("(%s) &cNo results found!", Utils.munge(sender.getNick()))));
				return;
			}

			JSONObject result = jsonArray.getJSONObject(0);
			String resultCount = jsonObject.getJSONObject("responseData").getJSONObject("cursor").getString("resultCount");

			String title = result.getString("titleNoFormatting");
			String url = result.getString("url");

			channel.send().message(Utils.colourise(String.format("(%s's Google Search) &2Title: &r%s &2URL: &r%s &2Results: &r%s", Utils.munge(sender.getNick()), StringEscapeUtils.unescapeHtml4(title), url, resultCount)));
			return;
		}
		sender.send().notice(String.format("Wrong number of args! Use %sgoogle <query>", foxbot.getConfig().getCommandPrefix()));
	}
}
