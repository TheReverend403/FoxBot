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

public class CommandPart extends Command
{
    private final FoxBot foxbot;

	/**
	 * Makes the bot leave a channel, or a list of channels.
	 *
	 * Usage: .part <channel> [channels]
	 */
    public CommandPart(FoxBot foxbot)
    {
        super("part", "command.part", "leave");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length != 0)
        {
            for (String chan : args)
            {
                if (chan.startsWith("#"))
                {
                    foxbot.partChannel(foxbot.getChannel(chan), String.format("Part command used by %s", sender.getNick()));
                    foxbot.sendNotice(sender, String.format("Left %s", chan));
                    continue;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", chan));
            }
            return;
        }
        foxbot.partChannel(channel);
        foxbot.sendNotice(sender, String.format("Left %s", channel.getName()));
    }
}