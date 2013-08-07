package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter
{
    private FoxBot foxbot;

    private String urlInfo;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private String urlPattern = ".*((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
    private Pattern patt = Pattern.compile(urlPattern);

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

        matcher = patt.matcher(message);
        if (!matcher.matches())
        {
            return;
        }

        message = matcher.group(1);

        if (foxbot.getPermissionManager().userHasPermission(user, "chat.urls"))
        {
            urlInfo = foxbot.getUtils().parseChatUrl(message, user);
            if (!urlInfo.isEmpty())
            {
                channel.sendMessage(urlInfo);
            }
        }
    }
}
