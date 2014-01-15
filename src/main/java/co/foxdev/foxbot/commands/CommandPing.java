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

import java.io.IOException;
import java.net.*;

public class CommandPing extends Command
{

    private final FoxBot foxbot;

    public CommandPing(FoxBot foxbot)
    {
        super("ping", "command.ping");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();
        String host;
        int port = 80;

        if (args.length == 0)
        {
            channel.sendMessage("Pong!");
            return;
        }

        if (args[0].equalsIgnoreCase("me"))
        {
            channel.sendMessage(sender.getNick());
            return;
        }

        if (args.length < 3)
        {
            host = args[0];

            try
            {
                port = args.length == 2 ? Integer.parseInt(args[1]) : port;
	            Socket socket = new Socket();
                long start = System.currentTimeMillis();
	            socket.connect(new InetSocketAddress(host, port), 500);
                long end = System.currentTimeMillis();

                socket.close();
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &2Ping response time: &r%sms", foxbot.getUtils().munge(sender.getNick()), end - start)));
            }
            catch (UnknownHostException ex)
            {
                foxbot.sendNotice(sender, String.format("%s is an unknown address!", host));
            }
            catch (IOException ex)
            {
                foxbot.sendMessage(channel, foxbot.getUtils().colourise(String.format("(%s) &cError:&r Port %s seems to be closed on %s", foxbot.getUtils().munge(sender.getNick()), port, host)));
            }
            catch (NumberFormatException ex)
            {
                foxbot.sendNotice(sender, String.format("%s is not a number!", args[1]));
            }
            catch (IllegalArgumentException ex)
            {
                foxbot.sendNotice(sender, String.format("%s is too high a number for a port!", port));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sping <address> [port]", foxbot.getConfig().getCommandPrefix()));
    }
}