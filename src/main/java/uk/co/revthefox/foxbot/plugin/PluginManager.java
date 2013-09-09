package uk.co.revthefox.foxbot.plugin;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PluginManager
{
    private FoxBot foxbot;

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
            ex.printStackTrace();
        }
        return true;
    }
}