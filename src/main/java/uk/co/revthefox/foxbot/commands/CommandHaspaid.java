package uk.co.revthefox.foxbot.commands;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

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
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 1)
        {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Future<Response> future;
            Response response;

            try
            {
                future = asyncHttpClient.prepareGet("https://minecraft.net/haspaid.jsp?user=" + args[0]).execute();
                response = future.get();
                channel.sendMessage(response.getResponseBody().contains("true") ? String.format("(%s) The account \"%s\"' is a %spremium%s Minecraft account!", foxbot.getUtils().munge(sender.getNick()), args[0], Colors.GREEN, Colors.NORMAL) : String.format("(%s) The account \"%s\" is %sNOT%s a premium Minecraft account!", foxbot.getUtils().munge(sender.getNick()), args[0], Colors.RED, Colors.NORMAL));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                channel.sendMessage("Something went wrong...");
            }
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %shaspaid <user>",
                foxbot.getConfig().getCommandPrefix()));
    }
}
