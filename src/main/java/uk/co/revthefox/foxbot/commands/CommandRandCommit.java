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

            Matcher matcher;

            String commitMessage;

            try
            {
                commitMessage = asyncHttpClient.prepareGet("http://whatthecommit.com/").execute().get().getResponseBody();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                channel.sendMessage("Something went wrong...");
                return;
            }

            Pattern titlePattern = Pattern.compile("<p>(.*)</p>.*<p class=\"permalink\">.*</p>", Pattern.DOTALL);

            matcher = titlePattern.matcher(commitMessage);

            while (matcher.find())
            {
                commitMessage = matcher.group(1);
            }
            channel.sendMessage(String.format("(%s) %sRandom commit message: %s%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, commitMessage));
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %swtc", foxbot.getConfig().getCommandPrefix()));
    }
}
