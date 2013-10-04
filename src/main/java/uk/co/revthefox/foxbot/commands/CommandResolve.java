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

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import org.xbill.DNS.*;
import uk.co.revthefox.foxbot.FoxBot;
import org.pircbotx.Channel;

public class CommandResolve extends Command
{
    public FoxBot foxbot;

    public CommandResolve(FoxBot foxbot)
    {
        super("resolve", "command.znc.resolve");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length > 0)
        {
            Channel channel = event.getChannel();
            Record[] records = null;
            String host = args[0];

            host = foxbot.getUser(host).getHostmask() == null ? host : foxbot.getUser(host).getHostmask();

            try
            {
                records = new Lookup(host, Type.AAAA).run();
            }
            catch (TextParseException ex)
            {
                ex.printStackTrace();
            }

            if (records == null || records.length == 0)
            {
                channel.sendMessage(String.format("(%s) No records found for %s", foxbot.getUtils().munge(sender.getNick()), host));
                return;
            }

            for (Record record : records)
            {
                AAAARecord aaaaRecord = (AAAARecord) record;
                PTRRecord ptr = new PTRRecord(ReverseMap.fromAddress(aaaaRecord.getAddress()), aaaaRecord.getDClass(), aaaaRecord.getTTL(), aaaaRecord.getName());

                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aAAAA record for %s:&r %s. %s IN %s", foxbot.getUtils().munge(sender.getNick()), host, host, aaaaRecord.getType(), aaaaRecord.getAddress())));
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aPTR record for %s:&r %s", foxbot.getUtils().munge(sender.getNick()), host, ptr.getTarget())));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sresolve <host>", foxbot.getConfig().getCommandPrefix()));
    }
}