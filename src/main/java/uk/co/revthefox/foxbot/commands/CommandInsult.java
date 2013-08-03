package uk.co.revthefox.foxbot.commands;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length > 2)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %sinsult [#channel] [flags]",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> future;
        Response response;

        Matcher matcher;

        String insult = "";

        try
        {
            future = asyncHttpClient.prepareGet("http://www.pangloss.com/seidel/Shaker/").execute();
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
            insult = response.getResponseBody();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Pattern titlePattern = Pattern.compile(".*?<font.*>(.*)</font>.*?", Pattern.DOTALL);

        matcher = titlePattern.matcher(insult);

        while (matcher.find()) {
            insult = matcher.group(1);
        }

        if (args.length > 0)
        {
            if (args[0].startsWith("#"))
            {
                if (foxbot.getBot().getChannel(args[0]).isInviteOnly())
                {
                    foxbot.getBot().sendNotice(sender, String.format("%s is invite only!", args[0]));
                    return;
                }

                foxbot.getBot().joinChannel(args[0]);

                if (!args[args.length - 1].equalsIgnoreCase("-s"))
                {
                    foxbot.getBot().sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", ""));
                    foxbot.getBot().partChannel(foxbot.getBot().getChannel(args[0]));
                    foxbot.getBot().sendNotice(sender, String.format("Insult sent to %s, and channel has been left", args[0]));
                    return;
                }

                foxbot.getBot().sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", ""));
                foxbot.getBot().sendNotice(sender, String.format("Insult sent to %s", args[0]));
                return;
            }
            foxbot.getBot().sendNotice(sender, String.format("%s is not a channel...", args[0]));
            return;
        }
        foxbot.getBot().sendMessage(channel, insult.replace("[", "").replace("]", "").replaceAll("^\\s", ""));
    }
}
