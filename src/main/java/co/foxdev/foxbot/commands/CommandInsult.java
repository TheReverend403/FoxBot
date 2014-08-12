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
import co.foxdev.foxbot.utils.Utils;
import org.jsoup.Jsoup;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandInsult extends Command
{
    private final FoxBot foxbot;

    /**
     * Generates an insult from http://www.pangloss.com/seidel/Shaker and sends it to the specified channel.
     * If no channel is specified, the current channel will be used.
     * <p/>
     * Usage: .insult [channel]
     */
    public CommandInsult(FoxBot foxbot)
    {
        super("insult", "command.insult");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length < 2)
        {
            String insult;

            try
            {
                insult = Jsoup.connect("http://www.pangloss.com/seidel/Shaker/").timeout(500).execute().parse().select("font").first().text()
                         .replace("\n", "")
                         .replaceAll("^\\s", "")
                         .replace("[", "")
                         .replace("]", "");
            }
            catch (Exception ex)
            {
                foxbot.getLogger().error("Error occurred while fetching random insult", ex);
                channel.send().message(String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick())));
                return;
            }

            if (args.length == 1)
            {
                String targetChan;

                if (args[0].startsWith("#"))
                {
                    targetChan = args[0];
                }
                else
                {
                    targetChan = "#" + args[0];
                }

                channel = foxbot.bot().getUserChannelDao().getChannel(targetChan);

                if (!foxbot.bot().getUserBot().getChannels().contains(channel))
                {
                    sender.send().notice("I'm not in " + channel.getName());
                    return;
                }

                channel.send().message(insult);
                sender.send().notice("Insult sent to " + targetChan);
                return;
            }

            channel.send().message(insult);
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %sinsult [#channel]", foxbot.getConfig().getCommandPrefix()));
    }
}