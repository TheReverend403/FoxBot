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
            commandHandler(user, channel, message.substring(1));
        }
    }

    public void commandHandler(User commandSender, Channel channel, String commandLine)
    {
        foxbot.getCommandManager().dispatchCommand(commandSender, channel, commandLine);
        /*
        if (command.equalsIgnoreCase("ban"))
        {
            final CommandBan ban = new CommandBan(foxbot);
            ban.execute(channel, commandSender, args);
        }
        if (command.equalsIgnoreCase("kick"))
        {
            final CommandKick kick = new CommandKick(foxbot);
            kick.execute(channel, commandSender, args);
        }
        if (command.equalsIgnoreCase("kill"))
        {
            final CommandKill kill = new CommandKill(foxbot);
            kill.execute(channel, commandSender, args);
        }
        */
    }
}
