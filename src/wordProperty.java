/**
 * Created by Kevin on 4/24/16.
 */
public class wordProperty {

    public String word;
    public String POStag;
    public String NERtag;
    public String table;
    public wordProperty(String word, String POStag)
    {
        this.word = word;
        this.POStag = POStag;
        this.table = "";
        this.NERtag = "";
    }
}
