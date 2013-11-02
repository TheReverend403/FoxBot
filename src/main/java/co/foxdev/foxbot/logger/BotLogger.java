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
import java.util.logging.Level;

public class BotLogger
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");

    public static void log(Level Level, String message)
    {
        String logMessage = String.format("%s [%s] %s", sdf.format(Calendar.getInstance().getTimeInMillis()), Level, message);

        System.out.println(logMessage);

        try
        {
            FileWriter fw = new FileWriter("foxbot.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(logMessage);
            bw.newLine();
            bw.close();
            fw.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}