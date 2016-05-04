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

    //Constructor initializes QueryFactory and DBAccess
    public BusinessTier(){
        qFactory = new QueryFactory();
        dbAccess = new DBAccess();
    }

    /*
        The following method adds a second apostrophe to the string being passed in.
        The method was created in order to include apostrophes in SQL where clauses.
        For example Schindler's List wouldnt work. Instead here we would need Schindler''s List
     */
    public String containsApostrophe(String word){
        if(word.contains("'")){
            word = word.replaceAll("'","''");
            return word;
        }

        return word;
    }

    /*
        What this method does is essentially grab all the tables that are needed to answer the questions.
        Afterwards it constructs the first half of the SQL statement without the where clauses.
     */
    public boolean determineYesNoQuestion(ArrayList<WordProperty> querys, String oscarType){
        ArrayList<String> tables = new ArrayList<>();               //Stores tables
        ArrayList<NounTableTuple> pNouns = new ArrayList<>();          //Stores proper nouns aka for the where clauses
        int numberOfTables = 0;

        for(WordProperty temp: querys) {                            //Goes through all the words in the questions
            if (temp.POStag.equals("NNP")) {                        //Queries the database for proper nouns

                String searchWord = containsApostrophe(temp.word);
                String t1 = dbAccess.searchMovieName(searchWord);
                boolean t3 = dbAccess.searchPersonLocation(searchWord);
                boolean t4 = dbAccess.searchPersonName(searchWord);
                                                                                    //If a match is found the table
                if (t4|| !t1.equals("N/A")) {                                       //that is hit is added to the
                    if(t4) {                                                        //array list above
                        if (!tables.contains("Person")) {
                            tables.add("Person");

                        }
                        pNouns.add(new NounTableTuple(searchWord, "Person", "name"));
                    }
                    if (!t1.equals("N/A") && t1.contains(temp.word)) {
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

        for(WordProperty temp:querys){                                              //For loop adds more relevant tables
            if(!temp.table.equals("N/A")){                                          //that we have identified from
                if (!tables.contains(temp.table)) {                                 //labeler class
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

        for(WordProperty temp:querys){                                              //Determines what type of year we
            if(temp.POStag.equals("CD")) {                                          //are going to be looking up
                if (tables.contains("Oscar")) {                                     //I.E. either Oscar.year or Movie.year
                    pNouns.add(new NounTableTuple(temp.word, "Oscar", "year"));
                }
                else{
                    pNouns.add(new NounTableTuple(temp.word, "Movie", "year"));
                }
            }
        }

        if(tables.contains("Oscar") && !oscarType.equals("N/A")) {                  //Adds specific oscar types
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));             //to the pNouns arraylist
        }                                                                           //it essentially adds on to the
        else if(!oscarType.equals("N/A") && !tables.contains("Oscar")){             //where clauses that need to be in the
            tables.add("Oscar");                                                    //query
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }

        for(int i = 0; i<tables.size();i++){                                        //Makes sure to have the Movie table
            if(tables.get(i).equals("Movie")){                                      //last in the array list because of
                tables.remove(i);                                                   //inner joining purposes
                tables.add("Movie");
            }
        }

        numberOfTables = tables.size();

        if(numberOfTables == 1){                                                    //Determines which overloaded method
            qFactory.buildTable(tables.get(0));                                     //needs to be used in order to construct
        }                                                                           //the first half of the SQL statement
        else if(numberOfTables == 2){
            qFactory.buildTables(tables.get(0), tables.get(1));
        }
        else if(numberOfTables == 3){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2));
        }
        else if(numberOfTables == 4){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2), tables.get(3));
        }

        return evaluateAnswer(pNouns);                                              //Returns a boolean if the question
    }                                                                               //Is true or false


    /*
        What this method does is essentially grab all the tables that are needed to answer the questions.
        Afterwards it constructs the first half of the SQL statement without the where clauses.
        Note: This method is specifically designed for the Wh- Questions
     */
    public String determineWHQuestion(ArrayList<WordProperty> querys, String whType, String oscarType){
        ArrayList<String> tables = new ArrayList<>();                       //Stores tables
        ArrayList<NounTableTuple> pNouns = new ArrayList<>();               //Stores proper nouns or relevant
        int numberOfTables = 0;                                             //words for the where clauses

        for(WordProperty temp: querys) {                                    //For loop iterates through the words

            if (temp.POStag.equals("NNP")) {                                //For proper nouns we query the database
                                                                            //and store the hit table
                String searchWord = containsApostrophe(temp.word);
                String t1 = dbAccess.searchMovieName(searchWord);
                boolean t3 = dbAccess.searchPersonLocation(searchWord);
                boolean t4 = dbAccess.searchPersonName(searchWord);

                if (t4|| !t1.equals("N/A")) {                               //If it hits the person or movie table
                    if(t4) {                                                //the table is added to the array list
                        if (!tables.contains("Person")) {                   //and the proper noun is added as a where
                            tables.add("Person");                           //clause to the pNouns arraylist
                        }
                        pNouns.add(new NounTableTuple(searchWord, "Person", "name"));
                    }
                    if (!t1.equals("N/A") && t1.contains(temp.word)) {
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

        for(WordProperty temp:querys){                                      //For loop adds more relevant tables
            if(!temp.table.equals("N/A")){                                  //that we have identified from
                if (!tables.contains(temp.table)) {                         //labeler class
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

        for(WordProperty temp:querys){                                      //Determines what type of year we
            if(temp.POStag.equals("CD")) {                                  //are going to be looking up
                if (tables.contains("Oscar")) {                             //I.E. either Oscar.year or Movie.year
                    pNouns.add(new NounTableTuple(temp.word, "Oscar", "year"));
                }
                else{
                    pNouns.add(new NounTableTuple(temp.word, "Movie", "year"));
                }
            }
        }

        if(tables.contains("Oscar") && !oscarType.equals("N/A")) {              //Adds specific oscar types
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));         //to the pNouns arraylist
        }                                                                       //it essentially adds on to the
        else if(!oscarType.equals("N/A") && !tables.contains("Oscar")){         //where clauses that need to be in the
            tables.add("Oscar");                                                //query
            pNouns.add(new NounTableTuple(oscarType, "Oscar", "type"));
        }

        for(int i = 0; i<tables.size();i++){                                    //Makes sure to have the Movie table
            if(tables.get(i).equals("Movie")){                                  //last in the array list because of
                tables.remove(i);                                               //inner joining purposes
                tables.add("Movie");
            }
        }

        numberOfTables = tables.size();

        if(whType.equals("Who") || whType.equals("Which")) {                    //Depending on the type of Wh- question
            if(tables.contains("Person")) {                                     //The following will determine which column
                qFactory.buildBase("Person.name");                              //Or type of string that we need to return
            }
            else{
                qFactory.buildBase("Movie.name");
            }
        }
        else if(whType.equals("When")) {
            qFactory.buildBase("year");
        }

        if(numberOfTables == 1){                                                //Again determines which overloaded method
            qFactory.buildTable(tables.get(0));                                 //we need to use in order to construct the
        }                                                                       //SQL method
        else if(numberOfTables == 2){
            qFactory.buildTables(tables.get(0), tables.get(1));
        }
        else if(numberOfTables == 3){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2));
        }
        else if(numberOfTables == 4){
            qFactory.buildTables(tables.get(0), tables.get(1), tables.get(2), tables.get(3));
        }

        return evaluateWHAnswer(pNouns);                //Returns a string for the answer
    }

    /*
            This method is designed to return a boolean depending if the sql query that is being constructed
            is true or false. The method also finished constructing the 2nd of the SQL query aka the where clauses.
     */
    public boolean evaluateAnswer(ArrayList<NounTableTuple> pNouns){
        ArrayList<NounTableTuple> noDuplicates = new ArrayList<>();
        boolean contained = false;
        boolean answer = false;
        String query = "";

        for(NounTableTuple temp: pNouns){                                   //Double for loop double checks for duplicate where clauses
            for(NounTableTuple temp2: noDuplicates){                        //Also removes them
                if(temp.getNoun().equals(temp2.getNoun())){
                    contained = true;
                }
            }

            if(!contained){
                noDuplicates.add(temp);
            }

            contained = false;
        }

        int numberOfWheres = noDuplicates.size();

        if(numberOfWheres == 1){                                            //Determines the correct overloaded method
            query = qFactory.finalQuery(noDuplicates.get(0));               //that we need in order to construct
        }                                                                   //the 2nd half of our query aka the where
        else if(numberOfWheres == 2){                                       //clauses
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1));
        }
        else if(numberOfWheres == 3){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1), noDuplicates.get(2));
        }

        qFactory.printQuery();                                              //Prints the final query
        answer = dbAccess.executeQuery(query);                              //Executes it on the DB and returns the answer

        qFactory.resetBase();                                               //Resets the base query
        return answer;                                                      //Returns the answer
    }

    /*
            This method is designed to return a string depending if the sql query that is being constructed
            is true or false. The method also finished constructing the 2nd of the SQL query aka the where clauses.
     */
    public String evaluateWHAnswer(ArrayList<NounTableTuple> pNouns){
        ArrayList<NounTableTuple> noDuplicates = new ArrayList<>();
        boolean contained = false;
        String answer = null;
        String query = "";

        for(NounTableTuple temp: pNouns){                       //Double for loop double checks for duplicate where clauses
            for(NounTableTuple temp2: noDuplicates){            //Also removes them
                if(temp.getNoun().equals(temp2.getNoun())){
                    contained = true;
                }
            }

            if(!contained){
                noDuplicates.add(temp);
            }

            contained = false;
        }

        int numberOfWheres = noDuplicates.size();

        if(numberOfWheres == 1){                                    //Determines the correct overloaded method
            query = qFactory.finalQuery(noDuplicates.get(0));       //that we need in order to construct
        }                                                           //the 2nd half of our query aka the where
        else if(numberOfWheres == 2){                               //clauses
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1));
        }
        else if(numberOfWheres == 3){
            query = qFactory.finalQuery(noDuplicates.get(0), noDuplicates.get(1), noDuplicates.get(2));
        }

        qFactory.printQuery();                                      //Prints the final query
        answer = dbAccess.executeWHQuery(query);                    //Specifically executes on the WHQuery method because
                                                                    //this one returns a string
        qFactory.resetBase();                                       //Resets the base SQL statement

        return answer;                                              //Returns the answer
    }
}
