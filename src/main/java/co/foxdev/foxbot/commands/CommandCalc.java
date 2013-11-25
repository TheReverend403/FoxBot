package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import javax.script.ScriptException;

public class CommandCalc extends Command
{
    private final FoxBot foxbot;

    public CommandCalc(FoxBot foxbot)
    {
        super("calc", "command.calc", "math");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 0)
        {
            StringBuilder expression = new StringBuilder(args[0]);
            String result = null;

            for (int i = 1; i < args.length; i++)
            {
                expression.append(" ").append(args[i]);
            }

            try
            {
                result = foxbot.getScriptEngine().eval(expression.toString()).toString();
            }
            catch (ScriptException ex)
            {
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &cNot a valid expression!", foxbot.getUtils().munge(sender.getNick()))));
                return;
            }
            channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &2Result for %s:&r %s", foxbot.getUtils().munge(sender.getNick()), expression.toString().replace(" ", ""), result)));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %scalc <expression>", foxbot.getConfig().getCommandPrefix()));
    }
}
