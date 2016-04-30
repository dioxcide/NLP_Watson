package Label;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/29/16.
 */
public class Tuple {
    public ArrayList<WordProperty> labeledWordList;
    public String questionWord;

    public Tuple(ArrayList<WordProperty> labeledWordList,String questionWord)
    {
        this.labeledWordList = labeledWordList;
        this.questionWord = questionWord;
    }
}
