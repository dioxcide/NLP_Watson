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

    private static WordProperty processNER(String w){

        ArrayList<WordProperty> tupleList = new ArrayList<WordProperty>();

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        WordProperty temp = null;

        // read some text from the file..
        //File inputFile = new File("src/test/resources/sample-content.txt");

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(w);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                //System.out.println("word: " + word + " ne:" + ne);

                temp = new WordProperty(word,ne);
                return temp;
            }

        }
        return temp;
    }

    private static String associateTable(String word)
    {
        String[] directorList = {"director","by","directed"};
        String[] actorList = {"actor","actress","star"};
        String[] oscarList = {"oscar"};
        String[] movieList = {"movie"};

        if(ArrayUtils.contains(directorList,word))   return     "Director";
        else if(ArrayUtils.contains(actorList,word)) return     "Actor";
        else if(ArrayUtils.contains(oscarList,word)) return     "Oscar";
        else if(ArrayUtils.contains(movieList,word)) return     "Movie";

        return "N/A";
    }

    public static String processNERWord(String w){


        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String temp = null;

        // read some text from the file..
        //File inputFile = new File("src/test/resources/sample-content.txt");

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(w);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                //System.out.println("word: " + word + " ne:" + ne);

                temp = ne;
                return temp;
            }

        }
        return temp;
    }

    private static void mapWords(ArrayList<WordProperty> importantWords)
    {
        for(WordProperty tuple : importantWords)
        {
            //tuple.NERtag = processNERWord(tuple.word);

            if(!tuple.POStag.equals("NNP"))
            {
                tuple.table = associateTable(tuple.word);
            }
        }
    }

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

    public static ArrayList<WordProperty> runSentence(String sentence)
    {
        sentence = sentence.split(" ",2)[1];
        List<Tree> trees = parse(sentence);
        ArrayList<WordProperty> importantWords = new ArrayList<WordProperty>();

        String noun;
        String currentTag = "";

        for(Tree tree : trees)
        {
            for(Tree subtree : tree)
            {
                if(subtree.label().value().equals("NNP") || subtree.label().value().equals("IN") || subtree.label().value().equals("NN")
                        || subtree.label().value().equals("VB") || subtree.label().value().equals("VBD") || subtree.label().value().equals("JJR") || subtree.label().value().equals("JJ")
                        || subtree.label().value().equals("JJS") || subtree.label().value().equals("CD") ||  subtree.label().value().equals("NNPS")
                        || subtree.label().value().equals("POS"))
                {
                    importantWords.add(new WordProperty(subtree.getChild(0).value(),subtree.label().value()));
                }
            }
        }


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

        int size = importantWords.size();

        for(int i = 0 ; i < size-1; i++)
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


        ArrayList<WordProperty> finalList = new ArrayList<WordProperty>();

        for(WordProperty x : importantWords)
        {
            if(!x.word.equals("NULL"))
            {
                finalList.add(x);
            }
        }

        mapWords(finalList);

        for(WordProperty x : finalList)
        {
            x.word = replaceNationality(x.word);
        }

        return finalList;
    }
}
