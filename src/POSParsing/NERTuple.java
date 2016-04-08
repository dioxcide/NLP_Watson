package POSParsing;

/**
 * Created by Tony on 4/8/2016.
 */
public class NERTuple {
    private String NERTag;
    private String word;

    public NERTuple(String tag, String w){
        this.NERTag = tag;
        this.word = w;
    }

    public String getNERTag() {
        return NERTag;
    }

    public void setNERTag(String NERTag) {
        this.NERTag = NERTag;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
