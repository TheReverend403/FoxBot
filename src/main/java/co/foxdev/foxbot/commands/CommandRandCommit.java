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

public class CommandRandCommit extends Command
{
    private final FoxBot foxbot;

	/**
	 * Gets a random commit message from http://whatthecommit.com and sends it to the current channel.
	 *
	 * Usage: .wtc
	 */
    public CommandRandCommit(FoxBot foxbot)
    {
        super("wtc", "command.wtc");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

	    String commitMessage;

	    try
	    {
		    commitMessage = Jsoup.connect("http://whatthecommit.com/").timeout(500).get().select("p").first().text();
	    }
	    catch (Exception ex)
	    {
		    foxbot.getLogger().error("Error occurred while fetching random commit message", ex);
		    channel.send().message(Utils.colourise(String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick()))));
		    return;
	    }

	    channel.send().message(Utils.colourise(String.format("(%s) &2Random commit message: &r%s", Utils.munge(sender.getNick()), commitMessage)));
    }
}