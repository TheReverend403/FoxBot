package co.foxdev.foxbot.utils;

import co.foxdev.foxbot.FoxBot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

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
        int period = 30000;

        Timer timer = new Timer();

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                Socket socket = null;

                try
                {
                    socket = new Socket(InetAddress.getByName("cookieslap.net"), 17647);
                    socket.setSoTimeout(10);
                    socket.close();
                }
                catch (UnknownHostException ignored)
                {
                }
                catch (IOException ex)
                {
                    foxbot.getUser("TheReverend403").sendMessage("cookieslap.net is kill, fix pls");
                }
            }
        };
        timer.scheduleAtFixedRate(task, initialDelay, period);
    }
}
