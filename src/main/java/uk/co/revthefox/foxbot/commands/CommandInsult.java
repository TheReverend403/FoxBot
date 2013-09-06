package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

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
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();

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
                ex.printStackTrace();
                bot.sendMessage(channel, "Something went wrong...");
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
                    if (bot.getChannel(args[0]).isInviteOnly())
                    {
                        bot.sendNotice(sender, String.format("%s is invite only!", args[0]));
                        return;
                    }

                    bot.joinChannel(args[0]);

                    if (!args[args.length - 1].equalsIgnoreCase("-s"))
                    {
                        bot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                        bot.partChannel(bot.getChannel(args[0]));
                        bot.sendNotice(sender, String.format("Insult sent to %s, and channel has been left", args[0]));
                        return;
                    }
                    bot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                    bot.sendNotice(sender, String.format("Insult sent to %s", args[0]));
                    return;
                }
                bot.sendNotice(sender, String.format("%s is not a channel...", args[0]));
                return;
            }
            channel.sendMessage(insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %sinsult [#channel] [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}