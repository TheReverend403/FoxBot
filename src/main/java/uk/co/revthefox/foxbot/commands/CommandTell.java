package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandTell extends Command
{
    private FoxBot foxbot;

    public CommandTell(FoxBot foxbot)
    {
        super("tell", "command.tell");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length > 2)
        {
            String nick = args[0];

            StringBuilder message = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                message.append(" ").append(args[arg]);
            }
            foxbot.getDatabase().addTell(nick, message.toString());
            foxbot.getBot().sendNotice(sender, String .format("Tell added for %s", args[0]));
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %stell <nick> <message>", foxbot.getConfig().getCommandPrefix()));
    }
}
