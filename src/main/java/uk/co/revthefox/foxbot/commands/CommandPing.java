package uk.co.revthefox.foxbot.commands;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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
        Channel channel = event.getChannel();
        User sender = event.getUser();
        String host;
        int port = 80;

        if (args.length == 1 || args.length == 2)
        {
            try
            {
                host = args[0];
                port = args.length == 2 ? Integer.parseInt(args[1]) : port;

                long start = System.currentTimeMillis();
                Socket socket = new Socket(InetAddress.getByName(host), port);
                long end = System.currentTimeMillis();
                
                socket.close();
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aPing response time: &r%sms",foxbot.getUtils().munge(sender.getNick()), end - start)));
            }
            catch (UnknownHostException ex)
            {
                foxbot.sendNotice(sender, String.format("%s is an unknown address!", args[0]));
            }
            catch (IOException ex)
            {
                foxbot.sendMessage(channel, foxbot.getUtils().colourise(String.format("(%s) &cSomething went wrong...", foxbot.getUtils().munge(sender.getNick()))));
            }
            catch (IllegalArgumentException ex)
            {
                if (StringUtils.isAlpha(args[1]))
                {
                    foxbot.sendNotice(sender, String.format("%s is not a number!", args[1]));
                }
                else
                {
                    foxbot.sendNotice(sender, String.format("%s is too high a number for a port!", args[1]));
                }
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sping <address> [port]", foxbot.getConfig().getCommandPrefix()));
    }
}
//
