package uk.co.revthefox.foxbot.utils;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.Console;
import java.util.Scanner;

public class ConsoleReader
{
    private FoxBot foxbot;

    public ConsoleReader(FoxBot foxbot)
    {
        this.foxbot = foxbot;
        readInput();
    }

    public void readInput()
    {
        Console cnsl;
        Scanner scan;

        try
        {
            cnsl = System.console();

            if (cnsl != null)
            {
                System.out.print("> ");

                scan = new Scanner(cnsl.reader());

                while (scan.hasNext())
                {
                    String str = scan.next();
                    System.out.println(str);
                    foxbot.getPluginManager().dispatchCommand(new MessageEvent<FoxBot>(null, null, null, null), str);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
