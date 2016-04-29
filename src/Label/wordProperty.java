package Label;

/**
 * Created by Kevin on 4/24/16.
 */
public class WordProperty {

    public String word;
    public String POStag;
    public String NERtag;
    public String table;
    public WordProperty(String word, String POStag)
    {
        this.word = word;
        this.POStag = POStag;
        this.table = "N/A";
        this.NERtag = "";
    }
}
