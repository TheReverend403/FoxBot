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

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

public class CommandDelay extends Command
{
    private final FoxBot foxbot;

    public CommandDelay(FoxBot foxbot)
    {
        super("delay", "command.delay");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 1)
        {
            try
            {
                foxbot.setMessageDelay(Long.valueOf(args[0]));
                foxbot.sendNotice(sender, String.format("Message delay set to %sms", foxbot.getMessageDelay()));
            }
            catch (NumberFormatException ex)
            {
                foxbot.sendNotice(sender, "That is not a number!");
                foxbot.setMessageDelay(foxbot.getConfig().getMessageDelay());
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sdelay <number of milliseconds>", foxbot.getConfig().getCommandPrefix()));
    }
}