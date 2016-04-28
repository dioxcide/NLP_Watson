package Factory;

import java.util.Hashtable;

/**
 * Created by Tony on 4/27/2016.
 */
public class QueryFactory {
    private Hashtable<String, String> ids;
    private String base;

    public QueryFactory(){
        base = "SELECT *";

        ids = new Hashtable<>();

        ids.put("Actor", "Actor.actor_id");
        ids.put("Director", "Director.director_id");
        ids.put("Actor", "Actor.actor_id");
        ids.put("Movie", "Movie.id");
        ids.put("Person", "Person.id");


    }

    public void resetBase(){
        base = "SELECT *";
    }

    public void buildTable(String table1){
        base = base+" FROM "+table1;
    }

    public void buildTables(String table1, String table2){
        base = base+" FROM "+table1 +" INNER JOIN "+table2+" ON "+ids.get(table1)+
                " = "+ids.get(table2);
    }

    public void buildTables(String table1, String table2, String table3){
        base = base+" FROM "+table1 +" INNER JOIN "+table2+" ON "+ids.get(table1)+
                " = "+ids.get(table2)
                +" INNER JOIN "+table3+" ON "+ids.get(table2)+
                " = "+ids.get(table3);
    }

    public String finalQuery(String where, String table1){

        base = base + " WHERE "+table1+".name LIKE %"+where+"%";

        return base;
    }

    public String finalQuery(String where, String table1, String where2, String table2){

        base = base + " WHERE "+table1+".name LIKE '%"+where+"%'"+
                " AND WHERE "+table2+".name LIKE '%"+where2+"%'";

        return base;
    }
}
