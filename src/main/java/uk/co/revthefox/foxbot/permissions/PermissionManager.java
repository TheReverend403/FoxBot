package uk.co.revthefox.foxbot.permissions;

import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.HashMap;
import java.util.List;

public class PermissionManager
{
    private FoxBot foxbot;

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public Boolean userHasPermission(User user, String permission)
    {
        if (foxbot.getConfig().getUsersMustBeLoggedIn() && (!user.isVerified()))
        {
            foxbot.getBot().sendNotice(user, "You are not logged in. Please log in to nickserv before performing any bot commands.");
            return false;
        }
        if (foxbot.getPermissionsFile().getStringList("permissions." + user.getRealName()).isEmpty()
                && (!foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission)))
        {
            return false;
        }
        if (foxbot.getPermissionsFile().getStringList("permissions." + user.getRealName()).contains("-" + permission))
        {
            return false;
        }
        if (foxbot.getPermissionsFile().getStringList("permissions." + user.getRealName()).contains(permission))
        {
            return true;
        }
        return true;
    }
}
