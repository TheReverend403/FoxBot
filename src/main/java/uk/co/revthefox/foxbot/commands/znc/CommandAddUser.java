package uk.co.revthefox.foxbot.commands.znc;

import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

public class CommandAddUser extends Command
{
    public FoxBot foxbot;

    public CommandAddUser(FoxBot foxbot)
    {
        super("zncadduser", "command.znc.adduser");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 2)
        {
            String user = args[0];
            String password = RandomStringUtils.randomAlphanumeric(6);
            String bindhost = args[1];

            foxbot.sendMessage("*controlpanel", String.format("adduser %s %s", user, password));
            foxbot.sendMessage("*controlpanel", String.format("set bindhost %s %s", user, bindhost));
            foxbot.sendMessage(sender, String.format("User added! Password is: %s", password));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncadduser <name> <bindhost>", foxbot.getConfig().getCommandPrefix()));
    }
}
