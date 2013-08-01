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
        if (foxbot.getConfig().getUsersMustBeVerified() && (!user.isVerified()))
        {
            foxbot.getBot().sendNotice(user, "You must be logged into nickserv to use bot commands.");
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
