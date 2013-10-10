package co.foxdev.foxbot.logger;

public enum LogLevel
{
    INFO("[INFO]"),
    WARNING("[WARNING]"),
    SEVERE("[SEVERE]");

    private LogLevel(final String message)
    {
        this.message = message;
    }

    private final String message;
}