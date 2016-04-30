package Tester;

import Labaler.Database;
import Label.*;
import Label.WordProperty;

import java.util.ArrayList;

import org.junit.Test;


/**
 * Created by Kevin on 4/29/16.
 */
public class TestClass {

    public void print(ArrayList<WordProperty> list)
    {
        System.out.println("\n-------------\n");
        for(WordProperty x : list)
        {
            System.out.println(x.word + ":" + x.POStag );
        }
        System.out.println("\n-------------\n");
    }
    //@Test
    public void M1()

    {
        ArrayList<String> temp = new ArrayList<String>();


        temp.add("When did Blanchett win an oscar for best actress?");
        temp.add("Who won the oscar for best actor in 2005?");
        temp.add("Was Birdman the best movie in 2015?");


        for(String x : temp)
        {
            Tuple tuple = Labeler.runSentence(x);

            print(tuple.labeledWordList);
            System.out.println("AWARD: " + tuple.oscarType);

        }
    }
}
