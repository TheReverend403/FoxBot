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

import co.foxdev.foxbot.logger.BotLogger;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringEscapeUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import co.foxdev.foxbot.FoxBot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

    private final FoxBot foxbot;

    public Utils(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>.+</title>", Pattern.DOTALL);

    public String parseChatUrl(String stringToParse, User sender)
    {
        try
        {
            AsyncHttpClientConfig clientConf = new AsyncHttpClientConfig.Builder().setUserAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36").build();
            AsyncHttpClient client = new AsyncHttpClient(clientConf);
            Future<Response> future = client.prepareGet(stringToParse).setFollowRedirects(true).execute();
            Response response = future.get();
            String output = response.getResponseBody();
            String size = (response.getResponseBodyAsBytes().length / 1024) + "kb";
            String contentType = response.getContentType().contains(";") ? response.getContentType().split(";")[0] : response.getContentType();

            if (response.getStatusCode() != 200 && response.getStatusCode() != 302 && response.getStatusCode() != 301)
            {
                return colourise(String.format("(%s's URL) &cError: &r%s %s ", munge(sender.getNick()), response.getStatusCode(), response.getStatusText()));
            }

            if (!contentType.contains("html"))
            {
                return colourise(String.format("(%s's URL) &2Content Type: &r%s &2Size: &r%s", munge(sender.getNick()), contentType, size));
            }

            Matcher matcher;
            String title = "No title found";

            for (String line : output.split("\n"))
            {
                matcher = TITLE_PATTERN.matcher(line);

                if (matcher.find())
                {
                    title = line.split("<title>")[1].split("</title>")[0];
                }
            }
            return colourise(String.format("(%s's URL) &2Title: &r%s &2Content Type: &r%s &2Size: &r%s", munge(sender.getNick()), StringEscapeUtils.unescapeHtml4(title), contentType, size));
        }
        catch (IllegalArgumentException ex)
        {
            // Who cares?
        }
        catch (Exception ex)
        {
            BotLogger.log(Level.WARNING, "Exception occurred while parsing chat URL");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return "null";
    }

    public String munge(String stringToMunge)
    {
        return foxbot.getConfig().getMungeUsernames() ? stringToMunge.replace("a", "ä").replace("e", "è").replace("o", "ö").replace("u", "ù").replace("s", "š").replace("i", "í").replace("n", "ñ") : stringToMunge;
    }

    public String colourise(String stringToColour)
    {
        char colourChar = foxbot.getConfig().getColourChar();

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
                .replace(colourChar + "n", Colors.UNDERLINE);
    }

    public String getPrefix(User user, Channel channel)
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

    public void addCustomCommand(String channel, String command, String text) throws IOException
    {
        String filePath = "data/custcmds/" + channel.substring(1);
        File path = new File(filePath);

        if (!path.exists() && !path.mkdirs())
        {
            throw new IOException();
        }

        File file = new File(filePath + "/" + command);

        if (file.exists())
        {
            if (!file.delete())
            {
                throw new IOException();
            }
        }

        FileWriter fw = new FileWriter(filePath + "/" + command);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(text);
        bw.close();
        fw.close();
    }

    public void scheduleUnban(final Channel channel, final String hostmask, final int time)
    {
        new Timer().schedule(
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        foxbot.unBan(channel, hostmask);
                    }
                },
                TimeUnit.SECONDS.toMillis(time)
        );
    }

    public void scheduleModeRemove(final Channel channel, final String hostmask, final String mode, final int time)
    {
        new Timer().schedule(
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        foxbot.setMode(channel, "-" + mode + " " + hostmask);
                    }
                },
                TimeUnit.SECONDS.toMillis(time)
        );
    }
}