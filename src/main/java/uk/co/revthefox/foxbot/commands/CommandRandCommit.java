package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: thereverend403
 * Date: 11/08/13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class CommandRandCommit extends Command
{
    private FoxBot foxbot;

    public CommandRandCommit(FoxBot foxbot)
    {
        super("wtc", "command.wtc");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Future<Response> future;
            Response response;

            Matcher matcher;

            String commitMessage = "";

            try
            {
                future = asyncHttpClient.prepareGet("http://whatthecommit.com/").execute();
                response = future.get();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                foxbot.getBot().sendMessage(channel, "Something went wrong...");
                return;
            }

            try
            {
                commitMessage = response.getResponseBody();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            Pattern titlePattern = Pattern.compile("<p>(.*)</p>.*<p class=\"permalink\">.*</p>", Pattern.DOTALL);

            matcher = titlePattern.matcher(commitMessage);

            while (matcher.find())
            {
                commitMessage = matcher.group(1);
            }
            foxbot.getBot().sendMessage(channel, String.format("(%s) %sRandom commit message: %s%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, commitMessage));
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %swtc",
                foxbot.getConfig().getCommandPrefix()));
    }
}
