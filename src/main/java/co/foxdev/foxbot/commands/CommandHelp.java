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

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandHelp extends Command
{
    private final FoxBot foxbot;

    /**
     * Sends information about the bot to the command sender.
     * <p/>
     * Usage: .help
     */
    public CommandHelp(FoxBot foxbot)
    {
        super("help", "command.help");
        this.foxbot = foxbot;
    }

    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (foxbot.getConfig().getHelpLines() == null || foxbot.getConfig().getHelpLines().size() == 0)
        {
            sender.send().notice("FoxBot is an IRC bot written by FoxDev* ( https://github.com/FoxDev/ )");
            sender.send().notice(String.format("This particular instance of the bot is owned and operated by %s**", foxbot.getConfig().getBotOwner()));
            sender.send().notice("*FoxDev have no affiliation with this instance of FoxBot, unless explicitly stated by a member of FoxDev.");
            sender.send().notice("*A list of these people can be found here: https://github.com/FoxDev?tab=members");
            sender.send().notice("**This may not be accurate, as it relies on the owner adding their name to the config as a bot owner.");
            return;
        }
        for (String line : foxbot.getConfig().getHelpLines())
        {
            sender.send().notice(line);
        }
    }
}
