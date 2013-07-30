package uk.co.revthefox.foxbot.commands;

public abstract class Command
{

    private final String name;
    private final String[] aliases;

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public Command(String name)
    {
        this( name, null );
    }

    /**
     * Construct a new command.
     *
     * @param name primary name of this command
     * null or empty string allows it to be executed by everyone
     * @param aliases aliases which map back to this command
     */
    public Command(String name, String... aliases)
    {
        this.name = name;
        this.aliases = aliases;
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args arguments used to invoke this command
     */
    public abstract void execute(CommandSender sender, String[] args);
}
