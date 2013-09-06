package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.Utils;

import java.io.IOException;
import java.util.concurrent.Future;

public class CommandHaspaid extends Command
{
    private FoxBot foxbot;

    public CommandHaspaid(FoxBot foxbot)
    {
        super("haspaid", "command.ping", "mcpaid");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();

        if (args.length == 1)
        {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            try
            {
                channel.sendMessage(asyncHttpClient.prepareGet("https://minecraft.net/haspaid.jsp?user=" + args[0]).execute().get().getResponseBody().contains("true") ? String.format("(%s) The account \"%s\"' is a %spremium%s Minecraft account!", Utils.munge(sender.getNick()), args[0], Colors.GREEN, Colors.NORMAL) : String.format("(%s) The account \"%s\" is %sNOT%s a premium Minecraft account!", Utils.munge(sender.getNick()), args[0], Colors.RED, Colors.NORMAL));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                channel.sendMessage("Something went wrong...");
            }
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %shaspaid <user>", foxbot.getConfig().getCommandPrefix()));
    }
}
