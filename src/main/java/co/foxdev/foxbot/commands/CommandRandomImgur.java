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

import co.foxdev.foxbot.utils.Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.*;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.io.IOException;
import java.util.Random;

public class CommandRandomImgur extends Command
{
    private final FoxBot foxbot;

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
            String link = "";

            while (link.equals(""))
            {
                link = generateLink();
            }

            if (!link.equals("exception"))
            {
                channel.sendMessage(Utils.colourise(String.format("(%s) &2Random Imgur: &r%s", Utils.munge(sender.getNick()), link)));
                return;
            }
            channel.sendMessage(Utils.colourise(String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick()))));
        }
    }

    private String generateLink()
    {
        String imgurLink = rand.nextBoolean() ? String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(5)) : String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(7));

        try
        {
	        // We're not trying to get content, we're just connecting until we don't 404.
	        Connection.Response response = Jsoup.connect(imgurLink).timeout(300).followRedirects(true).execute();
        }
        // Invalid link? Try again.
        catch (HttpStatusException ex)
        {
	        return "";
        }
	    catch (IOException ex)
	    {
		    foxbot.log(ex);
		    return "exception";
	    }

	    return imgurLink;
    }
}