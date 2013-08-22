package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

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

    private static final Pattern COMMIT_PATTERN = Pattern.compile("<p>(.*)</p>.*<p class=\"permalink\">.*</p>", Pattern.DOTALL);

    @Override
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();

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

            matcher = COMMIT_PATTERN.matcher(commitMessage);

            while (matcher.find())
            {
                commitMessage = matcher.group(1);
            }
            channel.sendMessage(String.format("(%s) %sRandom commit message: %s%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, commitMessage));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! use %swtc", foxbot.getConfig().getCommandPrefix()));
    }
}
