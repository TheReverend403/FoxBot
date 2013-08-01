package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.CommandBan;
import uk.co.revthefox.foxbot.commands.CommandKick;
import uk.co.revthefox.foxbot.commands.CommandKill;

public class MessageListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void onMessage(MessageEvent event)
    {
        String message = event.getMessage();
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (message.length() > 0 && message.startsWith(foxbot.getConfig().getCommandPrefix()))
        {
            foxbot.getCommandManager().dispatchCommand(user, channel, message.substring(1));
        }
    }
}
