import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.*;

import Label.Labeler;
import Label.Tuple;
import Label.WordProperty;
import MiddleTier.BusinessTier;
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

    public static String computeWhichOscarType(String sample){
        String words[] = sample.split(" ");

        if(words[0].equals("Which")){
            if(words[1].equals("actress")){
                return "best-actress";
            }
            else if(words[1].equals("actor")){
                return "best-actor";
            }
            else if(words[1].equals("movie")){
                return "best-picture";
            }
            else if(words[1].equals("director")){
                return "best-director";
            }
        }
        return "N/A";
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
        ArrayList<String> sentenceList = new ArrayList<String>();
        String path = new File("").getAbsolutePath();
        BusinessTier bsnRn = new BusinessTier();
        boolean yesNoAnswer = false;
        Tuple tupleTemp;
        String whAnswer = "";
        String sample;
        File f;
        path+= "/src/sample2.txt";


//        f = new File(args[0]);
//
//        if(!f.exists())
//        {
//            System.out.println("Error: " + args[0] + " is not a file. Running sample.txt");
//        }
//        else
//        {
//            path = args[0];
//        }

        // this is your print stream, store the reference
        PrintStream err = System.err;

        // now make all writes to the System.err stream silent
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
            }
        }));

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

            for(int i = 0; i < sentenceList.size();i++)
            {
                System.out.println(sentenceList.get(i));
                tupleTemp = Labeler.runSentence(sentenceList.get(i));

                if(tupleTemp.questionWord.equals("Who") || tupleTemp.questionWord.equals("Which") || tupleTemp.questionWord.equals("When") ){

                    if(tupleTemp.oscarType.equals("N/A")){
                        tupleTemp.oscarType = computeWhichOscarType(sentenceList.get(i));
                    }
                    whAnswer = bsnRn.determineWHQuestion(tupleTemp.labeledWordList, tupleTemp.questionWord, tupleTemp.oscarType);

                    if(whAnswer != null){
                        System.out.println("Answer: "+whAnswer);
                    }
                    else{
                        System.out.println("Answer: No answer");
                    }


                }
                else{
                    yesNoAnswer = bsnRn.determineYesNoQuestion(tupleTemp.labeledWordList, tupleTemp.oscarType);

                    if(yesNoAnswer){
                        System.out.println("Answer: Yes");
                    }
                    else{
                        System.out.println("Answer: No");
                    }
                }

                System.out.println("");
            }

        }
        catch(Exception e)
        {

        }

        // set everything bck to its original state afterwards
        System.setErr(err);
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
