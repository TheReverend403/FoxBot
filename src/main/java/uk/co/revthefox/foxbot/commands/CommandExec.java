package uk.co.revthefox.foxbot.commands;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.UtilEvalError;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandExec extends Command
{
    private static FoxBot foxbot;
    private static Interpreter interpreter;
    static{
        try {
            interpreter = new Interpreter();
            interpreter.getNameSpace().doSuperImport();
            interpreter.setStrictJava(false);
        } catch (Exception ex) {
            Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public CommandExec(FoxBot foxbot)
    {
        super("exec", "command.exec");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        try {
            interpreter.set("sender", sender);
            interpreter.set("channel", channel);            
            interpreter.set("bot", foxbot.getBot());
            interpreter.set("foxbot", foxbot);
            interpreter.eval(StringUtils.join(args, " ").trim());
            
        } catch (EvalError ex) {
            foxbot.getBot().sendMessage(channel, ex.getLocalizedMessage());
            Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
