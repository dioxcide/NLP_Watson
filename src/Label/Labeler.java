package Label;

import java.lang.reflect.Array;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.ArrayUtils;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by Kevin on 4/21/16.
 */


public class Labeler {


    // convert a String sentence to a parse tree (TA's code)
    private static List<Tree> parse(String text) {
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

    /*
        for every non NNP tag words,
        if the word matches within the lists below,
        we return that string that's associated with that table
    */
    private static String associateTable(String word)
    {
        String[] directorList = {"director","by","directed","direct"};
        String[] actorList = {"actor","actress","star","starred"};
        String[] oscarList = {"oscar"};
        String[] movieList = {"movie"};

        if(ArrayUtils.contains(directorList,word))   return     "Director";
        else if(ArrayUtils.contains(actorList,word)) return     "Actor";
        else if(ArrayUtils.contains(oscarList,word)) return     "Oscar";
        else if(ArrayUtils.contains(movieList,word)) return     "Movie";

        return "N/A";
    }


    /*
         iterate through our list of important words and
         give it an associated table
    */
    private static void mapWords(ArrayList<WordProperty> importantWords)
    {
        for(WordProperty tuple : importantWords)
        {
            if(!tuple.POStag.equals("NNP"))
            {
                tuple.table = associateTable(tuple.word);
            }
        }
    }

    /*
        Since we only have to deal with 5 nationalities,
        we just replaced each nationality with its country
     */
    private static String replaceNationality(String nationality)
    {
        if(nationality.equals("Italian"))
        {
            return "Italy";
        }
        else if(nationality.equals("German"))
        {
            return "Germany";
        }
        else if(nationality.equals("British"))
        {
            return "UK";
        }
        else if(nationality.equals("French"))
        {
            return "France";
        }
        else if(nationality.equals("American"))
        {
            return "USA";
        }
        else
        {
            return nationality;
        }
    }

    public static Tuple runSentence(String sentence)
    {
        // grab the first word that's in our sentence
        // Which Where When etc ...
        String questionWord = sentence.split(" ",2)[0];


        List<Tree> trees = parse(sentence);

        // our important data structure
        ArrayList<WordProperty> importantWords = new ArrayList<WordProperty>();

        // our oscar reward
        String award = "N/A";

        // keeps track for best ... type of rewards
        String prevWord = "";

        for(Tree tree : trees)
        {
            for(Tree subtree : tree)
            {
                // our tags that we're filtering into our importWords list
                if(subtree.label().value().equals("NNP") || subtree.label().value().equals("IN") || subtree.label().value().equals("NN")
                        || subtree.label().value().equals("VB") || subtree.label().value().equals("VBD") || subtree.label().value().equals("JJR") || subtree.label().value().equals("JJ")
                        || subtree.label().value().equals("JJS") || subtree.label().value().equals("CD") ||  subtree.label().value().equals("NNPS")
                        || subtree.label().value().equals("POS") || subtree.label().value().equals("VBN"))
                {
                    importantWords.add(new WordProperty(subtree.getChild(0).value(),subtree.label().value()));


                    if(prevWord.equals("best"))
                    {
                        // best-film award
                        if(subtree.getChild(0).value().equals("movie") || subtree.getChild(0).value().equals("film"))
                        {
                            award = "best-" + "picture";
                        }
                        else // any other best award such as actor, actress etc...
                        {
                            award = "best-" + subtree.getChild(0).value();
                        }
                    }

                    //update our previous word
                    prevWord = subtree.getChild(0).value();
                }
            }
        }

        // if our list contains a question word in the beginning, remove it from our list
        // since it is not used for our analysis
        if(importantWords.get(0).word.equals("Was") || importantWords.get(0).word.equals("Did")
        ||importantWords.get(0).word.equals("Is") || importantWords.get(0).word.equals("Who")
        || importantWords.get(0).word.equals("Which") || importantWords.get(0).word.equals("When"))
        {
            importantWords.remove(0);
        }

        // convert any words that are capitalized but not an NNP to an NNP type
        for(WordProperty x : importantWords)
        {
            if(Character.isUpperCase(x.word.charAt(0)))
            {
                x.POStag = "NNP";
            }

            if(x.POStag.equals("NNPS"))
            {
                x.POStag = "NNP";
            }
        }

        /*
            combine our NNP, POS, and CD together
            this assumption is for movie names
            for example [... , NNP, POS, NNP]
            this will combine it as Schindlier's List in our list
         */
        for(int i = 0 ; i < importantWords.size()-1; i++)
        {
            if(importantWords.get(i).POStag.equals("NNP") && importantWords.get(i+1).POStag.equals("NNP"))
            {
                importantWords.get(i+1).word = importantWords.get(i).word + " " + importantWords.get(i+1).word;
                importantWords.get(i).word = "NULL";
            }
            else if(importantWords.get(i).POStag.equals("NNP") && importantWords.get(i+1).POStag.equals("POS"))
            {
                importantWords.get(i+1).word = importantWords.get(i).word + importantWords.get(i+1).word;
                importantWords.get(i+1).POStag = "NNP";
                importantWords.get(i).word = "NULL";
            }
            else if(importantWords.get(i).POStag.equals("NNP") && importantWords.get(i+1).POStag.equals("CD"))
            {
                importantWords.get(i+1).word = importantWords.get(i).word + " " + importantWords.get(i+1).word;
                importantWords.get(i+1).POStag = "NNP";
                importantWords.get(i).word = "NULL";
            }

        }


        // finalList is our cleanedup list
        ArrayList<WordProperty> finalList = new ArrayList<WordProperty>();

        for(WordProperty x : importantWords)
        {
            if(!x.word.equals("NULL"))
            {
                finalList.add(x);
            }
        }

        // map associate words
        mapWords(finalList);

        // replace the nationality with the actual country.
        for(WordProperty x : finalList)
        {
            x.word = replaceNationality(x.word);
        }

        return new Tuple(finalList,questionWord,award);
    }
}
