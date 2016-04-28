import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
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

    private static wordProperty processNER(String w){

        ArrayList<wordProperty> tupleList = new ArrayList<wordProperty>();

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        wordProperty temp = null;

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

                temp = new wordProperty(word,ne);
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

    private static void mapWords(ArrayList<wordProperty> importantWords)
    {
        for(wordProperty tuple : importantWords)
        {
            if(!tuple.POStag.equals("NNP"))
            {
                tuple.table = associateTable(tuple.word);
            }
        }
    }

    public static ArrayList<wordProperty> runSentence(String sentence)
    {
        List<Tree> trees = parse(sentence);
        ArrayList<wordProperty> importantWords = new ArrayList<wordProperty>();

        String noun;
        String currentTag = "";

        for(Tree tree : trees)
        {
            for(Tree subtree : tree)
            {
                if(subtree.label().value().equals("NNP") || subtree.label().value().equals("IN") || subtree.label().value().equals("NN")
                        || subtree.label().value().equals("VB") || subtree.label().value().equals("JJR") || subtree.label().value().equals("JJ")
                        || subtree.label().value().equals("JJS"))
                {
                    importantWords.add(new wordProperty(subtree.getChild(0).value(),subtree.label().value()));
                }
            }
        }

        mapWords(importantWords);

        for(wordProperty x : importantWords)
        {
            System.out.println(x.word + ":" + x.POStag);
        }

        return importantWords;
    }
}
