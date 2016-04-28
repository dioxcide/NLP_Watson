import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.io.*;

import POSParsing.NERParser;
import POSParsing.NERTuple;
import POSParsing.WordBank;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
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

    public static void Categorize(String sentence,ArrayList<NERTuple> NERsentence,Tree tree) {
        int geoScore = WordBank.CheckGeographyBank(sentence);
        int musicScore = WordBank.CheckMusicBank(sentence);
        int movieScore = WordBank.CheckMovieBank(sentence);

        System.out.println("<QUESTION> " + sentence);
        if(geoScore ==  musicScore && musicScore == movieScore)
        {

            int locationScore = 0;
            int personScore = 0;

            for(NERTuple x : NERsentence)
            {
                if(!x.getNERTag().equals("O"))
                {
                    if(x.getNERTag().equals("LOCATION"))
                    {
                        locationScore++;
                    }
                    else if(x.getNERTag().equals("PERSON"))
                    {
                        personScore++;
                    }

                }
            }
            if(locationScore > 0 && personScore == 0)
            {
                System.out.println("<CATEGORY> Geography");
            }
            else if(personScore > 0 && locationScore > 0)
            {
                System.out.println("<CATEGORY> Music");
            }
            else
            {
                if(sentence.contains("by"))
                {
                    System.out.println("<CATEGORY> Movie");
                }
                else
                {
                    System.out.println("<CATEGORY> Music");
                }
            }
        }
        else
        {
            if(geoScore > musicScore && geoScore > movieScore)
            {
                System.out.println("<CATEGORY> Geography");
            }
            else if(movieScore > geoScore && movieScore > musicScore)
            {
                System.out.println("<CATEGORY> Movie");
            }
            else if(musicScore > movieScore && musicScore > geoScore)
            {
                System.out.println("<CATEGORY> Music");
            }
            else
            {
                if(sentence.contains("born") || sentence.contains("who"))
                {
                    System.out.println("<CATEGORY> Music");
                }
                else if(sentence.contains("in") || sentence.toLowerCase().contains("was"))
                {
                    System.out.println("<CATEGORY> Movie");
                }
                else
                {
                    System.out.println("<CATEGORY> Geography");
                }
            }
        }
        System.out.println("<PARSETREE>");
        tree.pennPrint();

    }

    static void part1(String args[])
    {
        ArrayList<String> sentenceList = new ArrayList<String>();
        ArrayList<Tree> treeList = new ArrayList<Tree>();
        String path = new File("").getAbsolutePath();
        path+= "/src/sample.txt";

        ArrayList<ArrayList<NERTuple>> NERList = new ArrayList<ArrayList<NERTuple>>();

        File f = new File(args[0]);

        if(!f.exists())
        {
            System.out.println("Error: " + args[0] + " is not a file. Running sample.txt");
        }
        else
        {
            path = args[0];
        }

        try
        {
            FileReader filereader = new FileReader(path);
            BufferedReader bufferedreader = new BufferedReader(filereader);

            String line = null;
            while((line = bufferedreader.readLine()) != null)
            {
                if(line.length() == 0)
                {
                    continue;
                }
                sentenceList.add(line);
            }

            bufferedreader.close();


            for(String sentence : sentenceList)
            {
                NERList.add(NERParser.processNER(sentence));
                treeList.add(parse(sentence).get(0));
            }

            for(int i = 0; i < sentenceList.size();i++)
            {
                Categorize(sentenceList.get(i),NERList.get(i),treeList.get(i));
                System.out.println();
            }

        }
        catch(Exception e)
        {

        }
    }

    static void part2(String args[])
    {
        String sample = "Was Loren born in Italy?";
        Labeler.runSentence(sample);
    }

    public static void main(String[] args) {

        int flag = 1;

        if(flag == 0)
        {
            part1(args);
        }
        else
        {
            part2(args);
        }
    }
}
