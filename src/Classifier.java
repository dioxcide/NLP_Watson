import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import POSParsing.TreeParser;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


/**
 * Created by Tony on 4/5/2016.
 */
public class Classifier {

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

        private static String searchWordNet(String word, SynsetType type){
            NounSynset nounSynset;
            NounSynset[] hyponyms;

            WordNetDatabase database = WordNetDatabase.getFileInstance();
            Synset[] synsets = database.getSynsets(word, type);

            for (int i = 0; i < synsets.length; i++) {
                nounSynset = (NounSynset)(synsets[i]);
                hyponyms = nounSynset.getHyponyms();
                System.err.println(nounSynset.getWordForms()[0] +
                        ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
            }

            return "null";
        }

        private static void WordNetConverter(String word, String POS){
            if(POS == "NN"){
                searchWordNet(word, SynsetType.NOUN);
            }
        }

        public static void main(String[] args) {
            System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
            TreeParser temp = new TreeParser();

            String text =
            "The truck was stolen in Queretaro State and recovered in neighboring Mexico State.";

//            List<Tree> trees = parse(text);
//            for (Tree tree : trees) {
//                String parseTree = tree.toString();
//
//            }

            System.out.println(temp.getPOS(text));

//            WordNetConverter("Mexico", "NN");


        }

}
