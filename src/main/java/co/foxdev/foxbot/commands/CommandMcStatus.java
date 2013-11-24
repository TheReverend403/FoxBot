package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.net.InetAddress;
import java.net.Socket;

public class CommandMcStatus extends Command
{
    private final FoxBot foxbot;
    private String online = Colors.DARK_GREEN + "✔";
    private String offline = Colors.RED + "✘";
    private String[] urls = new String[]{
            "minecraft.net",
            "login.minecraft.net",
            "session.minecraft.net",
            "skins.minecraft.net",
            "realms.minecraft.net"
    };

    public CommandMcStatus(FoxBot foxbot)
    {
        super("mcstatus", "command.mcstatus", "mcs");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 0)
        {
            StringBuilder statusString = new StringBuilder(String.format("(%s) ", foxbot.getUtils().munge(sender.getNick())));

            for (String url : urls)
            {
                try
                {
                    Socket socket = new Socket(InetAddress.getByName(url), 80);
                    socket.setSoTimeout(5000);
                    statusString.append("| ").append(url).append(" ").append(online).append(" ");
                    socket.close();
                }
                catch (Exception ex)
                {
                    statusString.append("| ").append(url).append(" ").append(offline).append(" ");
                }
            }
            statusString.append("|");
            channel.sendMessage(statusString.toString());
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smcstatus", foxbot.getConfig().getCommandPrefix()));
    }
}
