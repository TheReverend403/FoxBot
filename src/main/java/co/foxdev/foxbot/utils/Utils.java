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

package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.logger.BotLogger;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils
{
    public static String parseChatUrl(String stringToParse, User sender)
    {
        try
        {
	        Connection conn = Jsoup.connect(stringToParse);

	        conn.followRedirects(true)
	            .userAgent("FoxBot // https://github.com/FoxDev/FoxBot // Seeing this? It means your web address was posted on IRC and FoxBot is getting page info (title, size, content type) to send to the channel. Nothing to worry about.")
	            .timeout(3000)
	            .maxBodySize(100000);

	        Connection.Response response = conn.execute();
	        Document doc = response.parse();
            String size = response.header("Content-Length") == null ? "Unknown" : (Integer.parseInt(response.header("Content-Length")) / 1024) + "kb";
            String contentType = response.contentType().contains(";") ? response.contentType().split(";")[0] : response.contentType();

            if (response.statusCode() != 200 && response.statusCode() != 302 && response.statusCode() != 301)
            {
                return colourise(String.format("(%s's URL) &cError: &r%s %s ", munge(sender.getNick()), response.statusCode(), response.statusMessage()));
            }

            if (!contentType.contains("html"))
            {
                return colourise(String.format("(%s's URL) &2Content Type: &r%s &2Size: &r%s", munge(sender.getNick()), contentType, size));
            }

	        String title = doc.title() == null || doc.title().isEmpty() ? "No title found" : doc.title();

	        if (stringToParse.matches("^https?://(www\\.)?youtube\\.com/watch/.*"))
	        {
		        title = doc.select("a#watch-headline-show-title").text();
		        String views = doc.select("span.watch-view-count").first().text();
		        String likes = doc.select("span.likes-count").first().text();
		        String dislikes = doc.select("span.dislikes-count").first().text();
		        String uploader = doc.select("a.g-hovercard.yt-uix-sessionlink.yt-user-name").first().text();

		        return colourise(String.format("(%s's URL) &2Title: &r%s &2Uploader: &r%s &2Views: &r%s &2Rating: &a%s&r/&c%s", munge(sender.getNick()), StringEscapeUtils.unescapeHtml4(title), uploader, views, likes, dislikes));
	        }
            return colourise(String.format("(%s's URL) &2Title: &r%s &2Content Type: &r%s &2Size: &r%s", munge(sender.getNick()), StringEscapeUtils.unescapeHtml4(title), contentType, size));
        }
        catch (IllegalArgumentException ignored)
        {

        }
        catch (Exception ex)
        {
            BotLogger.log(Level.WARNING, "Exception occurred while parsing chat URL");
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "null";
    }

    public static String munge(String stringToMunge)
    {
        return FoxBot.getInstance().getConfig().getMungeUsernames() ? stringToMunge.replace("a", "ä").replace("e", "è").replace("o", "ö").replace("u", "ù").replace("s", "š").replace("i", "í").replace("n", "ñ") : stringToMunge;
    }

    public static String colourise(String stringToColour)
    {
		return colourise(stringToColour, '&');
    }

	public static String colourise(String stringToColour, char colourChar)
	{
		return stringToColour.replace(colourChar + "0", Colors.BLACK)
		                     .replace(colourChar + "1", Colors.DARK_BLUE)
		                     .replace(colourChar + "2", Colors.DARK_GREEN)
		                     .replace(colourChar + "3", Colors.TEAL)
		                     .replace(colourChar + "4", Colors.RED)
		                     .replace(colourChar + "5", Colors.PURPLE)
		                     .replace(colourChar + "6", Colors.BROWN)
		                     .replace(colourChar + "7", Colors.LIGHT_GRAY)
		                     .replace(colourChar + "8", Colors.DARK_GRAY)
		                     .replace(colourChar + "9", Colors.BLUE)
		                     .replace(colourChar + "a", Colors.GREEN)
		                     .replace(colourChar + "b", Colors.CYAN)
		                     .replace(colourChar + "c", Colors.RED)
		                     .replace(colourChar + "d", Colors.MAGENTA)
		                     .replace(colourChar + "e", Colors.YELLOW)
		                     .replace(colourChar + "f", Colors.WHITE)
		                     .replace(colourChar + "r", Colors.NORMAL)
		                     .replace(colourChar + "l", Colors.BOLD)
		                     .replace(colourChar + "n", Colors.UNDERLINE)
		                     .replace(colourChar + "m", "")
		                     .replace(colourChar + "k", "")
		                     .replace(colourChar + "o", "");

	}

    public static String getPrefix(User user, Channel channel)
    {
        if (channel.isOwner(user))
        {
            return "~";
        }
        else if (channel.isSuperOp(user))
        {
            return "&";
        }
        else if (channel.isOp(user))
        {
            return "@";
        }
        else if (channel.isHalfOp(user))
        {
            return "%";
        }
        else if (channel.hasVoice(user))
        {
            return "+";
        }
        else
        {
            return "";
        }
    }

    public static boolean addCustomCommand(String channel, String command, String text)
    {
        String filePath = "data/custcmds/" + channel.substring(1);
        File path = new File(filePath);

        try
        {
            if (!path.exists() && !path.mkdirs())
            {
                BotLogger.log(Level.WARNING, "Error while creating custom command folders!");
            }

            File file = new File(filePath + "/" + command);

            if (file.exists() && !file.delete())
            {
                BotLogger.log(Level.WARNING, "Error while deleting old command!");
            }

            if (text.equalsIgnoreCase("delete"))
            {
                if (file.delete())
                {
                    BotLogger.log(Level.INFO, String.format("Command '%s' deleted for %s!", command, channel));
                }
                return false;
            }

            FileWriter fw = new FileWriter(filePath + "/" + command);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(text);
            bw.close();
            fw.close();
            BotLogger.log(Level.INFO, String.format("Command '%s' set for %s at %s", command, channel, file.getAbsolutePath()));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return true;
    }

    public static void scheduleUnban(final Channel channel, final String hostmask, final int time)
    {
        new Timer().schedule(
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
	                    FoxBot.getInstance().unBan(channel, hostmask);
                    }
                },
                TimeUnit.SECONDS.toMillis(time)
        );
    }

    public static void scheduleModeRemove(final Channel channel, final String hostmask, final String mode, final int time)
    {
        new Timer().schedule(
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
	                    FoxBot.getInstance().setMode(channel, "-" + mode + " " + hostmask);
                    }
                },
                TimeUnit.SECONDS.toMillis(time)
        );
    }
}