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

    public String containsApostrophe(String word){
        if(word.contains("'")){
            word = word.replaceAll("'","''");
            return word;
        }

        return word;
    }

    public boolean determineYesNoQuestion(ArrayList<WordProperty> querys, String oscarType){
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<NounTableTuple> pNouns = new ArrayList<>();
        int numberOfTables = 0;

        for(WordProperty temp: querys) {

            if (temp.POStag.equals("NNP")) {

                String searchWord = containsApostrophe(temp.word);
                boolean t1 = dbAccess.searchMovieName(searchWord);
                boolean t3 = dbAccess.searchPersonLocation(searchWord);
                boolean t4 = dbAccess.searchPersonName(searchWord);

                if (t4||t1) {
                    if(t4) {
                        if (!tables.contains("Person")) {
                            System.out.println("PersonNAME: "+temp.word);
                            tables.add("Person");

//                            if(dbAccess.searchIsActor(searchWord)){
//                                System.out.println("IS AN ACTOR");
//                                tables.add("Actor");
//                            }
                        }
                        pNouns.add(new NounTableTuple(searchWord, "Person", "name"));
                    }
                    if (t1) {
                        if (!tables.contains("Movie")) {
                            System.out.println("MOVIE NAME: "+temp.word);
                            tables.add("Movie");
                        }
                        pNouns.add(new NounTableTuple(searchWord, "Movie", "name"));
                    }
                }
                else if (t3) {
                    if (!tables.contains("Person")) {
                        System.out.println("PobNAME: "+temp.word);
                        tables.add("Person");
                    }
                    pNouns.add(new NounTableTuple(searchWord, "Person", "pob"));
                }

            }
        }

        for(WordProperty temp:querys){
            if(!temp.table.equals("N/A")){
                if (!tables.contains(temp.table)) {
                    System.out.println("ADDING :" + temp.table);
                    if(temp.table.equals("Actor") && !tables.contains("Person")) {

                        tables.add("Person");
                    }
                    if(temp.table.equals("Director") && !tables.contains("Person")) {
                        System.out.println("ADDING PERSON FROM DIRECTOR");
                        tables.add("Person");
                    }
                    tables.add(temp.table);
                }
            }
        }

        for(WordProperty temp:querys){
            if(temp.POStag.equals("CD")) {
                if (tables.contains("Oscar")) {
                    pNouns.add(new NounTableTuple(temp.word, "Oscar", "year"));
                }
                else{
                    pNouns.add(new NounTableTuple(temp.word, "Movie", "year"));
                }
            }
        }

        if(tables.contains("Oscar") && !oscarType.equals("N/A")) {
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }
        else if(!oscarType.equals("N/A") && !tables.contains("Oscar")){
            System.out.println("In here");
            tables.add("Oscar");
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }

        for(int i = 0; i<tables.size();i++){
            if(tables.get(i).equals("Movie")){
                tables.remove(i);
                tables.add("Movie");
            }
        }

        numberOfTables = tables.size();

        if(numberOfTables == 1){
            qFactory.buildTable(tables.get(0));
        }
        else if(numberOfTables == 2){
            qFactory.buildTables(tables.get(0), tables.get(1));
        }
        else if(numberOfTables == 3){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2));
        }
        else if(numberOfTables == 4){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2), tables.get(3));
        }

        qFactory.printQuery();

        return evaluateAnswer(pNouns);
    }

    public String determineWHQuestion(ArrayList<WordProperty> querys, String whType, String oscarType){
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<NounTableTuple> pNouns = new ArrayList<>();
        int numberOfTables = 0;

        for(WordProperty temp: querys) {

            if (temp.POStag.equals("NNP")) {

                String searchWord = containsApostrophe(temp.word);
                boolean t1 = dbAccess.searchMovieName(searchWord);
                boolean t3 = dbAccess.searchPersonLocation(searchWord);
                boolean t4 = dbAccess.searchPersonName(searchWord);

                if (t4||t1) {
                    if(t4) {
                        if (!tables.contains("Person")) {
                            tables.add("Person");
                        }
                        pNouns.add(new NounTableTuple(searchWord, "Person", "name"));
                    }
                    if (t1) {
                        if (!tables.contains("Movie")) {
                            tables.add("Movie");
                        }
                        pNouns.add(new NounTableTuple(searchWord, "Movie", "name"));
                    }
                }
                else if (t3) {
                    if (!tables.contains("Person")) {
                        tables.add("Person");
                    }
                    pNouns.add(new NounTableTuple(searchWord, "Person", "pob"));
                }

            }
        }

        for(WordProperty temp:querys){
            if(!temp.table.equals("N/A")){
                if (!tables.contains(temp.table)) {
                    if(temp.table.equals("Actor") && !tables.contains("Person")) {
                        tables.add("Person");
                    }
                    if(temp.table.equals("Director") && !tables.contains("Person")) {
                        tables.add("Person");
                    }
                    tables.add(temp.table);
                }
            }
        }

        for(WordProperty temp:querys){
            if(temp.POStag.equals("CD")) {
                if (tables.contains("Oscar")) {
                    pNouns.add(new NounTableTuple(temp.word, "Oscar", "year"));
                }
                else{
                    pNouns.add(new NounTableTuple(temp.word, "Movie", "year"));
                }
            }
        }

        if(tables.contains("Oscar") && !oscarType.equals("N/A")) {
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }
        else if(!oscarType.equals("N/A") && !tables.contains("Oscar")){
            System.out.println("In here");
            tables.add("Oscar");
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }

        for(int i = 0; i<tables.size();i++){
            if(tables.get(i).equals("Movie")){
                tables.remove(i);
                tables.add("Movie");
            }
        }

        numberOfTables = tables.size();

        if(whType.equals("Who") || whType.equals("Which")) {
            if(tables.contains("Person")) {
                qFactory.buildBase("Person.name");
            }
            else{
                qFactory.buildBase("Movie.name");
            }
        }
        else if(whType.equals("When")) {
            qFactory.buildBase("year");
        }

        if(numberOfTables == 1){
            qFactory.buildTable(tables.get(0));
        }
        else if(numberOfTables == 2){
            qFactory.buildTables(tables.get(0), tables.get(1));
        }
        else if(numberOfTables == 3){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2));
        }
        else if(numberOfTables == 4){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2), tables.get(3));
        }

        qFactory.printQuery();

        return evaluateWHAnswer(pNouns);
    }

    public boolean evaluateAnswer(ArrayList<NounTableTuple> pNouns){
        ArrayList<NounTableTuple> noDuplicates = new ArrayList<>();
        boolean contained = false;
        boolean answer = false;
        String query = "";

        for(NounTableTuple temp: pNouns){
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
            query = qFactory.finalQuery(noDuplicates.get(0));
        }
        else if(numberOfWheres == 2){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1));
        }
        else if(numberOfWheres == 3){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1), noDuplicates.get(2));
        }

        qFactory.printQuery();
        answer = dbAccess.executeQuery(query);

        qFactory.resetBase();
        return answer;
    }

    public String evaluateWHAnswer(ArrayList<NounTableTuple> pNouns){
        ArrayList<NounTableTuple> noDuplicates = new ArrayList<>();
        boolean contained = false;
        String answer = null;
        String query = "";

        for(NounTableTuple temp: pNouns){
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
            query = qFactory.finalQuery(noDuplicates.get(0));
        }
        else if(numberOfWheres == 2){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1));
        }
        else if(numberOfWheres == 3){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1), noDuplicates.get(2));
        }

        qFactory.printQuery();
        answer = dbAccess.executeWHQuery(query);

        qFactory.resetBase();

        return answer;
    }
}
