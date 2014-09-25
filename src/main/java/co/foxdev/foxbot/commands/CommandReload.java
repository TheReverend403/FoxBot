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

public class CommandReload extends Command
{
    private final FoxBot foxbot;

    /**
     * Reloads the bot's configuration, permissions and ZNC config.
     * <p/>
     * Usage: .reload
     */
    public CommandReload(FoxBot foxbot)
    {
        super("reload", "command.reload");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        sender.send().notice("Reloading...");
        foxbot.getConfig().reload();
        sender.send().notice("Reloaded! Some options will only take effect when you restart the bot.");
    }
}