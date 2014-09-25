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
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandGeoip extends Command
{
    private final FoxBot foxbot;

    /**
     * Attempts to get an IP address' location using the Maxmind GeoIP databases.
     * Requires a GeoIP database to be placed in the data folder.
     * <p/>
     * Usage: .geoip <user|host>
     */
    public CommandGeoip(FoxBot foxbot)
    {
        super("geoip", "command.geoip");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (foxbot.getLookupService() == null)
        {
            channel.send().message("GeoIP is unavailable as the bot owner has not installed a GeoIP database. Ask them to install one from here: http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
            return;
        }

        if (args.length == 1)
        {
            User user = foxbot.bot().getUserChannelDao().getUser(args[0]);
            String ip = user.getHostmask().isEmpty() ? args[0] : user.getHostmask();
            String country = foxbot.getLookupService().getLocation(ip).countryName;
            String city = foxbot.getLookupService().getLocation(ip).city;
            channel.send().message(Utils.colourise(String.format("(%s) &2GeoIP info for %s:&r %s%s", Utils.munge(sender.getNick()), ip, city == null ? "" : city,
                                                   country == null ? "" : city == null ? country : ", " + country)));
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %sgeoip <host|user>", foxbot.getConfig().getCommandPrefix()));
    }
}
