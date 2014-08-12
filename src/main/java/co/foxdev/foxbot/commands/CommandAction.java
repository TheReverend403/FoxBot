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
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandAction extends Command
{
    private final FoxBot foxbot;

    /**
     * Makes the bot send an action to a specified channel.
     * If no channel is specified, the current channel will be used.
     * <p/>
     * Usage: .action [channel] <message>
     */
    public CommandAction(FoxBot foxbot)
    {
        super("action", "command.action");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 0)
        {
            if (args[0].startsWith("#") && args.length > 1)
            {
                String targetChan = args[0];
                channel = foxbot.bot().getUserChannelDao().getChannel(targetChan);

                if (!foxbot.bot().getUserBot().getChannels().contains(channel))
                {
                    sender.send().notice("I'm not in " + channel .getName());
                    return;
                }

                StringBuilder action = new StringBuilder(args[1]);

                for (int arg = 2; arg < args.length; arg++)
                {
                    action.append(" ").append(args[arg]);
                }

                channel.send().action(action.toString());
                sender.send().notice(String.format("Action sent to %s", targetChan));
                return;
            }

            channel.send().action(StringUtils.join(args, " "));
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %saction [#channel] <action>", foxbot.getConfig().getCommandPrefix()));
    }
}