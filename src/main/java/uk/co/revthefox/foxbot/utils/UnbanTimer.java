package uk.co.revthefox.foxbot.utils;

import uk.co.revthefox.foxbot.FoxBot;

import java.util.TimerTask;

public class UnbanTimer extends TimerTask
{
    private FoxBot foxbot;

    public UnbanTimer(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    @Override
    public void run()
    {
    }
}
