package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

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
                String uptime = new Scanner(new FileInputStream("/proc/uptime")).next();
                Double unixTime = Double.valueOf(uptime) / 60 / 60 / 24;
                channel.sendMessage(String.format("%sSystem uptime: %s%s days", Colors.GREEN, Colors.NORMAL, roundTwoDecimals(unixTime)));
            }
            catch (FileNotFoundException ex)
            {
                foxbot.getBot().sendNotice(sender, "File \"/proc/uptime\" not found!");
                return;
            }
            return;
        }
        foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %suptime",
                foxbot.getConfig().getCommandPrefix()));
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}
