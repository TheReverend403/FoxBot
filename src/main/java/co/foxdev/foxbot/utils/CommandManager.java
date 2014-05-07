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

package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.commands.Command;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class CommandManager
{
    private final FoxBot foxbot;

    private static final Pattern ARGS_SPLIT = Pattern.compile(" ");
    private static final Pattern LINES_SPLIT = Pattern.compile("\\\\n");
    private final Map<String, Command> commandMap = new HashMap<>();
	private final char[] restrictedChars = new char[]{'.', '/', '\\', '~'};

    public CommandManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void registerCommand(Command command)
    {
        commandMap.put(command.getName().toLowerCase(), command);

        for (String alias : command.getAliases())
        {
            commandMap.put(alias.toLowerCase(), command);
        }
    }

    public boolean dispatchCommand(MessageEvent event, String commandLine)
    {
        String[] split = ARGS_SPLIT.split(commandLine);
        User sender = event.getUser();
        String commandName = split[0].toLowerCase();
        Command command = commandMap.get(commandName);

	    if (runCustomCommand(event.getChannel().getName(), commandName))
	    {
		    return true;
	    }

	    if (command == null)
	    {
		    return false;
	    }

        foxbot.log(String.format("Dispatching command '%s' used by %s", command.getName(), sender.getNick()));

        String permission = command.getPermission();

        if (permission != null && !permission.isEmpty())
        {
            if (!foxbot.getPermissionManager().userHasPermission(sender, permission))
            {
	            foxbot.log(Level.WARNING, String.format("Permission denied for command '%s' used by %s", command.getName(), sender.getNick()));
	            sender.send().notice("You do not have permission to do that!");
                return false;
            }
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);

        try
        {
            command.execute(event, args);
        }
        catch (Exception ex)
        {
            sender.send().notice("An internal error occurred whilst executing this command, please alert a bot admin.");
	        foxbot.log("Error dispatching command: " + command.getName());
	        foxbot.log(ex);
        }
        return true;
    }

    private boolean runCustomCommand(String channel, String command)
    {
	    if (command == null || command.isEmpty())
	    {
		    return false;
	    }

	    // Prevent filesystem access
	    if (StringUtils.containsAny(channel, restrictedChars) || StringUtils.containsAny(command, restrictedChars))
	    {
		    return false;
	    }

        File file = new File(String.format("data/custcmds/%s/%s", channel.substring(1), command));
        StringBuilder message = new StringBuilder();

        if (file.exists())
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;

                while ((line = reader.readLine()) != null)
                {
                    message.append(line);
                }
                reader.close();
            }
            catch (IOException ex)
            {
                foxbot.log(ex);
                return false;
            }

            if (!message.toString().isEmpty())
            {
                String[] lines = LINES_SPLIT.split(message.toString());

                for (int i = 0; i < lines.length && i < 3; i++)
                {
	                foxbot.bot().getUserChannelDao().getChannel(channel).send().message(foxbot.getConfig().getCommandPrefix() + command + ": " + lines[i]);
                }
                return true;
            }
        }
        return false;
    }

	public ImmutableList<Command> getCommands()
	{
		return ImmutableList.copyOf(commandMap.values());
	}
}