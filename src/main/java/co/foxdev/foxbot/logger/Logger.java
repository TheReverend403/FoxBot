package co.foxdev.foxbot.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger
{
    public static void log(Level level, String message)
    {
        System.out.println(String.format("%s %s %s", new SimpleDateFormat("[yyyy-MM-dd - HH:mm:ss]").format(Calendar.getInstance().getTimeInMillis()), level.toString(), message));
    }
}
