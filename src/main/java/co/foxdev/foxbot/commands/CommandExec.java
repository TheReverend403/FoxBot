/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.commands;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.UtilEvalError;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandExec extends Command
{
    private static FoxBot foxbot;
    private static Interpreter interpreter;

    static
    {
        try
        {
            interpreter = new Interpreter();
            interpreter.getNameSpace().doSuperImport();
            interpreter.setStrictJava(false);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CommandExec(FoxBot foxbot)
    {
        super("exec", "command.exec");
        CommandExec.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        try
        {
            interpreter.getNameSpace().doSuperImport();
            interpreter.set("sender", sender);
            interpreter.set("channel", channel);
            interpreter.set("event", event);
            interpreter.set("foxbot", foxbot);
            interpreter.eval(StringUtils.join(args, " ").trim());
        }
        catch (EvalError | UtilEvalError ex)
        {
	        foxbot.log(ex);
            channel.sendMessage(ex.getLocalizedMessage());
        }
    }
}