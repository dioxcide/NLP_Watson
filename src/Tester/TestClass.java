package Tester;

import Labaler.Database;
import Label.Labeler;
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
            System.out.println(x.word + ":" + x.POStag);
        }
        System.out.println("\n-------------\n");
    }

    //@Test
    public void M1()
    {
        ArrayList<String> temp = new ArrayList<String>();

        temp.add("When did Berry win an oscar for best actress ?");
        temp.add("Did De Niro win the oscar in 1981 ?");
        temp.add("Did a movie with Neeson win the oscar for best film?");

        for(String x : temp)
        {
            print(Labeler.runSentence(x).labeledWordList);
        }
    }
}
