package Labaler;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Kevin on 4/27/16.
 */
public class Database {



    public static void executeQuery(String search)
    {
        String[] tables = {"Person","Movie"};
        Connection c = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/Users/Kevin/Downloads/SqliteDatabases/oscar-movie_imdb.sqlite");


            for(String table : tables)
            {
                ResultSet rs = c.createStatement().executeQuery("PRAGMA TABLE_INFO('" + table + "')");


                rs.close();


            }
            c.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
