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

import com.ning.http.client.AsyncHttpClient;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHaspaid extends Command
{
    private final FoxBot foxbot;

    public CommandHaspaid(FoxBot foxbot)
    {
        super("haspaid", "command.ping", "mcpaid");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 1)
        {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

            try
            {
                channel.sendMessage(asyncHttpClient.prepareGet("https://minecraft.net/haspaid.jsp?user=" + args[0]).execute().get().getResponseBody().contains("true") ? foxbot.getUtils().colourise(String.format("(%s) The account \"%s\" is a &2premium&r Minecraft account!", foxbot.getUtils().munge(sender.getNick()), args[0])) : foxbot.getUtils().colourise(String.format("(%s) The account \"%s\" is &cNOT&r a premium Minecraft account!", foxbot.getUtils().munge(sender.getNick()), args[0])));
            }
            catch (Exception ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                foxbot.sendMessage(channel, String.format("(%s) &cSomething went wrong...", foxbot.getUtils().munge(sender.getNick())));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %shaspaid <user>", foxbot.getConfig().getCommandPrefix()));
    }
}