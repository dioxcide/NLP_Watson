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
        ArrayList<WordProperty> temp = Labeler.runSentence("Did a French actor win the oscar in 2012?");

        print(temp);

        temp = Labeler.runSentence("Did a British actor win the oscar in 2012?");

        print(temp);


        temp = Labeler.runSentence("Did a American actor win the oscar in 2012?");

        print(temp);


        temp = Labeler.runSentence("Did a Italian actor win the oscar in 2012?");

        print(temp);


        temp = Labeler.runSentence("Did a German actor win the oscar in 2012?");

        print(temp);



    }
}
