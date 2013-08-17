package uk.co.revthefox.foxbot.commands;


import org.pircbotx.Channel;
import org.pircbotx.User;
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
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            foxbot.getBot().sendNotice(sender, "Reloading...");
            foxbot.getConfig().reload();
            foxbot.getBot().sendNotice(sender, "Reloaded! Some options will only take effect when you restart the bot.");
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %sreload", foxbot.getConfig().getCommandPrefix()));
    }
}
