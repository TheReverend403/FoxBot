package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

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
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        PircBotX bot = foxbot.getBot();

        if (args.length == 1 && args[0].equalsIgnoreCase("list"))
        {
            List<String> tells = foxbot.getDatabase().getTells(sender.getNick(), false);

            if (!tells.isEmpty())
            {
                for (String tell : tells)
                {
                    bot.sendNotice(sender, tell);
                }
                return;
            }
            bot.sendNotice(sender, "No messages for you :<");
            return;
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
            bot.sendNotice(sender, String.format("Tell added for %s", nick));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! use %stell <nick> <message> or %stell list", foxbot.getConfig().getCommandPrefix(), foxbot.getConfig().getCommandPrefix()));
    }
}
