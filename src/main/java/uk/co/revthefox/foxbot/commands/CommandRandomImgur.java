package uk.co.revthefox.foxbot.commands;

import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.Random;

public class CommandRandomImgur extends Command
{
    private FoxBot foxbot;

    public CommandRandomImgur(FoxBot foxbot)
    {
        super("imgur", "command.imgur");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            channel.sendMessage(new Random().nextBoolean() ? String.format("(%s) %sRandom Imgur: %shttp://imgur.com/gallery/%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, RandomStringUtils.randomAlphanumeric(5)) : String.format("(%s) %sRandom Imgur: %shttp://imgur.com/gallery/%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, RandomStringUtils.randomAlphanumeric(7)));
        }
    }
}
