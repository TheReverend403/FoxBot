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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommandHash extends Command
{
    private final FoxBot foxbot;

    /**
     * Sends a hash of a specified string using md5, sha1 or sha256 to the channel.
     * <p/>
     * Usage: .hash <MD5|SHA-1|SHA-256> <string>
     */
    public CommandHash(FoxBot foxbot)
    {
        super("hash", "command.hash");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length > 1)
        {
            String hashType = args[0].toUpperCase();
            MessageDigest digest;

            try
            {
                digest = MessageDigest.getInstance(hashType);
            }
            catch (NoSuchAlgorithmException ex)
            {
                channel.send().message(String.format("(%s) Invalid hash. Valid types are SHA-1, SHA-256 and MD5", Utils.munge(sender.getNick())));
                return;
            }

            StringBuilder stringToHash = new StringBuilder(args[1]);

            for (int i = 2; i < args.length; i++)
            {
                stringToHash.append(" ").append(args[i]);
            }

            digest.reset();
            channel.send().message(String.format("(%s) %s", Utils.munge(sender.getNick()), byteArrayToHexString(digest.digest(stringToHash.toString().getBytes()))));
            return;
        }

        sender.send().notice(String.format("Wrong number of args! Use %shash <SHA-1|SHA-256|MD5> <text>", foxbot.getConfig().getCommandPrefix()));
    }

    private String byteArrayToHexString(byte[] b)
    {
        String result = "";

        for (byte aB : b)
        {
            result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }
}