package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;

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
                Socket socket = null;

                for (String url : foxbot.getConfig().getUrlsToPing())
                {
                    try
                    {
                        socket = new Socket(InetAddress.getByName(url), 80);
                        socket.setSoTimeout(foxbot.getConfig().getTimeout());
                        socket.close();

                        for (String user : foxbot.getConfig().getUsersToAlert())
                        {
                            if (checkedHosts.contains(url))
                            {
                                foxbot.getUser(user).sendMessage(foxbot.getUtils().colourise(String.format("&2ALERT:&r %s appears to be back up!", url)));
                            }
                        }
                        checkedHosts.remove(url);
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
