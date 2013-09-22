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

package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandMute extends Command
{
    private final FoxBot foxbot;

    public CommandMute(FoxBot foxbot)
    {
        super("mute", "command.mute", "quiet");
        this.foxbot = foxbot;
    }

    @Override
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

            // Delay the mute to prevent whois throttling due to the permission check on both users
            try
            {
                Thread.sleep(foxbot.getConfig().getKickDelay());
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            if (foxbot.getPermissionManager().userHasQuietPermission(target, "protection.mute") || args[0].equals(foxbot.getNick()))
            {
                foxbot.sendNotice(sender, "You cannot mute that user!");
                return;
            }

            String hostmask = "*" + target.getHostmask();

            StringBuilder reason = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                reason.append(" ").append(args[arg]);
            }

            long muteTime = System.currentTimeMillis();

            foxbot.setMode(channel, "+q ", hostmask);
            foxbot.getDatabase().addMute(channel, target, reason.toString(), sender, muteTime);

            if (foxbot.getConfig().getUnbanTimer() != 0)
            {
                foxbot.getUtils().scheduleModeRemove(channel, hostmask, "q", foxbot.getConfig().getUnbanTimer());
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %smute <user> <reason>", foxbot.getConfig().getCommandPrefix()));
    }
}