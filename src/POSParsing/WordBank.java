package POSParsing;

import edu.stanford.nlp.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Kevin on 4/8/16.
 */
public class WordBank {

    private static int CountWords(String sentence,String[] Bank)
    {
        String[] sentenceList = sentence.split(" ");

        int counter = 0;

        for(String word : sentenceList)
        {
            if(ArrayUtils.contains(Bank,word))
            {
                counter++;
            }
        }
        return counter;
    }

    public static int CheckMovieBank(String sentence)
    {
        String[] movieBank = {"star","directed","movie","win","oscar","actor","film","actress"};

        return CountWords(sentence,movieBank);
    }

    public static int CheckMusicBank(String sentence)
    {
        String[] MusicBank = {"sing","album","track","sings","artist","rock","pop","dance"};

        return CountWords(sentence,MusicBank);
    }

    public static int CheckGeographyBank(String sentence)
    {
        String[] GeographyBank = {  "capital","deep","deeper","deepest","continent",
                                    "countries","border","mountain","world","ocean","highest"};

        return CountWords(sentence,GeographyBank);
    }

}
