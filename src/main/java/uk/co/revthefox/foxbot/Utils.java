package uk.co.revthefox.foxbot;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringEscapeUtils;
import org.pircbotx.Colors;
import org.pircbotx.User;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

    private static FoxBot foxbot;

    public Utils(FoxBot foxbot)
    {
        Utils.foxbot = foxbot;
    }

    public static String parseChatUrl(String stringToParse, User sender)
    {
        try
        {
            AsyncHttpClientConfig clientConf = new AsyncHttpClientConfig.Builder().setUserAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36").build();
            AsyncHttpClient client = new AsyncHttpClient(clientConf);
            Future<Response> future = client.prepareGet(stringToParse).setFollowRedirects(true).execute();
            Response response = future.get();
            String output = response.getResponseBody("UTF-8");
            String size = (response.getResponseBodyAsBytes().length / 1024) + "kb";
            String contentType = response.getContentType().contains(";") ? response.getContentType().split(";")[0] : response.getContentType();

            if (response.getStatusCode() != 200 && response.getStatusCode() != 302 && response.getStatusCode() != 301)
            {
                return String.format("(%s's URL) %sError: %s%s %s ", munge(sender.getNick()), Colors.RED, Colors.NORMAL, response.getStatusCode(), response.getStatusText());
            }
            if (!contentType.contains("html"))
            {
                return String.format("(%s's URL) %sContent Type: %s%s %sSize: %s%s", munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, contentType, Colors.GREEN, Colors.NORMAL, size);
            }

            Pattern pattern = Pattern.compile("<title>.+</title>");
            Matcher matcher;
            String title = "No title found";
            for (String line : output.split("\n"))
            {
                matcher = pattern.matcher(line);
                if (matcher.find())
                {
                    title = line.split("<title>")[1].split("</title>")[0];
                }
            }
            return String.format("(%s's URL) %sTitle: %s%s %sContent Type: %s%s %sSize: %s%s", munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, StringEscapeUtils.unescapeHtml4(title), Colors.GREEN, Colors.NORMAL, contentType, Colors.GREEN, Colors.NORMAL, size);
        }
        catch (IllegalArgumentException ex)
        {
            // Who cares?
        }
        catch (Exception ex)
        {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "null";
    }

    public static String munge(String stringToMunge)
    {
        return foxbot.getConfig().getMungeUsernames() ? stringToMunge.replace("a", "ä").replace("e", "è").replace("o", "ö").replace("u", "ù").replace("s", "š").replace("i", "í").replace("n", "ñ") : stringToMunge;
    }

    public static String colourise(String stringToColour)
    {
        String colourChar = foxbot.getConfig().getColourChar();

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
}