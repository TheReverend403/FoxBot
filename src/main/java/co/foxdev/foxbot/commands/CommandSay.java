/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandSay extends Command
{
    private final FoxBot foxbot;

    /**
     * Makes the bot send a message to a specified channel.
     * If the bot is not in the channel, it will join it, say the message, then leave.
     * The bot will stay in the channel if the -s flag is used.
     * If no channel is specified, the current channel will be used.
     * <p/>
     * Usage: .say [channel] <message> [-s]
     */
    public CommandSay(FoxBot foxbot)
    {
        super("say", "command.say");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 0 && !(args[0].startsWith("#") && args.length == 1))
        {
            StringBuilder message;

            if (args[0].startsWith("#"))
            {
                Channel target = foxbot.bot().getUserChannelDao().getChannel(args[0]);
                message = new StringBuilder(args[1]);

                for (int arg = 2; arg < args.length; arg++)
                {
                    if (!args[arg].equalsIgnoreCase("-s"))
                    {
                        message.append(" ").append(args[arg]);
                    }
                }

                if (target.isInviteOnly())
                {
                    sender.send().notice(String.format("%s is invite only!", args[0]));
                    return;
                }

                foxbot.bot().sendIRC().joinChannel(target.getName());

                if (!args[args.length - 1].equalsIgnoreCase("-s"))
                {
                    sender.send().message(Utils.colourise(message.toString()));
                    target.send().part();
                    sender.send().notice(String.format("Message sent to %s, and channel has been left", args[0]));
                    return;
                }
                target.send().message(Utils.colourise(message.toString()));
                sender.send().notice(String.format("Message sent to %s", args[0]));
                return;
            }

            message = new StringBuilder(args[0]);

            for (int arg = 1; arg < args.length; arg++)
            {
                if (!args[arg].equalsIgnoreCase("-s"))
                {
                    message.append(" ").append(args[arg]);
                }
            }
            channel.send().message(Utils.colourise(message.toString()));
            return;
        }
        sender.send().notice(String.format("Wrong number of args! Use %ssay [#channel] <message> [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}