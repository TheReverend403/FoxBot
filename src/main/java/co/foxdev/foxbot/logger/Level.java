package co.foxdev.foxbot.logger;

public enum Level
{
    INFO("[INFO]"),
    WARNING("[WARNING]"),
    SEVERE("[SEVERE]");

    private Level(final String message)
    {
        this.message = message;
    }

    private final String message;
}