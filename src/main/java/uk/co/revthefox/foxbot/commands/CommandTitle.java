package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.TagElement;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CommandTitle extends Command
{

    private FoxBot foxbot;

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private Response response;

    public CommandTitle(FoxBot foxbot)
    {
        super("title", "command.title");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length != 1)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %stitle <address>", foxbot.getConfig().getCommandPrefix()));
            return;
        }
        try
        {
            Future<Response> future = asyncHttpClient.prepareGet(args[0]).execute();
            response = future.get();
        }
        catch (IOException ex)
        {
            channel.sendMessage("IOException");
            ex.printStackTrace();
            return;
        }
        catch (InterruptedException ex)
        {
            channel.sendMessage("InterruptedException");
            ex.printStackTrace();
            return;
        }
        catch (ExecutionException ex)
        {
            channel.sendMessage("ExecutionException");
            ex.printStackTrace();
            return;
        }
        channel.sendMessage(String.format("(%s) %sContent type: %s%s", sender.getNick(), Colors.GREEN, Colors.NORMAL,
                response.getContentType()));
    }
}
