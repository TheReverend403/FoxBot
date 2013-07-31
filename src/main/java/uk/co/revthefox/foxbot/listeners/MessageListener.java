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

        if (message.startsWith(foxbot.getConfig().getCommandPrefix()))
        {
            String[] args = message.split(" ");
            String command = args[0].substring(1);
            if (args.length > 1)
            {
                args = message.substring(args[0].length()+1).split(" ");
                commandHandler(command, channel, user, args);
                return;
            }
            commandHandler(command, channel, user, new String[0]);
        }
    }

    public void commandHandler(String command, Channel channel, User commandSender, String[] args)
    {
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
    }
}
