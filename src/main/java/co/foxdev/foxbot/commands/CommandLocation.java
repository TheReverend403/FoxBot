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
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLocation extends Command
{
    private final FoxBot foxbot;

    public CommandLocation(FoxBot foxbot)
    {
        super("location", "command.location", "stalk", "geoip", "locate");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 1)
        {
            String host = foxbot.getUser(args[0]).getHostmask() == null || foxbot.getUser(args[0]).getHostmask().isEmpty() ? args[0] : foxbot.getUser(args[0]).getHostmask();

            LookupService ls = null;

            try
            {
                ls = new LookupService(new File("data/GeoLite2-Country.mmdb.gz"));
            }
            catch (IOException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                channel.sendMessage(String.format("(%s) &cSomething went wrong...", foxbot.getUtils().munge(sender.getNick())));
                return;
            }

            Location loc = ls.getLocation(host);
            StringBuilder sb = new StringBuilder();
            sb.append(ls.getCountry(host).getName());

            channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &2GeoIP location for %s:&r %s", foxbot.getUtils().munge(sender.getNick()), host, sb.toString())));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %slocation <user|host>", foxbot.getConfig().getCommandPrefix()));
    }
}

