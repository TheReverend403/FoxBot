/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotLogger
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
    private static FileWriter fw;
    private static BufferedWriter bw;
    private static File logFile = new File("foxbot.log");

    static
    {
        try
        {
            if (!logFile.exists())
            {
                if (!logFile.createNewFile())
                {
                    System.out.println(String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), LogLevel.SEVERE, "Couldn't create logfile. Shutting down."));
                }
            }

            fw = new FileWriter(logFile);
        }
        catch (IOException ex)
        {
            System.out.println(String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), LogLevel.SEVERE, "Error occurred while opening logfile. Shutting down."));
            ex.printStackTrace();
        }
    }

    public static void log(LogLevel logLevel, String message)
    {
        System.out.println(String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), logLevel, message));

        try
        {
            bw = new BufferedWriter(fw);
            bw.write(String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), logLevel, message));
            bw.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}