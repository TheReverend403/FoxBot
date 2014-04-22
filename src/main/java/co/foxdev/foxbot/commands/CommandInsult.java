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

import java.util.regex.Pattern;

public class CommandInsult extends Command
{
    private final FoxBot foxbot;

	/**
	 * Generates an insult from http://www.pangloss.com/seidel/Shaker and sends it to the specified channel.
	 * If the bot is not in the channel, it will join it, send the message, then leave.
	 * The bot will stay in the channel if the -s flag is used.
	 * If no channel is specified, the current channel will be used.
	 *
	 * Usage: .insult [channel] [-s]
	 */
    public CommandInsult(FoxBot foxbot)
    {
        super("insult", "command.insult");
        this.foxbot = foxbot;
    }

    private Pattern titlePattern = Pattern.compile(".*?<font.*?>(.*)</font>.*?", Pattern.DOTALL);

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length < 3)
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
	            foxbot.log(ex);
                foxbot.sendMessage(channel, String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick())));
                return;
            }

            if (args.length > 0)
            {
                if (args[0].startsWith("#"))
                {
	                Channel chan = foxbot.getChannel(args[0]);

                    if (chan.isInviteOnly())
                    {
                        foxbot.sendNotice(sender, String.format("%s is invite only!", args[0]));
                        return;
                    }

                    foxbot.joinChannel(chan);

                    if (!args[args.length - 1].equalsIgnoreCase("-s"))
                    {
                        foxbot.sendMessage(chan, insult);
                        foxbot.partChannel(foxbot.getChannel(args[0]));
                        foxbot.sendNotice(sender, String.format("Insult sent to %s, and channel has been left", args[0]));
                        return;
                    }
                    foxbot.sendMessage(chan, insult);
                    foxbot.sendNotice(sender, String.format("Insult sent to %s", args[0]));
                    return;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", args[0]));
                return;
            }
            channel.send().message(insult);
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sinsult [#channel] [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}