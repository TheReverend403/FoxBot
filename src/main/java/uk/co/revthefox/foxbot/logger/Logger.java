package uk.co.revthefox.foxbot.logger;

import org.pircbotx.hooks.ListenerAdapter;
import uk.co.revthefox.foxbot.FoxBot;

public class Logger extends ListenerAdapter
{
    private FoxBot foxbot;

    public Logger(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }
}
