package uk.co.revthefox.foxbot.commands;

import org.pircbotx.hooks.events.MessageEvent;

public abstract class Command
{

    private final String name;
    private final String permission;
    private final String[] aliases;

    public Command(String name)
    {
        this(name, null);
    }

    public Command(String name, String permission, String... aliases)
    {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    public String[] getAliases()
    {
        return aliases;
    }

    public abstract void execute(final MessageEvent event, final String[] args);
}