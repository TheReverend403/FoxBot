package uk.co.revthefox.foxbot.permissions;

import com.typesafe.config.ConfigException;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

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
    public Boolean userHasQuietPermission(User user, String permission)
    {
        String authType = foxbot.getConfig().getMatchUsersByHostmask() ? "\"" + user.getHostmask() + "\"" : user.getNick();

        if (!authedUsers.contains(user) && user.isVerified())
        {
            authedUsers.add(user);
        }

        if (foxbot.getConfig().getUsersMustBeVerified() && !authedUsers.contains(user))
        {
            return false;
        }

        try
        {
            if (foxbot.getPermissionsFile().getStringList("permissions." + authType).contains("-" + permission))
            {
                return false;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission) || foxbot.getPermissionsFile().getStringList("permissions.default").contains("permissions.*"))
            {
                return true;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions." + authType).contains(permission) || foxbot.getPermissionsFile().getStringList("permissions." + authType).contains("permissions.*"))
            {
                return true;
            }
        }
        catch (ConfigException ex)
        {
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean userHasPermission(User user, String permission)
    {
        String authType = foxbot.getConfig().getMatchUsersByHostmask() ? "\"" + user.getHostmask() + "\"" : user.getNick();

        if (!authedUsers.contains(user) && user.isVerified())
        {
            authedUsers.add(user);
        }

        if (foxbot.getConfig().getUsersMustBeVerified() && !authedUsers.contains(user))
        {
            foxbot.getBot().sendNotice(user, "You must be logged into nickserv to use bot commands.");
            return false;
        }

        try
        {
            if (foxbot.getPermissionsFile().getStringList("permissions." + authType).contains("-" + permission))
            {
                return false;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission) || foxbot.getPermissionsFile().getStringList("permissions.default").contains("permissions.*"))
            {
                return true;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions." + authType).contains(permission) || foxbot.getPermissionsFile().getStringList("permissions." + authType).contains("permissions.*"))
            {
                return true;
            }
        }
        catch (ConfigException ex)
        {
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean isNickProtected(String nick)
    {
        return foxbot.getNickProtectionFile().hasPath("protection." + nick) && !foxbot.getBot().getUser(nick).getHostmask().equals(foxbot.getNickProtectionFile().getString("protection." + nick + ".hostmask"));
    }

    public void removeAuthedUser(User user)
    {
        if (authedUsers.contains(user))
        {
            authedUsers.remove(user);
        }
    }
}
