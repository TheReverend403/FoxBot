package uk.co.revthefox.foxbot;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Colors;
import org.pircbotx.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    private FoxBot foxbot;

    private String urlPattern = ".*((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
    private Pattern patt = Pattern.compile(urlPattern);

    public Utils(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public String parseChatUrl(String stringToParse, User sender)
    {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Matcher matcher;
        Future<Response> future;
        Response response;

        matcher = patt.matcher(stringToParse);
        if (!matcher.matches())
        {
            return "";
        }
        stringToParse = matcher.group(1);
        try
        {
            future = asyncHttpClient.prepareGet(stringToParse).execute();
            response = future.get();
            if (!response.getStatusText().contains("OK") && !response.getStatusText().contains("Moved Permanently"))
            {
                return String.format("(%s's URL) %sError: %s%s %s ", sender.getNick(), Colors.RED, Colors.NORMAL,response.getStatusCode(), response.getStatusText());
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return "";
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
            return "";
        }
        catch (ExecutionException ex)
        {
            System.out.println("That last URL appeared to be invalid...");
            return "";
        }

        try
        {
            stringToParse = response.getResponseBody();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Pattern titlePattern = Pattern.compile(".*?<title.*?>(.*)</title>.*?",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher titleMatcher = titlePattern.matcher(stringToParse);
        while (titleMatcher.find())
        {
            stringToParse = titleMatcher.group(1);
        }

        return String.format("(%s's URL) %sTitle: %s%s %sContent type: %s%s", sender.getNick(), Colors.GREEN,
                Colors.NORMAL, stringToParse.replace("&#039;", "'"), Colors.GREEN, Colors.NORMAL, response.getContentType().replaceAll("(;.*)", ""));
    }

}
