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

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandUptime extends Command
{
    private final FoxBot foxbot;

    /**
     * ONLY WORKS ON UNIX SYSTEMS
     * Gets the system's current uptime and sends it to channel.
     * <p/>
     * Usage: .uptime
     */
    public CommandUptime(FoxBot foxbot)
    {
        super("uptime", "command.uptime");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (!System.getProperty("os.name").toLowerCase().contains("win"))
        {
            try
            {
                int unixTime = Integer.parseInt(new Scanner(new FileInputStream("/proc/uptime")).next().replaceAll("\\.[0-9]+", ""));
                int day = (int) TimeUnit.SECONDS.toDays(unixTime);
                long hours = TimeUnit.SECONDS.toHours(unixTime) - (day * 24);
                long minute = TimeUnit.SECONDS.toMinutes(unixTime) - (TimeUnit.SECONDS.toHours(unixTime) * 60);
                long seconds = TimeUnit.SECONDS.toSeconds(unixTime) - (TimeUnit.SECONDS.toMinutes(unixTime) * 60);
                channel.send().message(Utils.colourise(String.format("&2System uptime: &r%s days %s hours %s minutes %s seconds", day, hours, minute, seconds)));
            }
            catch (FileNotFoundException ex)
            {
                sender.send().notice("File \"/proc/uptime\" not found. Are you sure you're using Linux?");
            }

            return;
        }

        sender.send().notice("This command is only supported on Unix based systems.");
    }
}