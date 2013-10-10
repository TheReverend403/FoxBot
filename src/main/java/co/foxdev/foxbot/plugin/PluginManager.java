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
import co.foxdev.foxbot.logger.LogLevel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.commands.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PluginManager
{
    private final FoxBot foxbot;

    private static final Pattern ARGS_SPLIT = Pattern.compile(" ");
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

        if (command == null)
        {
            return false;
        }

        BotLogger.log(LogLevel.INFO, String.format("Dispatching command %s used by %s", command.getName(), sender.getNick()));

        String permission = command.getPermission();

        if (permission != null && !permission.isEmpty())
        {
            if (!foxbot.getPermissionManager().userHasPermission(sender, permission))
            {
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
}