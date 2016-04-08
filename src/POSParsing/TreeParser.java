package POSParsing;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Tony on 4/8/2016.
 */
public class TreeParser {
    private List<Tree> currentTree;

    public TreeParser(){
    }

    private List<Tree> parse(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<Tree> result = new ArrayList<Tree>();
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            result.add(tree);
        }

        return result;
    }

    private ArrayList<POSTuple> createTuples(List<Tree> t){
        String delims = "[(]";

        for (Tree tree : t) {
            String parseTree = tree.toString();
            String[] tokens = parseTree.split(delims);
            System.out.println(tokens);
        }

        return null;
    }

    public ArrayList<POSTuple> getPOS(String sentence) {

        currentTree = parse(sentence);

        return createTuples(currentTree);
    }

    public List<Tree> getCurrentTree() {
        return currentTree;
    }

    public void setCurrentTree(List<Tree> currentTree) {
        this.currentTree = currentTree;
    }
}
