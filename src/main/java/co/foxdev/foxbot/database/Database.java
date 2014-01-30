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

package co.foxdev.foxbot.database;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import co.foxdev.foxbot.FoxBot;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database
{
    private final FoxBot foxbot;

    private Connection connection = null;
    private String databaseType;
    private String url;

    public Database(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void connect()
    {
        Statement statement = null;

        databaseType = foxbot.getConfig().getDatabaseType();
        url = databaseType.equalsIgnoreCase("mysql") ? String.format("jdbc:mysql://%s:%s/%s", foxbot.getConfig().getDatabaseHost(), foxbot.getConfig().getDatabasePort(), foxbot.getConfig().getDatabaseName()) : "jdbc:sqlite:data/bot.db";

        try
        {
            if (databaseType.equalsIgnoreCase("sqlite"))
            {
                Class.forName("org.sqlite.JDBC");
            }

            if (databaseType.equalsIgnoreCase("mysql"))
            {
                String user = foxbot.getConfig().getDatabaseUser();
                String password = foxbot.getConfig().getDatabasePassword();
                connection = DriverManager.getConnection(url, user, password);
            }
            else
            {
                connection = DriverManager.getConnection(url);
            }

            statement = connection.createStatement();

            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tells (tell_time VARCHAR(32), sender VARCHAR(32), receiver VARCHAR(32), message VARCHAR(1024), used TINYINT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bans (channel VARCHAR(64), target VARCHAR(32), hostmask VARCHAR(64), reason VARCHAR(1024), banner VARCHAR(32), ban_time BIGINT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS kicks (channel VARCHAR(64), target VARCHAR(32), hostmask VARCHAR(64), reason VARCHAR(1024), kicker VARCHAR(32), kick_time BIGINT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS mutes (channel VARCHAR(64), target VARCHAR(32), hostmask VARCHAR(64), reason VARCHAR(1024), muter VARCHAR(32), mute_time BIGINT)");
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            foxbot.disconnect();
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reconnect()
    {
        if (connection == null)
        {
            try
            {
                if (databaseType.equalsIgnoreCase("sqlite"))
                {
                    Class.forName("org.sqlite.JDBC");
                }

                if (databaseType.equalsIgnoreCase("mysql"))
                {
                    String user = foxbot.getConfig().getDatabaseUser();
                    String password = foxbot.getConfig().getDatabasePassword();
                    connection = DriverManager.getConnection(url, user, password);
                }
                else
                {
                    connection = DriverManager.getConnection(url);
                }
            }
            catch (SQLException | ClassNotFoundException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                foxbot.disconnect();
            }
        }
    }

    public void addTell(String sender, String receiver, String message)
    {
        this.reconnect();

        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO tells (tell_time, sender, receiver, message, used) VALUES (?, ?, ?, ?, 0);");
            statement.setString(1, new SimpleDateFormat("[yyyy-MM-dd - HH:mm:ss]").format(Calendar.getInstance().getTimeInMillis()));
            statement.setString(2, sender);
            statement.setString(3, receiver);
            statement.setString(4, message);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<String> getTells(String user, Boolean showAll)
    {
        this.reconnect();

        List<String> tells = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            statement = connection.prepareStatement(showAll ? "SELECT * FROM tells WHERE receiver = ?" : "SELECT * FROM tells WHERE receiver = ? AND used = 0");
            statement.setString(1, user);
            connection.setAutoCommit(true);
            rs = statement.executeQuery();

            while (rs.next())
            {
                tells.add(Utils.colourise(String.format("%s &2Message from: &r%s &2Message: &r%s", rs.getString("tell_time"), rs.getString("sender"), rs.getString("message"))));
            }

            statement = connection.prepareStatement("UPDATE tells SET used = 1 WHERE receiver = ? AND used = 0");
            statement.setString(1, user);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tells;
    }

    public void cleanTells(String user)
    {
        this.reconnect();

        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement("DELETE FROM tells WHERE receiver = ? AND used = 1");
            statement.setString(1, user);
            connection.setAutoCommit(true);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addBan(Channel channel, User target, String reason, User banner, long time)
    {
        this.reconnect();

        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO bans (channel, target, hostmask, reason, banner, ban_time) VALUES (?, ?, ?, ?, ?, ?);");
            statement.setString(1, channel.getName());
            statement.setString(2, target.getNick());
            statement.setString(3, target.getHostmask());
            statement.setString(4, reason);
            statement.setString(5, banner.getNick());
            statement.setLong(6, time);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addKick(Channel channel, User target, String reason, User kicker, long time)
    {
        this.reconnect();

        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO kicks (channel, target, hostmask, reason, kicker, kick_time) VALUES (?, ?, ?, ?, ?, ?);");
            statement.setString(1, channel.getName());
            statement.setString(2, target.getNick());
            statement.setString(3, target.getHostmask());
            statement.setString(4, reason);
            statement.setString(5, kicker.getNick());
            statement.setLong(6, time);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addMute(Channel channel, User target, String reason, User muter, long time)
    {
        this.reconnect();

        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO mutes (channel, target, hostmask, reason, muter, mute_time) VALUES (?, ?, ?, ?, ?, ?);");
            statement.setString(1, target.getNick());

            statement.setString(1, channel.getName());
            statement.setString(2, target.getNick());
            statement.setString(3, target.getHostmask());
            statement.setString(4, reason);
            statement.setString(5, muter.getNick());
            statement.setLong(6, time);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                    connection = null;
                }
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void disconnect()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
                connection = null;
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        System.out.println("Database is already disconnected!");
    }
}