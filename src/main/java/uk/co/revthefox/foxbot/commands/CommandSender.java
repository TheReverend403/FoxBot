package uk.co.revthefox.foxbot.commands;

import java.util.Collection;


public interface CommandSender
{

    /**
     * Get the unique name of this command sender.
     *
     * @return the senders username
     */
    public String getName();

    /**
     * Send a message to this sender.
     *
     * @param message the message to send
     */
    public void sendMessage(String message);

    /**
     * Send several messages to this sender. Each message will be sent
     * separately.
     *
     * @param messages the messages to send
     */
    public void sendMessages(String... messages);

    /**
     * Get all groups this user is part of. This returns an unmodifiable
     * collection.
     *
     * @return the users groups
     */
    public Collection<String> getGroups();

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    public boolean hasPermission(String permission);

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value the value of the node
     */
    public void setPermission(String permission, boolean value);
}
