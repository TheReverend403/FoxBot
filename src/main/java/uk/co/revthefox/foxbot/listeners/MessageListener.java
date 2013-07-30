package uk.co.revthefox.foxbot.listeners;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class MessageListener extends ListenerAdapter
{
    private FoxBot foxbot;

    public MessageListener(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void onMessage(MessageEvent event)
    {
        String message = event.getMessage();
        User user = event.getUser();

        if (message.startsWith(foxbot.getConfig().getCommandPrefix()))
        {
            commandHandler(message, user, message.substring(1).split(" "));
            return;
        }
    }

    public void commandHandler(String command, User commandSender, String[] args)
    {

    }
}
