package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
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
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        PircBotX bot = foxbot.getBot();

        if (args.length == 0)
        {
            bot.sendNotice(sender, "Reloading...");
            foxbot.getConfig().reload();
            bot.sendNotice(sender, "Reloaded! Some options will only take effect when you restart the bot.");
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! use %sreload", foxbot.getConfig().getCommandPrefix()));
    }
}
