package uk.co.revthefox.foxbot;

import org.pircbotx.Channel;
import org.pircbotx.User;
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

    public boolean dispatchCommand(User sender, Channel channel, String commandLine)
    {
        channel.sendMessage("Attempting to dispatch command...");

        channel.sendMessage("Separating args from main command...");
        String[] split = argsSplit.split(commandLine);
        channel.sendMessage("Getting command name...");
        String commandName = split[0].toLowerCase();

        Command command = commandMap.get(commandName);
        channel.sendMessage("Command is: " + commandName);


        if (command == null)
        {
            return false;
        }

        channel.sendMessage("Checking permissions...");
        String permission = command.getPermission();
        if (permission != null && !permission.isEmpty())
        {
            if (!foxbot.getPermissionManager().userHasPermission(sender, permission))
            {
                channel.sendMessage("Permission check result: false (This is a good thing)");
                //foxbot.getBot().sendNotice(sender, "You do not have permission to do that!");
                return false;
            }
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);

        for (String arg : args)
        {
            channel.sendMessage("Arg: " + arg);
        }
        try
        {
            channel.sendMessage("Executing command...");
            command.execute(sender, channel, args);
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
