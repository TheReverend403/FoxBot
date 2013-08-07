FoxBot
======

IRC bot written in Java

Features:

* Bukkit-like permissions system
* Easy command registration/development
* Async http requests
* Easy to read config, with comments explaining each setting

Default Commands
----------------

All commands should be executed with the command prefix you set in the config, for example, commandPrefix: "." would mean you ran commands like ".say"

Key: 

[] - Optional argument

<> - Required argument

| Command       | Usage         | Result| Permission |
| ------------- |:-------------:|:-----:|-----------:|
| say      | say [#channel] \<message\> [-s] | Makes the bot send a message to the specified channel. Will attempt to join, send the message, and then leave, unless the -s flag is used. If no channel is specified, the message will be sent to the channel the command was used in. | command.say |
| ping     | ping | Makes the bot say "Pong!" Only really useful for making sure the bot is still running. | command.ping |
| join     | join <#channel> [#channel2 #channel3 ...] | Makes the bot join the specified channels. | command.join |
| part     | part [#channel #channel2 ...] | Makes the bot leave the specified channels. If no channel is specified, it will leave the channel the command was used in. | command.part |
| kill     | kill | Makes the bot leave all channels and then disconnect | command.kill
| uptime   | uptime | Displays system uptime. Only works on Unix operating systems. | command.uptime
| action   | action [#channel] \<action\> | Makes the bot send an action to the specified channel. Will attempt to join, do the action and then leave, unless the -s flag is used. If no channel is specified, the action will be sent to the channel the command was used in. | command.action
| reload   | reload | Reloads config variables. Some settings can only take effect when the bot restarts. | command.reload
| insult   | insult [#channel] [-s] | Makes the bot send a random shakespearean insult to the specified channel. Will attempt to join, send the insult and then leave, unless the -s flag is used. If no channel is specified, the insult will be sent to the channel the command was used in. | command.insult
| kick     | kick \<user\> [reason] | Makes the bot kick the specified user from the channel the command is used in with an optional reason. | command.kick

Non-command Features
--------------------

If a user sends a http/https link to a channel the bot is in and they have the chat.urls permission, the bot will attempt to parse their URL and print the page title and content type to the channel.

