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
            for (String chan : args)
            {
                if (chan.startsWith("#"))
                {
                    if (!bot.getChannel(chan).isInviteOnly())
                    {
                        bot.joinChannel(chan);
                        bot.sendNotice(sender, String.format("Joined %s", chan));
                        continue;
                    }
                    bot.sendNotice(sender, String.format("%s is invite only!", chan));
                    continue;
                }
                bot.sendNotice(sender, String.format("%s is not a channel...", chan));
            }
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ... ]", foxbot.getConfig().getCommandPrefix()));
    }
}
