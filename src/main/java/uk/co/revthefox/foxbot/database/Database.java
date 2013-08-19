package uk.co.revthefox.foxbot.database;

import uk.co.revthefox.foxbot.FoxBot;

import java.sql.*;

public class Database
{
    private FoxBot foxbot;

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

        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:bot.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("create table if not exists tells;");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
