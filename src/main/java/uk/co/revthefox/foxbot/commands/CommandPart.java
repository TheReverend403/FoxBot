package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandPart extends Command
{
    private FoxBot foxbot;

    public CommandPart(FoxBot foxbot)
    {
        super("part", "command.part");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(MessageEvent event, String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();
        PircBotX bot = foxbot.getBot();

        if (args.length != 0)
        {
            for (int arg = 0; arg < args.length; arg++)
            {
                if (args[arg].startsWith("#"))
                {
                    bot.partChannel(bot.getChannel(args[arg]), String.format("Part command used by %s", sender.getNick()));
                    bot.sendNotice(sender, String.format("Leaving %s", args[arg]));
                    continue;
                }
                bot.sendNotice(sender, String.format("%s is not a channel...", args[arg]));
            }
            return;
        }
        bot.partChannel(channel);
        bot.sendNotice(sender, String.format("Leaving %s", channel.getName()));
    }
}
