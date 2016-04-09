import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.*;

import POSParsing.NERParser;
import POSParsing.NERTuple;
import POSParsing.TreeParser;
import POSParsing.WordBank;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


/**
 * Created by Tony on 4/5/2016.
 */
public class Classifier
{

    public static List<Tree> parse(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        List<Tree> result = new ArrayList<Tree>();
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeAnnotation.class);
            result.add(tree);
        }

        return result;
    }

    public static void Categorize(ArrayList<NERTuple> list)
    {
        ArrayList<NERTuple> filterlist = new ArrayList<NERTuple>();

        String sentence = "";

        for(NERTuple x : list)
        {
            sentence += x.getWord() + " ";
            if(!x.getNERTag().equals("O"))
            {
                //System.out.println(x.getNERTag());
                filterlist.add(x);
            }
        }

        System.out.println("\n--------------------------\n");
        System.out.println("SENTENCE: " + sentence);
        System.out.println("Geography: " + WordBank.CheckGeographyBank(sentence));
        System.out.println("Movies: " + WordBank.CheckMovieBank(sentence));
        System.out.println("Music: " + WordBank.CheckMusicBank(sentence));
        System.out.println("\n--------------------------\n");
    }


        public static void main(String[] args)
        {


            String path = new File("").getAbsolutePath();
            path+= "/src/sample.txt";
            try
            {
                FileReader filereader = new FileReader(path);
                BufferedReader bufferedreader = new BufferedReader(filereader);
                ArrayList<String> testSet = new ArrayList<String>();

                String line = null;
                while((line = bufferedreader.readLine()) != null)
                {
                    ArrayList<NERTuple> sentence = NERParser.processNER(line);
                    Categorize(sentence);

                }

                bufferedreader.close();



            }
            catch(Exception e)
            {

            }
        }

}
