package uk.co.revthefox.foxbot.commands;

import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandTodo extends Command
{
    private FoxBot foxbot;

    public CommandTodo(FoxBot foxbot)
    {
        super("todo", "command.todo");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {

    }
}
