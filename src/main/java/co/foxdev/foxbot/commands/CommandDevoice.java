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

import co.foxdev.foxbot.FoxBot;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandDevoice extends Command
{
    private final FoxBot foxbot;

    /**
     * Devoices a user in the current channel (if voiced).
     * If no user is specified, the command sender will be devoiced.
     * Can specify multiple users.
     * <p/>
     * Usage: .devoice [user]
     */
    public CommandDevoice(FoxBot foxbot)
    {
        super("devoice", "command.devoice");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();

        if (args.length > 0)
        {
            if (foxbot.getPermissionManager().userHasQuietPermission(sender, "command.devoice.others"))
            {
                for (String target : args)
                {
                    User toDeVoice = foxbot.bot().getUserChannelDao().getUser(target);

                    if (channel.hasVoice(toDeVoice))
                    {
                        channel.send().deVoice(toDeVoice);
                    }
                }

                return;
            }

            sender.send().notice("You do not have permission to devoice other users!");
            return;
        }

        if (channel.hasVoice(sender))
        {
            channel.send().deVoice(sender);
            return;
        }

        sender.send().notice("You are not voice!");
    }
}