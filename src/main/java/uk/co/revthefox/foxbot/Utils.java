package uk.co.revthefox.foxbot;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
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

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private Response response;

    private String urlPattern = ".*((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
    private Pattern patt = Pattern.compile(urlPattern);
    private Matcher matcher;

    private Future<Response> future;

    public Utils(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public String parseChatUrl(String stringToParse, User sender)
    {
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
            stringToParse = response.getResponseBody().toString();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Pattern titlePattern = Pattern.compile("<head>.*?<title>(.*?)</title>.*?</head>", Pattern.DOTALL);
        Matcher m = titlePattern.matcher(stringToParse);
        while (m.find()) {
            stringToParse = m.group(1);
        }

        return String.format("(%s's URL) %sTitle: %s%s %sContent type: %s%s", sender.getNick(), Colors.GREEN, Colors.NORMAL, stringToParse.replace("&#039;", "'"), Colors.GREEN, Colors.NORMAL, response.getContentType());
    }

}
