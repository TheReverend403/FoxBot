package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

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

    private Pattern commitPattern = Pattern.compile("<p>(.*)</p>.*<p class=\"permalink\">.*</p>", Pattern.DOTALL);

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

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

            matcher = commitPattern.matcher(commitMessage);

            while (matcher.find())
            {
                commitMessage = matcher.group(1);
            }
            channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aRandom commit message: &r%s", foxbot.getUtils().munge(sender.getNick()), commitMessage)));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %swtc", foxbot.getConfig().getCommandPrefix()));
    }
}
