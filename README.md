FoxBot
======

IRC bot written in Java

Features:

* Bukkit-like permissions system
* Easy command registration/development
* Async http requests

Default Commands:

All commands should be executed with the command prefix you set in the config, for example, commandPrefix: "." would mean you ran commands like ".say"

Key: 

[] - Optional argument
<> - Required argument

| Command       | Usage         | Result| Permission |
| ------------- |:-------------:|:-----:|-----------:|
| say      | say [#channel] <message> [-flags] | Makes the bot send a message to the specified channel. Will attempt to join, say the message, then leave, unless the -s flag is used. If no channel is specified, the message will be sent to the channel the command was used in. | command.say |
