package uk.co.revthefox.foxbot.permissions;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.typesafe.config.ConfigException;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PermissionManager
{
    private FoxBot foxbot;

    private LoadingCache<User, String> authedUsers = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build(new CacheLoader<User, String>()
    {
        public String load(User user)
        {
            return user.getHostmask();
        }
    });

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public Boolean userHasPermission(User user, String permission)
    {
        String userName = user.getHostmask();

        if (!authedUsers.asMap().containsKey(user) && user.isVerified())
        {
            authedUsers.asMap().put(user, userName);
        }

        if (foxbot.getConfig().getUsersMustBeVerified() && !authedUsers.asMap().containsKey(user))
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
        if (authedUsers.asMap().containsKey(user))
        {
            authedUsers.asMap().remove(user);
        }
    }
}
