/*
 * This file is part of Foxbot.
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandSetCmd extends Command
{
    private final FoxBot foxbot;

	/**
	 * Sets a custom command to be used in the current channel.
	 * The command can contain any text and colours and will return that text when the command is used.
	 * The command can be deleted by setting a command with no message.
	 *
	 * Usage: .setcmd <name> [message]
	 */
    public CommandSetCmd(FoxBot foxbot)
    {
        // The only command without a perm check
        super("setcmd");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 0)
        {
            // Ops should be able to add custom commands for their own channels.
            if (!channel.getNormalUsers().contains(sender) && !channel.hasVoice(sender))
            {
	            StringBuilder builder = new StringBuilder("");

                String command = args[0];

	            if (args.length > 1)
	            {
		            builder = new StringBuilder(args[1]);

		            for (int arg = 2; arg < args.length; arg++)
		            {
			            builder.append(" ").append(args[arg]);
		            }
	            }
	            sender.send().notice(String.format("Command '%s' %s for %s", command, Utils.addCustomCommand(channel.getName(), command, builder.toString()) ? "set" : "deleted" , channel.getName()));
                return;
            }
	        sender.send().notice(String.format("Only channel half-ops and above can set custom commands!"));
            return;
        }
	    sender.send().notice(String.format("Wrong number of args! Use %ssetcmd <command> [text]", foxbot.getConfig().getCommandPrefix()));
    }
}
