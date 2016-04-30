package Label;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/29/16.
 */
public class Tuple {
    public ArrayList<WordProperty> labeledWordList;
    public String questionWord;
    public String oscarType;

    public Tuple(ArrayList<WordProperty> labeledWordList,String questionWord,String oscarType)
    {
        this.labeledWordList = labeledWordList;
        this.questionWord = questionWord;
        this.oscarType = oscarType;
    }
}
