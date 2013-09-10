package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInsult extends Command
{
    private FoxBot foxbot;

    public CommandInsult(FoxBot foxbot)
    {
        super("insult", "command.insult");
        this.foxbot = foxbot;
    }

    private Pattern titlePattern = Pattern.compile(".*?<font.*?>(.*)</font>.*?", Pattern.DOTALL);

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length < 3)
        {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Matcher matcher;
            String insult;

            try
            {
                insult = asyncHttpClient.prepareGet("http://www.pangloss.com/seidel/Shaker/").execute().get().getResponseBody();
            }
            catch (Exception ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                foxbot.sendMessage(channel, "Something went wrong...");
                return;
            }

            matcher = titlePattern.matcher(insult);

            while (matcher.find())
            {
                insult = matcher.group(1);
            }

            if (args.length > 0)
            {
                if (args[0].startsWith("#"))
                {
                    if (foxbot.getChannel(args[0]).isInviteOnly())
                    {
                        foxbot.sendNotice(sender, String.format("%s is invite only!", args[0]));
                        return;
                    }

                    foxbot.joinChannel(args[0]);

                    if (!args[args.length - 1].equalsIgnoreCase("-s"))
                    {
                        foxbot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                        foxbot.partChannel(foxbot.getChannel(args[0]));
                        foxbot.sendNotice(sender, String.format("Insult sent to %s, and channel has been left", args[0]));
                        return;
                    }
                    foxbot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                    foxbot.sendNotice(sender, String.format("Insult sent to %s", args[0]));
                    return;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", args[0]));
                return;
            }
            channel.sendMessage(insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sinsult [#channel] [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}