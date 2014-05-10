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
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.*;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;
import java.util.Random;

public class CommandRandomImgur extends Command
{
    private final FoxBot foxbot;

    private Random rand = new Random();

	/**
	 * Generates a valid link to a random Imgur page and sends it to the channel.
	 *
	 * Usage: .imgur
	 */
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

	    String link = null;

	    while (link == null)
	    {
		    try
		    {
			    link = generateLink();
		    }
		    catch (IOException ex)
		    {
			    foxbot.getLogger().error("Error occurred while generating Imgur URL", ex);
			    channel.send().message(Utils.colourise(String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick()))));
			    return;
		    }
	    }
	    channel.send().message(Utils.colourise(String.format("(%s) &2Random Imgur: &r%s", Utils.munge(sender.getNick()), link)));
    }

    private String generateLink() throws IOException
    {
        String imgurLink = rand.nextBoolean() ? String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(5)) : String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(7));

        try
        {
	        Jsoup.connect(imgurLink).timeout(300).followRedirects(true).execute();
        }
        // Invalid link? Try again.
        catch (HttpStatusException ex)
        {
	        return null;
        }
	    catch (IOException ex)
	    {
		    throw new IOException();
	    }
	    return imgurLink;
    }
}