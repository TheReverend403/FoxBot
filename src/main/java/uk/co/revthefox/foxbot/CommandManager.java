package uk.co.revthefox.foxbot;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.commands.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandManager
{

    private static final Pattern argsSplit = Pattern.compile(" ");

    private FoxBot foxbot;

    private final Map<String, Command> commandMap = new HashMap<>();

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
        String[] split = argsSplit.split(commandLine);
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
                foxbot.getBot().sendNotice(sender, "You do not have permission to do that!");
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
            foxbot.getBot().sendNotice(sender, "An internal error occurred whilst executing this command, please alert a bot admin.");
            System.out.println("Error in dispatching command: " + command.getName());
            ex.printStackTrace();
        }
        return true;
    }
}