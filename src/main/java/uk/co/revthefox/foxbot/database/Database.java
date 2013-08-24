package uk.co.revthefox.foxbot.database;

import org.pircbotx.Colors;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
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

            File path = new File("data");

            if (!path.exists() && !path.mkdirs())
            {
                System.out.println("Couldn't create data folder. Shutting down.");
                foxbot.getBot().disconnect();
                return;
            }

            connection = DriverManager.getConnection("jdbc:sqlite:data/bot.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tells (sender string, receiver string, message string, used boolean)");
        }
        catch(SQLException | ClassNotFoundException ex)
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
            statement = connection.prepareStatement("INSERT INTO tells (sender, receiver, message, used) VALUES (?,?,?, 'false');");
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setString(3, message);
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

    public List<String> getTells(String user, Boolean used)
    {
        List<String> tells = new ArrayList<>();
        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement("SELECT * FROM tells WHERE receiver = ? AND used = 'false'");
            statement.setString(1, user);
            connection.setAutoCommit(true);
            ResultSet rs = statement.executeQuery();

            while(rs.next())
            {
                tells.add(String.format("%sMessage from: %s%s %sMessage: %s%s", Colors.GREEN, Colors.NORMAL, rs.getString("sender"), Colors.GREEN, Colors.NORMAL, rs.getString("message")));
            }

            if (used)
            {
                statement = connection.prepareStatement("UPDATE tells SET used = 'true' WHERE receiver = ? AND used = 'false'");
                statement.setString(1, user);
                statement.executeUpdate();
            }
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
