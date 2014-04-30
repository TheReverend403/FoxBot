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

public class CommandKill extends Command
{

    private final FoxBot foxbot;

	/**
	 * Parts all channels and then shuts the bot down.
	 *
	 * Usage: .kill
	 */
    public CommandKill(FoxBot foxbot)
    {
        super("kill", "command.kill");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 0)
        {
            for (Channel channel : foxbot.bot().getUserBot().getChannels())
            {
                channel.send().part("Killed by " + sender.getNick());
            }

	        foxbot.shutdown();
            return;
        }
	    sender.send().notice(String.format("Wrong number of args! Use %skill", foxbot.getConfig().getCommandPrefix()));
    }
}