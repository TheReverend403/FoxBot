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
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CommandShorten extends Command
{
    private final FoxBot foxbot;

    /**
     * Shortens a given link with the goo.gl URL shortener service.
     * <p/>
     * Usage: .shorten <link>
     */
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

            channel.send().message(String.format("(%s) %s", Utils.munge(sender.getNick()), shorten(linkToShorten)));
            return;
        }
        sender.send().notice(String.format("Wrong number of args! Use %sshorten <link>", foxbot.getConfig().getCommandPrefix()));
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
            connection.setRequestProperty("Content-Length", Integer.toString(postData.getBytes().length));
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
            foxbot.getLogger().error("Error occurred while shortening URL", ex);
            return Colors.RED + "Something went wrong...";
        }
    }
}