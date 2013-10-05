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

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandRandomImgur extends Command
{
    private final FoxBot foxbot;

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private Random rand = new Random();

    public CommandRandomImgur(FoxBot foxbot)
    {
        super("imgur", "command.imgur");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 0)
        {
            String link;
            for (; ; )
            {
                link = generateLink();

                if (!link.equals(""))
                {
                    // I feel this might be necessary...
                    System.gc();
                    break;
                }
            }

            if (!link.equals("exception"))
            {
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &2Random Imgur: &r%s", foxbot.getUtils().munge(sender.getNick()), link)));
                return;
            }
            channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &cSomething went wrong...", foxbot.getUtils().munge(sender.getNick()))));
        }
    }

    private String generateLink()
    {
        Response response;
        String imgurLink = rand.nextBoolean() ? String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(5)) : String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(7));

        try
        {
            response = asyncHttpClient.prepareGet(imgurLink).execute().get();
        }
        catch (Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return "exception";
        }

        if (!String.valueOf(response.getStatusCode()).contains("404"))
        {
            return imgurLink;
        }
        return "";
    }
}