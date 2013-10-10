package co.foxdev.foxbot.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotLogger
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");

    public static void log(LogLevel logLevel, String message)
    {
        System.out.println(String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), logLevel, message));
    }
}