package co.foxdev.foxbot.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotLogger
{
    public static void log(LogLevel logLevel, String message)
    {
        System.out.println(String.format("%s [%s] %s", new SimpleDateFormat("[HH:mm:ss]").format(Calendar.getInstance().getTimeInMillis()), logLevel, message));
    }
}