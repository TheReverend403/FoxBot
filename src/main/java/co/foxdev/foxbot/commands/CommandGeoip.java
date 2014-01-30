package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandGeoip extends Command
{
    private final FoxBot foxbot;

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

        if (args.length == 1)
        {
            String ip = foxbot.getUser(args[0]).getHostmask().isEmpty() ? args[0] : foxbot.getUser(args[0]).getHostmask();
            String country = foxbot.getLookupService().getLocation(ip).countryName;
            String city = foxbot.getLookupService().getLocation(ip).city;

            channel.sendMessage(Utils.colourise(String.format("(%s) &2GeoIP info for %s:&r %s%s", Utils.munge(sender.getNick()), ip, city == null ? "" : city, country == null ? "" : city == null ? country : ", " + country)));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sgeoip <host|user>", foxbot.getConfig().getCommandPrefix()));
    }
}
