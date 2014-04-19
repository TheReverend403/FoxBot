/*
 * This file is part of Foxbot.
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandSound extends Command
{
    private final FoxBot foxbot;

    public CommandSound(FoxBot foxbot)
    {
        super("sound", "command.sound", "s");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 1)
        {
            String soundname = args[0];

            channel.send().message(String.format("%s%s.%s", foxbot.getConfig().getSoundURL(), soundname, foxbot.getConfig().getSoundExtension()));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %ssound <soundname>", foxbot.getConfig().getCommandPrefix()));
    }
}
