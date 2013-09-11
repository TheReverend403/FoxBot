package uk.co.revthefox.foxbot.database;

import org.pircbotx.Colors;
import uk.co.revthefox.foxbot.FoxBot;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database
{
    private FoxBot foxbot;

    Connection connection = null;

    public Database(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void connect()
    {
        Statement statement = null;

        try
        {
            String databaseType = foxbot.getConfig().getDatabaseType();

            if (databaseType.equalsIgnoreCase("sqlite"))
            {
                Class.forName("org.sqlite.JDBC");
            }

            String url = databaseType.equalsIgnoreCase("mysql") ? String.format("jdbc:mysql://%s:%s/%s", foxbot.getConfig().getDatabaseHost(), foxbot.getConfig().getDatabasePort(), foxbot.getConfig().getDatabaseName()) : "jdbc:sqlite:data/bot.db";

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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bans (target VARCHAR(32), reason VARCHAR(1024), banner VARCHAR(32), ban_time VARCHAR(32))");
        }
        catch (SQLException | ClassNotFoundException ex)
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addTell(String sender, String receiver, String message)
    {
        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO tells (tell_time, sender, receiver, message, used) VALUES (?, ?, ?, ?, 0);");
            statement.setString(1, new SimpleDateFormat("[yyyy-MM-dd - HH:mm:ss]").format(Calendar.getInstance().getTime()));
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<String> getTells(String user, Boolean showAll)
    {
        List<String> tells = new ArrayList<>();
        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement(showAll ? "SELECT * FROM tells WHERE receiver = ?" : "SELECT * FROM tells WHERE receiver = ? AND used = 0");
            statement.setString(1, user);
            connection.setAutoCommit(true);
            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                tells.add(String.format("%s %sMessage from: %s%s %sMessage: %s%s", rs.getString("tell_time"), Colors.GREEN, Colors.NORMAL, rs.getString("sender"), Colors.GREEN, Colors.NORMAL, rs.getString("message")));
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
            }
            catch (SQLException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void storeBan(String banner, String target, String reason, Date time)
    {
        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO bans (target, reason, banner, ban_time) VALUES (?, ?, ?, ?);");
            statement.setString(1, target);
            statement.setString(2, reason);
            statement.setString(3, banner);
            statement.setString(4, String.valueOf(time));
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