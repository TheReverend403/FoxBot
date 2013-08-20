package uk.co.revthefox.foxbot.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandJoin extends Command
{
    private FoxBot foxbot;

    public CommandJoin(FoxBot foxbot)
    {
        super("join", "command.join");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        PircBotX bot = foxbot.getBot();

        if (args.length != 0)
        {
            for (int arg = 0; arg < args.length; arg++)
            {
                if (args[arg].startsWith("#"))
                {
                    if (!bot.getChannel(args[arg]).isInviteOnly())
                    {
                        bot.joinChannel(args[arg]);
                        bot.sendNotice(sender, String.format("Joined %s", args[arg]));
                        continue;
                    }
                    bot.sendNotice(sender, String.format("%s is invite only!", args[arg]));
                    continue;
                }
                bot.sendNotice(sender, String.format("%s is not a channel...", args[arg]));
            }
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ...]", foxbot.getConfig().getCommandPrefix()));
    }
}
