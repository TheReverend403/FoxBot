package uk.co.revthefox.foxbot.database;

import org.pircbotx.Colors;
import uk.co.revthefox.foxbot.FoxBot;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:data/bot.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tells (time STRING, sender STRING, receiver STRING, message STRING, used TINYINT)");
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
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
                ex.printStackTrace();
            }
        }
    }

    public void addTell(String sender, String receiver, String message)
    {
        PreparedStatement statement = null;

        try
        {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO tells (time, sender, receiver, message, used) VALUES (?, ?, ?, ?, 0);");
            statement.setString(1, new SimpleDateFormat("[yyyy-MM-dd - HH:mm:ss]").format(Calendar.getInstance().getTime()));
            statement.setString(2, sender);
            statement.setString(3, receiver);
            statement.setString(4, message);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
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
                ex.printStackTrace();
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
                tells.add(String.format("%s %sMessage from: %s%s %sMessage: %s%s", rs.getString("time"), Colors.GREEN, Colors.NORMAL, rs.getString("sender"), Colors.GREEN, Colors.NORMAL, rs.getString("message")));
            }

            statement = connection.prepareStatement("UPDATE tells SET used = 1 WHERE receiver = ? AND used = 0");
            statement.setString(1, user);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
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
                ex.printStackTrace();
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
            ex.printStackTrace();
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
                ex.printStackTrace();
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
                ex.printStackTrace();
            }
            return;
        }
        System.out.println("Database is already disconnected!");
    }
}
