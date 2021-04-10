package Cmds;

import java.io.File;
import java.util.ArrayList;

public class Join extends DBCmds{
    private Tokenizer tokens;
    private Database database;
    private boolean dbInited;
    private String tableName1;
    private String tableName2;
    private Table table1;
    private Table table2;
    private int attNum1;
    private int attNum2;
    private ArrayList<String> results;
    private static final int error = -1;
    public Join(Tokenizer tokens, Database database, boolean dbInited) {
        this.tokens = tokens;
        this.database = database;
        this.dbInited = dbInited;
        parseJoin();
    }
    private void parseJoin(){
        if(!dbInited){
            setResultError("Make sure your using a database");
            return;
        }
        tableName1 = tokens.nextToken() + ".tab";
        File fileToOpen1 = new File(database.getDBName() + File.separator + tableName1);
        if(!fileToOpen1.exists()){
            setResultError("Table 1 doesn't exist.");
            return;
        }
        table1 = database.getTable(database.getTableNumFromName(tableName1));
        if(!tokens.nextToken().toLowerCase().equals("and")){
            setResultError("Make sure AND comes after the first table name.");
            return;
        }
        tableName2 = tokens.nextToken() + ".tab";
        File fileToOpen2 = new File(database.getDBName() + File.separator + tableName2);
        if(!fileToOpen2.exists()){
            setResultError("Table 2 doesn't exist.");
            return;
        }
        table2 = database.getTable(database.getTableNumFromName(tableName2));
        if(!tokens.nextToken().toLowerCase().equals("on")){
            setResultError("Make sure ON comes after table name 2.");
            return;
        }
        attNum1 = isAttribute(tokens.nextToken(), table1);
        if(attNum1 == error){
            setResultError("error with first attribute.");
            return;
        }
        if(!tokens.nextToken().toLowerCase().equals("and")){
            setResultError("An AND comes after the first attribute.");
            return;
        }
        attNum2 = isAttribute(tokens.nextToken(), table2);
        if(attNum2 == error){
            setResultError("error with second attribute.");
            return;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ;");
            return;
        }
        if(!doJoin()){
            setResultError("Something went wrong while doing join.");
            return;
        }
    }

    private boolean doJoin(){
        results = new ArrayList<String>();
        for(int i = 1; i < table1.getNumRows(); i++){
            String[] split1 = table1.getRow(i).split("\t");
            String toMatch = split1[attNum1];
            for(int j = 0; j < table2.getNumRows(); j++){
                String[] split2 = table2.getRow(j).split("\t");
                if(toMatch.equals(split2[attNum2])){
                    results.add(createRow(table1.getRow(i), table2.getRow(j)));
                }
            }
        }
        if(!sortResults()){
            return false;
        }
        return true;
    }
    private String createRow(String string1, String string2){
        String resultString = "";
        resultString += (results.size() + 1) + "\t";
        String[] split1 = string1.split("\t");
        String[] split2 = string2.split("\t");
        for(int i = 1; i < split1.length; i++){
            resultString += split1[i] + "\t";
        }
        for(int i = 1; i < split2.length; i++){
            resultString += split2[i] + "\t";
        }
        return resultString;
    }
    private boolean sortResults(){
        String columnNames1 = table1.getRow(0);
        String columnNames2 = table2.getRow(0);
        String[] colsSplit1 = columnNames1.split("\t");
        String[] colsSplit2 = columnNames2.split("\t");
        String topLine = "id" + "\t";
        for(int i = 1; i < colsSplit1.length; i++){
            topLine += tableName1.substring(0, tableName1.length() - 3) + colsSplit1[i] + "\t";
        }
        for(int i = 1; i < colsSplit2.length; i++){
            topLine += tableName2.substring(0, tableName2.length() - 3) + colsSplit2[i] + "\t";
        }
        results.add(0, topLine);
        for(int i = 0; i < results.size(); i++){
            setResults(results.get(i));
        }
        return true;
    }
    private int isAttribute(String attribute, Table table){
        String attribString = table.getRow(0);
        String[] attribList = attribString.split("\t");
        for(int i = 0; i < attribList.length; i++){
            if(attribList[i].equals(attribute)){
                return i;
            }
        }
        return error;
    }
}
