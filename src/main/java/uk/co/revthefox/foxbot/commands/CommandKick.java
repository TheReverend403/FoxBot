package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandKick extends Command
{
    private FoxBot foxbot;

    public CommandKick(FoxBot foxbot)
    {
        super("kick", "command.kick");
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
            if (bot.getUser(args[0]) == null)
            {
                bot.sendNotice(sender, "That user is not in this channel");
                return;
            }
            if (foxbot.getPermissionManager().userHasQuietPermission(bot.getUser(args[0]), "protection.kick"))
            {
                bot.sendNotice(sender, "You cannot kick that user!");
                return;
            }
            if (args.length > 1)
            {
                StringBuilder reason = new StringBuilder(args[1]);

                for (int arg = 2; arg < args.length; arg++)
                {
                    reason.append(" ").append(args[arg]);
                }

                bot.kick(channel, bot.getUser(args[0]), reason.toString());
                return;
            }
            bot.kick(channel, bot.getUser(args[0]));
            return;
        }
        bot.sendNotice(sender, String.format("Wrong number of args! use %skick <nick> [reason]", foxbot.getConfig().getCommandPrefix()));
    }
}
