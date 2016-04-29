package MiddleTier;


import DatabaseAccess.DBAccess;
import Factory.NounTableTuple;
import Factory.QueryFactory;
import Label.WordProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Tony on 4/28/2016.
 */
public class BusinessTier {

    private QueryFactory qFactory;
    private DBAccess dbAccess;

    public BusinessTier(){
        qFactory = new QueryFactory();
        dbAccess = new DBAccess();
    }

    public boolean determineQuestion(ArrayList<WordProperty> querys){
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<NounTableTuple> pNouns = new ArrayList<>();
        String query = "";
        int numberOfTables = 0;

        for(WordProperty temp: querys) {
            System.out.println("CURR WORD: "+temp.word);
            if (temp.POStag.equals("NNP")) {

                boolean t1 = dbAccess.searchMovieName(temp.word);
//                boolean t2 = dbAccess.searchMovieYear(temp.word);
                boolean t3 = dbAccess.searchPersonLocation(temp.word);
                boolean t4 = dbAccess.searchPersonName(temp.word);

                if (t4||t1) {
                    if (t1) {
                        if (!tables.contains("Movie")) {
                            System.out.println("MOVIE NAME: "+temp.word);
                            tables.add("Movie");
                        }
                        pNouns.add(new NounTableTuple(temp.word, "Movie", "name"));
                    }
                    if(t4) {
                        if (!tables.contains("Person")) {
                            System.out.println("PersonNAME: "+temp.word);
                            tables.add("Person");
                        }
                        pNouns.add(new NounTableTuple(temp.word, "Person", "name"));
                    }
                }
                else if (t3) {
                    if (!tables.contains("Person")) {
                        System.out.println("PobNAME: "+temp.word);
                        tables.add("Person");
                    }
                    pNouns.add(new NounTableTuple(temp.word, "Person", "pob"));
                }
//                else if (t2) {
//                    if (!tables.contains("Movie")) {
//                        System.out.println("Year fucking up");
//                        tables.add("Movie");
//                    }
//                    pNouns.add(new NounTableTuple(temp.word, "Movie", "year"));
//                }

            }

            if(temp.POStag.equals("CD")){
                pNouns.add(new NounTableTuple(temp.word, "Oscar", "year"));
            }

            if(!temp.table.equals("N/A")){
                if (!tables.contains(temp.table)) {
                    System.out.println("ADDING :" + temp.table);
                    if(temp.table == "Actor" && !tables.contains("Person")) {

                        tables.add("Person");
                    }
                    tables.add(temp.table);
                }
            }
            System.out.println("TABLE: "+temp.table);
        }

        numberOfTables = tables.size();

        if(numberOfTables == 1){
            qFactory.buildTable(tables.get(0));
        }
        else if(numberOfTables == 2){
            System.out.println("RIGHT");
            qFactory.buildTables(tables.get(0), tables.get(1));
        }
        else if(numberOfTables == 3){
            System.out.println("WRONG");
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2));
        }

        qFactory.printQuery();

        return evaluateAnswer(pNouns);
    }

    public boolean evaluateAnswer(ArrayList<NounTableTuple> pNouns){
        ArrayList<NounTableTuple> noDuplicates = new ArrayList<>();
        boolean contained = false;

        for(NounTableTuple temp: pNouns){
//            noDuplicates.add(temp);
            System.out.println("DUP NOUNS: " +temp.getNoun());
            for(NounTableTuple temp2: noDuplicates){
                if(temp.getNoun().equals(temp2.getNoun())){
                    System.out.println("MATCH: "+temp.getNoun());
                    contained = true;
                }
            }

            if(!contained){
                noDuplicates.add(temp);
            }

            contained = false;
        }

        int numberOfWheres = noDuplicates.size();

        if(numberOfWheres == 1){
            qFactory.finalQuery(noDuplicates.get(0));
        }
        else if(numberOfWheres == 2){
            qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1));
        }
        else if(numberOfWheres == 3){
            qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1), noDuplicates.get(2));
        }

        qFactory.printQuery();
        qFactory.resetBase();
        return true;
    }
}
