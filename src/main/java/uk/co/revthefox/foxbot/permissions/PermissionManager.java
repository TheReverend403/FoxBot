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

package uk.co.revthefox.foxbot.permissions;

import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.config.yamlconfig.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager
{
    private FoxBot foxbot;
    private List<User> authedUsers = new ArrayList<>();

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    // Exactly the same as userHasPermission(), except this gives no output.
    public boolean userHasQuietPermission(User user, String permission)
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
                return permissions.getStringList(authType).contains(permission);
            }
            return false;
        }

        if (permissions.getStringList("default").contains(permission))
        {
            return !permissions.getStringList(authType).contains("-" + permission);
        }
        return permissions.getStringList(authType).contains(permission);
    }

    public boolean userHasPermission(User user, String permission)
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
                return permissions.getStringList(authType).contains(permission);
            }
            foxbot.sendNotice(user, "You must be logged into nickserv to use bot commands.");
            return false;
        }
        if (permissions.getStringList("default").contains(permission))
        {
            return !permissions.getStringList(authType).contains("-" + permission);
        }
        return permissions.getStringList(authType).contains(permission);
    }

    public boolean isNickProtected(String nick)
    {
        return foxbot.getConfig().getBotNickProtection().getString(nick) != null && !foxbot.getUser(nick).getHostmask().equals(foxbot.getConfig().getBotNickProtection().getString(nick + ".hostmask"));
    }

    public void removeAuthedUser(User user)
    {
        if (authedUsers.contains(user))
        {
            authedUsers.remove(user);
        }
    }
}