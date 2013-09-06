package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

import java.util.Random;

public class CommandRandomImgur extends Command
{
    private FoxBot foxbot;

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
                channel.sendMessage(Utils.colourise(String.format("(%s) &aRandom Imgur: &r%s", Utils.munge(sender.getNick()), link)));
                return;
            }
            channel.sendMessage(Utils.colourise("&cSomething went wrong..."));
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
            ex.printStackTrace();
            return "exception";
        }

        if (!String.valueOf(response.getStatusCode()).contains("404"))
        {
            return imgurLink;
        }
        return "";
    }
}
