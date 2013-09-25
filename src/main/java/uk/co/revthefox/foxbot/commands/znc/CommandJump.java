package uk.co.revthefox.foxbot.commands.znc;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

public class CommandJump extends Command
{
    public FoxBot foxbot;

    public CommandJump(FoxBot foxbot)
    {
        super("zncreconnect", "command.znc.reconnect", "zncjump");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1 || args.length == 2)
        {
            String user = args[0];
            String network = args.length == 2 ? args[1] : "Esper";

            foxbot.sendMessage("*controlpanel", String.format("reconnect %s %s", user, network));
            foxbot.sendNotice(sender, String.format("%s is reconnecting!", user));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncreconnect <name> [network]", foxbot.getConfig().getCommandPrefix()));
    }
}
