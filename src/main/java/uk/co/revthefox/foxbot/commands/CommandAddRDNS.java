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

public class CommandAddRDNS extends Command
{
    public FoxBot foxbot;

    public CommandAddRDNS(FoxBot foxbot)
    {
        super("resolve", "command.znc.resolve");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        String sender = event.getUser().getNick();

        if (args.length > 0)
        {
            Record[] records = null;
            String host = args[0];

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
                Channel channel = event.getChannel();
                channel.sendMessage(String.format("No records found for %s", host));
                return;
            }

            for (Record record : records)
            {
                AAAARecord aaaaRecord = (AAAARecord) record;
                Channel channel = event.getChannel();

                PTRRecord ptr = new PTRRecord(ReverseMap.fromAddress(aaaaRecord.getAddress()), aaaaRecord.getDClass(), aaaaRecord.getTTL(), aaaaRecord.getName());

                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aAAAA Record for '%s':&r", foxbot.getUtils().munge(sender), aaaaRecord.toString().replace("/", ""))));
                channel.sendMessage(foxbot.getUtils().colourise(String.format("(%s) &aPTR Record for '%s':&r", foxbot.getUtils().munge(sender), ptr.toString().replace("/", ""))));
            }
        }
    }
}