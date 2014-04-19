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
import co.foxdev.foxbot.FoxBot;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandExec extends Command
{
    private static FoxBot foxbot;
    private Interpreter interpreter;

    public CommandExec(FoxBot foxbot)
    {
        super("exec", "command.exec");
        CommandExec.foxbot = foxbot;

	    try
	    {
		    interpreter = new Interpreter();
		    interpreter.getNameSpace().doSuperImport();
		    interpreter.setStrictJava(false);
		    interpreter.set("foxbot", foxbot);
	    }
	    catch (Exception ex)
	    {
		    foxbot.log(ex);
	    }
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        try
        {
            interpreter.set("sender", sender);
            interpreter.set("channel", channel);
            interpreter.set("event", event);
            interpreter.eval(StringUtils.join(args, " ").trim());
        }
        catch (EvalError ex)
        {
	        foxbot.log(ex);
            channel.send().message(ex.getLocalizedMessage());
        }
    }
}