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

import com.google.common.collect.ImmutableList;
import org.pircbotx.hooks.events.MessageEvent;

public abstract class Command
{
    private final String name;
    private final String permission;
    private final String[] aliases;

    public Command(String name)
    {
        this(name, null);
    }

    public Command(String name, String permission, String... aliases)
    {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public String getName()
    {
        return name;
    }

    public String getPermission()
    {
        return permission;
    }

    public ImmutableList<String> getAliases()
    {
		return ImmutableList.copyOf(aliases);
    }

    public abstract void execute(final MessageEvent event, final String[] args);
}