package uk.co.revthefox.foxbot.commands;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandReload extends Command
{
    private FoxBot foxbot;

    public CommandReload(FoxBot foxbot)
    {
        super("reload", "command.reload");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 0)
        {
            foxbot.sendNotice(sender, "Reloading...");
            foxbot.getConfig().reload();
            foxbot.sendNotice(sender, "Reloaded! Some options will only take effect when you restart the bot.");
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sreload", foxbot.getConfig().getCommandPrefix()));
    }
}