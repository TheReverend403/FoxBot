package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

import java.util.List;

public class CommandTell extends Command
{
    private FoxBot foxbot;

    public CommandTell(FoxBot foxbot)
    {
        super("tell", "command.tell");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("list"))
            {
                List<String> tells = foxbot.getDatabase().getTells(sender.getNick(), true);

                if (!tells.isEmpty())
                {
                    for (String tell : tells)
                    {
                        foxbot.sendNotice(sender, Utils.colourise(tell));
                    }
                    return;
                }
                foxbot.sendNotice(sender, "No messages for you :<");
                return;
            }

            if (args[0].equalsIgnoreCase("clean"))
            {
                foxbot.getDatabase().cleanTells(sender.getNick());
                foxbot.sendNotice(sender, "Deleted all of your read messages.");
                return;
            }
        }

        if (args.length > 1)
        {
            String nick = args[0];

            StringBuilder message = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                message.append(" ").append(args[arg]);
            }

            foxbot.getDatabase().addTell(sender.getNick(), nick, message.toString());
            foxbot.sendNotice(sender, String.format("Tell added for %s", nick));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %stell <nick> <message> or %stell list", foxbot.getConfig().getCommandPrefix(), foxbot.getConfig().getCommandPrefix()));
    }
}
