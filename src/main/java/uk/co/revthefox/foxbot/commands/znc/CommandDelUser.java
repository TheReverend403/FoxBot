package uk.co.revthefox.foxbot.commands.znc;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

public class CommandDelUser extends Command
{
    public FoxBot foxbot;

    public CommandDelUser(FoxBot foxbot)
    {
        super("zncdeluser", "command.znc.deluser");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1)
        {
            String user = args[0];

            foxbot.sendMessage("*controlpanel", String.format("deluser %s", user));
            foxbot.sendNotice(sender, String.format("%s has been deleted!", user));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncdeluser <name>", foxbot.getConfig().getCommandPrefix()));
    }
}
