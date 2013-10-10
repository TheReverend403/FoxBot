package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.logger.BotLogger;
import co.foxdev.foxbot.logger.LogLevel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PingTask
{
    private FoxBot foxbot;
    private List<String> checkedHosts = new ArrayList<>();

    public PingTask(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        this.start();
    }

    public void start()
    {
        int initialDelay = 0;
        int period = foxbot.getConfig().getCheckInterval();

        Timer timer = new Timer();

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                Socket socket;

                BotLogger.log(LogLevel.INFO, "PINGTASK: Beginning ping task");

                for (String url : foxbot.getConfig().getUrlsToPing())
                {
                    BotLogger.log(LogLevel.INFO, String.format("PINGTASK: Attempting to ping %s", url));

                    try
                    {
                        socket = new Socket(InetAddress.getByName(url), 80);
                        socket.setSoTimeout(foxbot.getConfig().getTimeout());
                        socket.close();
                        BotLogger.log(LogLevel.INFO, String.format("PINGTASK: Ping succeeded for %s", url));

                        for (String user : foxbot.getConfig().getUsersToAlert())
                        {
                            if (checkedHosts.contains(url))
                            {
                                BotLogger.log(LogLevel.INFO, String.format("PINGTASK: Ping failed for %s, alerting %s", url, user));
                                foxbot.getUser(user).sendMessage(foxbot.getUtils().colourise(String.format("&2ALERT:&r %s appears to be back up!", url)));
                            }
                        }
                        if (checkedHosts.contains(url))
                        {
                            checkedHosts.remove(url);
                        }
                    }
                    catch (UnknownHostException ex)
                    {
                        ex.printStackTrace();
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();

                        for (String user : foxbot.getConfig().getUsersToAlert())
                        {
                            if (!checkedHosts.contains(url))
                            {
                                BotLogger.log(LogLevel.INFO, String.format("PINGTASK: Ping succeeded for %s, alerting %s", url, user));
                                foxbot.getUser(user).sendMessage(foxbot.getUtils().colourise(String.format("&4ALERT:&r %s appears to be down!", url)));
                            }
                        }
                        if (!checkedHosts.contains(url))
                        {
                            checkedHosts.add(url);
                        }
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, initialDelay, TimeUnit.SECONDS.toMillis(period));
    }
}
