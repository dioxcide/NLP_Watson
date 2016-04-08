package POSParsing;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Tony on 4/8/2016.
 */
public class NERParser {

    public NERParser(){

    }

    private ArrayList<NERTuple> processNER(String w){
        ArrayList<NERTuple> tupleList = new ArrayList<>();

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text from the file..
//        File inputFile = new File("src/test/resources/sample-content.txt");

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

                System.out.println("word: " + word + " ne:" + ne);

                NERTuple temp = new NERTuple(ne, word);
                tupleList.add(temp);
            }

        }

        return tupleList;
    }
}
