package uk.co.revthefox.foxbot.listeners.spamhandler;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class RepeatSpamHandler extends SpamHandler
{
    private FoxBot foxbot;

    public RepeatSpamHandler(FoxBot foxbot)
    {
        super(foxbot);
        this.foxbot = foxbot;
    }

    @Override
    public void run(MessageEvent<FoxBot> event)
    {
        User user = event.getUser();
        Channel channel = event.getChannel();

        if (user.getNick().equals(foxbot.getNick()) || !channel.getNormalUsers().contains(user))
        {
            return;
        }

        String message = event.getMessage();
        String hostmask = user.getHostmask();

        if (!this.getDuplicateMap().containsKey(hostmask))
        {
            this.getDuplicateMap().put(hostmask, message);
            return;
        }

        if (message.equals(this.getDuplicateMap().get(hostmask)))
        {
            this.getSpamRating().put(hostmask, this.getSpamRating().asMap().get(hostmask) == null ? 1 : this.getSpamRating().asMap().get(hostmask) + 1);
            this.getDuplicateMap().put(hostmask, message);
        }

        // Take action on the spam rating
        if (this.getSpamRating().asMap().get(hostmask) != null && this.getSpamRating().asMap().get(hostmask) != 0)
        {
            spamPunisher(channel, user, this.getSpamRating().asMap().get(hostmask));
        }
    }
}
