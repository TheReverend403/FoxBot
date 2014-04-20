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

package co.foxdev.foxbot.database;

import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.List;

public abstract class Database
{
	public abstract void connect();

	public abstract void addTell(String sender, String receiver, String message);

	public abstract List<String> getTells(String user, boolean showAll);

	public abstract void cleanTells(String user);

	public abstract void addBan(Channel channel, User target, String reason, User banner, long time);

	public abstract void addKick(Channel channel, User target, String reason, User kicker, long time);

	public abstract void disconnect();
}