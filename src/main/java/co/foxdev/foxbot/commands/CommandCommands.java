package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandCommands extends Command
{
	private final FoxBot foxbot;

	public CommandCommands(FoxBot foxbot)
	{
		super("commands", "command.commands");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(final MessageEvent event, final String[] args)
	{
		User sender = event.getUser();

		StringBuilder sb = new StringBuilder();

		for (Command command : foxbot.getPluginManager().getCommands())
		{
			if (!sb.toString().contains(command.getName()) && foxbot.getPermissionManager().userHasQuietPermission(sender, command.getPermission()))
			{
				sb.append(command.getName()).append(", ");
			}
		}

		String cmdList = sb.toString();
		foxbot.sendNotice(sender, cmdList.substring(0, cmdList.lastIndexOf(",")));
	}
}