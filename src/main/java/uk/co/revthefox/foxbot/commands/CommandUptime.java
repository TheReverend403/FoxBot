package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandUptime extends Command
{
    private FoxBot foxbot;

    public CommandUptime(FoxBot foxbot)
    {
        super("uptime", "command.uptime");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            try
            {
                String uptime = new Scanner(new FileInputStream("/proc/uptime")).next().replaceAll("\\.[0-9]+", "");

                int seconds = Integer.valueOf(uptime);
                int day = (int) TimeUnit.SECONDS.toDays(seconds);
                long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
                long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
                long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
                channel.sendMessage(String.format("%sSystem uptime: %s%s days %s hours %s minutes %s seconds",
                        Colors.GREEN, Colors.NORMAL, day, hours, minute, second));
                return;
            }
            catch (FileNotFoundException ex)
            {
                foxbot.getBot().sendNotice(sender, "File \"/proc/uptime\" not found!");
                return;
            }
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %suptime",
                foxbot.getConfig().getCommandPrefix()));
    }
}
