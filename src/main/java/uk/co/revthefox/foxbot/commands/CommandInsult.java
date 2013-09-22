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

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInsult extends Command
{
    private final FoxBot foxbot;

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
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Matcher matcher;
            String insult;

            try
            {
                insult = asyncHttpClient.prepareGet("http://www.pangloss.com/seidel/Shaker/").execute().get().getResponseBody();
            }
            catch (Exception ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                foxbot.sendMessage(channel, "Something went wrong...");
                return;
            }

            matcher = titlePattern.matcher(insult);

            while (matcher.find())
            {
                insult = matcher.group(1);
            }

            if (args.length > 0)
            {
                if (args[0].startsWith("#"))
                {
                    if (foxbot.getChannel(args[0]).isInviteOnly())
                    {
                        foxbot.sendNotice(sender, String.format("%s is invite only!", args[0]));
                        return;
                    }

                    foxbot.joinChannel(args[0]);

                    if (!args[args.length - 1].equalsIgnoreCase("-s"))
                    {
                        foxbot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                        foxbot.partChannel(foxbot.getChannel(args[0]));
                        foxbot.sendNotice(sender, String.format("Insult sent to %s, and channel has been left", args[0]));
                        return;
                    }
                    foxbot.sendMessage(args[0], insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
                    foxbot.sendNotice(sender, String.format("Insult sent to %s", args[0]));
                    return;
                }
                foxbot.sendNotice(sender, String.format("%s is not a channel...", args[0]));
                return;
            }
            channel.sendMessage(insult.replace("[", "").replace("]", "").replaceAll("^\\s", "").replaceAll("<.*>", " "));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sinsult [#channel] [-s]", foxbot.getConfig().getCommandPrefix()));
    }
}