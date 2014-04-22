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
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandJoin extends Command
{
    private final FoxBot foxbot;

	/**
	 * Makes the bot join a channel, or a list of channels.
	 * The bot follows the same rules as a user, so it cannot join an invite only channel, or a channel it is banned in.
	 *
	 * Usage: .join <channel> [channels]
	 */
    public CommandJoin(FoxBot foxbot)
    {
        super("join", "command.join");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length != 0)
        {
            for (String chan : args)
            {
                if (chan.startsWith("#"))
                {
	                Channel channel = foxbot.getChannel(chan);

                    if (!channel.isInviteOnly())
                    {
                        foxbot.joinChannel(channel);
                        foxbot.sendNotice(sender, String.format("Joined %s", chan));
                        continue;
                    }
                    foxbot.sendNotice(sender, String.format("%s is invite only!", chan));
                    continue;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", chan));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sjoin <#channel> [#channel2 #channel3 ... ]", foxbot.getConfig().getCommandPrefix()));
    }
}