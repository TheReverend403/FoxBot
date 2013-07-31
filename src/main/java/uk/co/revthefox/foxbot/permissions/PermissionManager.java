package uk.co.revthefox.foxbot.permissions;

import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.HashMap;
import java.util.List;

public class PermissionManager
{
    private FoxBot foxbot;

    private HashMap<User, List<String>> permissionsList = new HashMap<User, List<String>>();

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }
}
