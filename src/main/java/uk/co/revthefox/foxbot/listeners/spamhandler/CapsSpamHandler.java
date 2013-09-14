package uk.co.revthefox.foxbot.listeners.spamhandler;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CapsSpamHandler extends SpamHandler
{
    private FoxBot foxbot;

    public CapsSpamHandler(FoxBot foxbot)
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

        int count = 0;
        int length = 0;

        for (char character : message.toCharArray())
        {
            // Don't count spaces, it messes with the final percentage
            if (Character.isAlphabetic(character))
            {
                length++;

                if (Character.isUpperCase(character))
                {
                    count++;
                }
            }
        }

        // Prevent divide-by-zero errors
        if (length > 5)
        {
            count = (count * 100) / length;

            // Kick the user if the percentage of caps in their message was higher than the max value
            if (count > 75)
            {
                long kickTime = System.currentTimeMillis();
                foxbot.kick(channel, user, "Caps spam (" + count + "%)");
                foxbot.getDatabase().addKick(channel, user, "Caps spam (" + count + "%)", foxbot.getUser(foxbot.getNick()), kickTime);
            }
        }
    }
}
