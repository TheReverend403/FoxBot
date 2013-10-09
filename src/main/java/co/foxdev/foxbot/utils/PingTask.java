package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PingTask
{
    private FoxBot foxbot;

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
                    }
                    catch (UnknownHostException ignored)
                    {
                    }
                    catch (IOException ex)
                    {
                        for (String user : foxbot.getConfig().getUsersToAlert())
                        {
                            foxbot.getUser(user).sendMessage(foxbot.getUtils().colourise(String.format("&4ALERT:&r %s appears to be down!", url)));
                        }
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, initialDelay, TimeUnit.SECONDS.toMillis(period));
    }
}
