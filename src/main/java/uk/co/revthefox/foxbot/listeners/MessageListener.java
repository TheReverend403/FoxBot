package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private String urlPattern = ".*((https?)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
    private Pattern patt = Pattern.compile(urlPattern);

    Random rand = new Random();

    @Override
    public void onMessage(MessageEvent event)
    {
        String message = event.getMessage();
        User user = event.getUser();
        Channel channel = event.getChannel();
        Matcher matcher;

        if (message.length() > 0 && message.startsWith(foxbot.getConfig().getCommandPrefix()))
        {
            foxbot.getCommandManager().dispatchCommand(user, channel, message.substring(1));
        }

        if (message.length() > 0 && message.startsWith(foxbot.getConfig().getBotNick() + ", "))
        {
            foxbot.getCommandManager().dispatchCommand(user, channel, message.substring(foxbot.getConfig().getBotNick().length() + 2));
        }

        if (rand.nextInt(100) == 50)
        {
            foxbot.getBot().sendMessage(channel, "O)_(O");
        }

        if (message.toLowerCase().contains("pex") || message.toLowerCase().contains("permissionsex"))
        {
            foxbot.getBot().kick(channel, user, "We don't use that language round 'ere.");
        }

        matcher = patt.matcher(message);
        if (!matcher.matches())
        {
            return;
        }

        message = matcher.group(1);

        if (foxbot.getPermissionManager().userHasPermission(user, "chat.urls"))
        {
            String urlInfo = foxbot.getUtils().parseChatUrl(message, user);
            if (!urlInfo.isEmpty())
            {
                channel.sendMessage(urlInfo);
            }
        }
    }
}
