package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.utils.Utils;

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
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 0)
        {
            if (!System.getProperty("os.name").toLowerCase().contains("win"))
            {
                try
                {
                    int unixTime = Integer.valueOf(new Scanner(new FileInputStream("/proc/uptime")).next().replaceAll("\\.[0-9]+", ""));
                    int day = (int) TimeUnit.SECONDS.toDays(unixTime);
                    long hours = TimeUnit.SECONDS.toHours(unixTime) - (day * 24);
                    long minute = TimeUnit.SECONDS.toMinutes(unixTime) - (TimeUnit.SECONDS.toHours(unixTime) * 60);
                    long seconds = TimeUnit.SECONDS.toSeconds(unixTime) - (TimeUnit.SECONDS.toMinutes(unixTime) * 60);
                    channel.sendMessage(foxbot.getUtils().colourise(String.format("&aSystem uptime: &r%s days %s hours %s minutes %s seconds", day, hours, minute, seconds)));
                }
                catch (FileNotFoundException ex)
                {
                    foxbot.sendNotice(sender, "File \"/proc/uptime\" not found. Are you sure you're using Linux?");
                }
                return;
            }
            foxbot.sendNotice(sender, "This command is only supported on Unix based systems.");
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %suptime", foxbot.getConfig().getCommandPrefix()));
    }
}