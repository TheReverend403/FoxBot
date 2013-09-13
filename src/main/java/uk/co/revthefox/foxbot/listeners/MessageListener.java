package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter<FoxBot>
{
    private FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    private Pattern urlPattern = Pattern.compile(".*((https?)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*");

    @Override
    public void onMessage(MessageEvent<FoxBot> event)
    {
        String message = event.getMessage();
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (message.length() > 0 && (message.startsWith(foxbot.getConfig().getCommandPrefix()) || message.startsWith(foxbot.getConfig().getBotNick() + ", ")))
        {
            foxbot.getPluginManager().dispatchCommand(event, message.substring(message.startsWith(foxbot.getConfig().getCommandPrefix()) ? 1 : foxbot.getConfig().getBotNick().length() + 2));
        }

        /* 
        if (message.toLowerCase().contains("pex") || message.toLowerCase().contains("permissionsex"))
        {
            foxbot.kick(channel, user, "We don't use that language round 'ere.");
        } 
        */

        Matcher matcher = urlPattern.matcher(message);

        if (matcher.matches() && !user.getNick().equalsIgnoreCase(foxbot.getNick()) && foxbot.getPermissionManager().userHasQuietPermission(user, "chat.urls"))
        {
            message = foxbot.getUtils().parseChatUrl(matcher.group(1), user);

            if (!message.isEmpty() && !message.equalsIgnoreCase("null"))
            {
                channel.sendMessage(message);
            }
        }
    }
}