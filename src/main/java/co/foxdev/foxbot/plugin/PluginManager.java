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

package co.foxdev.foxbot.plugin;

import co.foxdev.foxbot.logger.BotLogger;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.commands.Command;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PluginManager
{
    private final FoxBot foxbot;

    private static final Pattern ARGS_SPLIT = Pattern.compile(" ");
    private static final Pattern LINES_SPLIT = Pattern.compile("\\\\n");
    private final Map<String, Command> commandMap = new HashMap<>();

    public PluginManager(FoxBot foxbot)
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

	    List<Command> matches = new ArrayList<>();

	    if (runCustomCommand(event.getChannel().getName(), commandName))
	    {
		    return true;
	    }

	    for (Command cmd : commandMap.values())
	    {
		    if ((commandName + ".*").matches(cmd.getName()))
		    {
			    matches.add(cmd);
		    }
	    }

	    if (matches.size() > 1)
	    {
		    StringBuilder sb = new StringBuilder();

		    for (int i = 0; i < sb.length(); i++)
		    {
			    sb.append(matches.get(i)).append(", ");
		    }

		    String commandMatches = sb.toString();

		    foxbot.sendNotice(sender, String.format("'%s' matches %s commands: %s", commandName, matches.size(), commandMatches.substring(0, commandMatches.lastIndexOf(","))));
		    return false;
	    }

	    if (matches.size() == 0)
	    {
		    return false;
	    }

	    if (!commandMap.containsKey(commandName))
	    {
		    StringBuilder commandBuilder = new StringBuilder(matches.get(0).getName());

		    for (int i = 1; i < commandBuilder.length(); i++)
		    {
			    commandBuilder.append(split[i]).append(" ");
		    }

		    return dispatchCommand(event, commandBuilder.toString());
	    }

	    if (command == null)
	    {
		    return false;
	    }

        BotLogger.log(Level.INFO, String.format("COMMAND: Dispatching command '%s' used by %s", command.getName(), sender.getNick()));

        String permission = command.getPermission();

        if (permission != null && !permission.isEmpty())
        {
            if (!foxbot.getPermissionManager().userHasPermission(sender, permission))
            {
                BotLogger.log(Level.WARNING, String.format("COMMAND: Permission denied for command '%s' used by %s", command.getName(), sender.getNick()));
                foxbot.sendNotice(sender, "You do not have permission to do that!");
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
            foxbot.sendNotice(sender, "An internal error occurred whilst executing this command, please alert a bot admin.");
            System.out.println("Error in dispatching command: " + command.getName());
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private boolean runCustomCommand(String channel, String command)
    {
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
                ex.printStackTrace();
                return false;
            }

            if (!message.toString().isEmpty())
            {
                String[] lines = LINES_SPLIT.split(message.toString());

                for (int i = 0; i <= 2; i++)
                {
                    foxbot.getChannel(channel).sendMessage(foxbot.getConfig().getCommandPrefix() + command + ": " + lines[i]);
                }
                return true;
            }
        }
        return false;
    }

	public Collection<Command> getCommands()
	{
		return commandMap.values();
	}
}