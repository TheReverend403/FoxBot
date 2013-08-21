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

    public Boolean userHasPermission(User user, String permission)
    {
        String userName = user.getNick();

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
            if (foxbot.getPermissionsFile().getStringList("permissions." + userName).contains("-" + permission))
            {
                return false;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission) || foxbot.getPermissionsFile().getStringList("permissions.default").contains("permissions.*"))
            {
                return true;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions." + userName).contains(permission) || foxbot.getPermissionsFile().getStringList("permissions." + userName).contains("permissions.*"))
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

    public void removeAuthedUser(User user)
    {
        if (authedUsers.contains(user))
        {
            authedUsers.remove(user);
        }
    }
}
