package uk.co.revthefox.foxbot.permissions;

import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.config.file.FileConfiguration;

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
        String authType = foxbot.getConfig().getMatchUsersByHostmask() ? "\"" + user.getHostmask() + "\"" : user.getNick();
        FileConfiguration permissions = foxbot.getConfig().getBotPermissions();

        if (!authedUsers.contains(user) && user.isVerified())
        {
            authedUsers.add(user);
        }

        return !(foxbot.getConfig().getUsersMustBeVerified()
                && !authedUsers.contains(user)
                && !permissions.getStringList("default").contains(permission))
                && permissions.getString(authType) != null
                && !permissions.getStringList(authType).contains("-" + permission)
                && (permissions.getStringList("default").contains(permission)
                || permissions.getStringList("default").contains("permissions.*")
                || permissions.getStringList(authType).contains(permission)
                || permissions.getStringList(authType).contains("permissions.*"));
    }

    public boolean userHasPermission(User user, String permission)
    {
        String authType = foxbot.getConfig().getMatchUsersByHostmask() ? "\"" + user.getHostmask() + "\"" : user.getNick();
        FileConfiguration permissions = foxbot.getConfig().getBotPermissions();

        if (!authedUsers.contains(user) && user.isVerified())
        {
            authedUsers.add(user);
        }

        if (foxbot.getConfig().getUsersMustBeVerified() && !authedUsers.contains(user))
        {
            foxbot.sendNotice(user, "You must be logged into nickserv to use bot commands.");
            return false;
        }
        return  !(permissions.getStringList("default").contains(permission)
                && permissions.getString(authType) != null
                && !permissions.getStringList(authType).contains("-" + permission)
                && (permissions.getStringList("default").contains(permission)
                || permissions.getStringList("default").contains("permissions.*")
                || permissions.getStringList(authType).contains(permission)
                || permissions.getStringList(authType).contains("permissions.*")));
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
