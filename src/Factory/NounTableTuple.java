package Factory;

/**
 * Created by Tony on 4/28/2016.
 */
public class NounTableTuple {
    private String noun;
    private String table;
    private String column;

    public NounTableTuple(String n, String t, String c){
        this.noun = n;
        this.table = t;
        this.column = c;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
