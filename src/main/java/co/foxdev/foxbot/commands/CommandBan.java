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

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

public class CommandBan extends Command
{
    private final FoxBot foxbot;

    public CommandBan(FoxBot foxbot)
    {
        super("ban", "command.ban");
        this.foxbot = foxbot;
    }

    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 1)
        {
            User target = foxbot.getUser(args[0]);

            if (!channel.getUsers().contains(target))
            {
                foxbot.sendNotice(sender, "That user is not in this channel!");
                return;
            }

            // Please don't throttle me ;_;
            try
            {
                Thread.sleep(foxbot.getConfig().getKickDelay());
            }
            catch (InterruptedException ex)
            {
	            foxbot.log(ex);
            }

            if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.ban") || args[0].equals(foxbot.getNick()))
            {
                foxbot.sendNotice(sender, "You cannot ban that user!");
                return;
            }

            String hostmask = "*!*@*" + target.getHostmask();

            StringBuilder reason = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                reason.append(" ").append(args[arg]);
            }

            foxbot.kick(channel, target, String.format("Ban requested by %s - %s", sender.getNick(), Utils.colourise(reason.toString()) + Colors.NORMAL));

            long banTime = System.currentTimeMillis();

            foxbot.ban(channel, hostmask);
            foxbot.getDatabase().addBan(channel, target, reason.toString(), sender, banTime);

            if (foxbot.getConfig().getUnbanTimer() != 0)
            {
                Utils.scheduleUnban(channel, hostmask, foxbot.getConfig().getUnbanTimer());
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sban <user> <reason>", foxbot.getConfig().getCommandPrefix()));
    }
}