package DatabaseAccess;

import java.sql.*;

/**
 * Created by Tony on 4/27/2016.
 */
public class DBAccess {

    private Connection con = null;

    public DBAccess(){
    }

    public boolean executeQuery(String query) {
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(rs.next()){
                return true;
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String executeWHQuery(String query){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(rs.next()){
                return rs.getString(1);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean searchPersonName(String name){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Person WHERE Person.name" +
                    " LIKE '%"+name+"'");

            if(rs.next()){
                return true;
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean searchPersonLocation(String place){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Person WHERE Person.pob" +
                    " LIKE '%"+place+"%'");

            if(rs.next()){
                return true;
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String searchMovieName(String name){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Movie WHERE Movie.name" +
                    " LIKE '%"+name+"%'");

            if(rs.next()){
                return rs.getString("name");
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    public boolean searchMovieYear(String year){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Movie WHERE Movie.year" +
                    " LIKE '%"+year+"%'");

            if(rs.next()){
                return true;
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean searchIsActor(String name){
        try{
            //String dir = System.getProperty("user.dir");
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" +
                    "SqliteDatabases\\oscar-movie_imdb.sqlite");

            Statement stmt;

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Person INNER JOIN ACTOR ON Actor.actor_id = Person.id WHERE Person.name" +
                    " LIKE '%"+name+"'");

            if(rs.next()){
                return true;
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
