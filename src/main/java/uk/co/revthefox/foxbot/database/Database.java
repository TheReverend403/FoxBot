package uk.co.revthefox.foxbot.database;

import uk.co.revthefox.foxbot.FoxBot;

import java.io.File;
import java.sql.*;

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
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

        try
        {
            File path = new File("data");

            if (!path.exists())
            {
                path.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:data/bot.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customCommands (command string, text string)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tells (user string, text string, used boolean)");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void addTell(String name, String message)
    {
        try
        {
            PreparedStatement statement;
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("INSERT INTO tells (user, text, used) VALUES (?,?, 'false');");
            statement.setString(1, name);
            statement.setString(2, message);
            statement.executeUpdate();
            connection.commit();
            //statement.executeUpdate(String.format("INSERT INTO tells (user, text, used)\nVALUES ('%s','%s', 'false');", name, message));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
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
