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
        if (foxbot.getConfig().getUsersMustMatchHostmask() && (!verifyUser(user)))
        {
            foxbot.getBot().sendNotice(user, "Your hostmask does not match the one in the bot's permission file.");
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

    public Boolean verifyUser(User user)
    {
        if (foxbot.getPermissionsFile().getString(user.getRealName() + ".hostmask").equals(user.getHostmask()))
        {
            return true;
        }
        return false;
    }
}
