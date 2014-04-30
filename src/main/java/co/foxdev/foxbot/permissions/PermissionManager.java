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

package co.foxdev.foxbot.permissions;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.config.yamlconfig.file.FileConfiguration;
import org.pircbotx.User;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager
{
    private final FoxBot foxbot;
    private List<User> authedUsers = new ArrayList<>();

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    // Exactly the same as userHasPermission(), except this gives no output.
    public boolean userHasQuietPermission(User user, String permission)
    {
	    return checkPerm(user, permission, true);
    }

    public boolean userHasPermission(User user, String permission)
    {
		return checkPerm(user, permission, false);
    }

	private boolean checkPerm(User user, String permission, boolean quiet)
	{
		String authType = foxbot.getConfig().getMatchUsersByHostmask() ? user.getHostmask() : user.getNick();
		FileConfiguration permissions = foxbot.getConfig().getBotPermissions();

		if (foxbot.getConfig().getUsersMustBeVerified())
		{
			if (!authedUsers.contains(user) && user.isVerified())
			{
				authedUsers.add(user);
			}

			if (authedUsers.contains(user))
			{
				if (permissions.getStringList("default").contains(permission))
				{
					return !permissions.getStringList(authType).contains("-" + permission);
				}
				return permissions.getStringList(authType).contains(permission) || permissions.getStringList(authType).contains("*");
			}

			if (!quiet)
			{
				user.send().notice("You must be logged into nickserv to use bot commands.");
			}
			return false;
		}
		if (permissions.getStringList("default").contains(permission))
		{
			return !permissions.getStringList(authType).contains("-" + permission);
		}
		return permissions.getStringList(authType).contains(permission) || permissions.getStringList(authType).contains("*");
	}

    public void removeAuthedUser(User user)
    {
        if (authedUsers.contains(user))
        {
            authedUsers.remove(user);
        }
    }
}