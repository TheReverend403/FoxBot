package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class CommandPing extends Command
{
    private FoxBot foxbot;

    public CommandPing(FoxBot foxbot)
    {
        super("ping", "command.ping");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Channel channel = event.getChannel();
                User sender = event.getUser();
                PircBotX bot = foxbot.getBot();

                if (args.length == 1)
                {
                    try
                    {
                        URL url = new URL(args[0].startsWith("http://") ? args[0] : "http://" + args[0]);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setConnectTimeout(10000);
                        urlConn.setInstanceFollowRedirects(true);
                        long startTime = System.currentTimeMillis();
                        urlConn.connect();
                        long endTime = System.currentTimeMillis();
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
                        {
                            channel.sendMessage(Utils.colourise(String.format("&aPing response time: &r%sms", (endTime - startTime))));
                            urlConn.disconnect();
                            return;
                        }
                    }
                    catch (MalformedURLException ex)
                    {
                        bot.sendNotice(sender, String.format("%s is not a valid address!", args[0]));
                        ex.printStackTrace();
                    }
                    catch (UnknownHostException ex)
                    {
                        bot.sendNotice(sender, String.format("%s is an unknown address!", args[0]));
                        ex.printStackTrace();
                    }
                    catch (IOException ex)
                    {
                        channel.sendMessage("Something went wrong...");
                        ex.printStackTrace();
                    }
                    return;
                }
                bot.sendNotice(sender, String.format("Wrong number of args! Use %sping <address>", foxbot.getConfig().getCommandPrefix()));
            }
        }).start();
    }
}
