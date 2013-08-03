package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandTitle extends Command
{

    private FoxBot foxbot;

    public CommandTitle(FoxBot foxbot)
    {
        super("title", "command.title");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length != 1)
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %stitle <address>", foxbot.getConfig().getCommandPrefix()));
            return;
        }
        /*
        try
        {
            Future<Response> future = asyncHttpClient.prepareGet(args[0].startsWith("http://") ? args[0] : args[0].startsWith("https://") ? "https://" + args[0] : "https://" + args[0]).execute();
            response = future.get();
        }
        catch (IOException ex)
        {
            channel.sendMessage("IOException");
            ex.printStackTrace();
            return;
        }
        catch (InterruptedException ex)
        {
            channel.sendMessage("InterruptedException");
            ex.printStackTrace();
            return;
        }
        catch (ExecutionException ex)
        {
            channel.sendMessage("ExecutionException");
            ex.printStackTrace();
            return;
        }

        try
        {
            content = response.getResponseBody().toString();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Pattern pattern = Pattern.compile("<head>.*?<title>(.*?)</title>.*?</head>", Pattern.DOTALL);
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            content = m.group(1);
        }

        */

        String title = foxbot.getUtils().parseChatUrl(args[0], sender, channel);
        if (!title.isEmpty())
        {
            channel.sendMessage(title);
        }
    }
}
