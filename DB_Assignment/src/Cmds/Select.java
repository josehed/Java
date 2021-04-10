package Cmds;

import Cmds.subcmds.Condition;
import Cmds.subcmds.WildAttributeList;
import Cmds.subcmds.GetRow;

import java.io.File;
import java.util.ArrayList;

public class Select extends DBCmds{
    private Tokenizer tokens;
    private ArrayList<String> atts;
    private ArrayList<String> rows;
    private Database database;
    private String tableName;
    private Table table;
    private boolean dbInited;
    private WildAttributeList wildAttributeList;
    private Condition conditions;
    private ArrayList<String> wAList;
    private static final int error = -1;
    public Select(Tokenizer tokens, Database database, boolean dbInited) {
        this.tokens = tokens;
        this.database = database;
        this.dbInited = dbInited;
        parseSelect();
    }
    private void parseSelect(){
        if(!dbInited){
            setResultError("Make sure your using a database");
            return;
        }
        wildAttributeList = new WildAttributeList();
        if(!wildAttributeList.parseWAL(tokens)){
            setResultError("Something wrong with Wild Attribute List.");
            return;
        }
        tableName = tokens.nextToken() + ".tab";
        File fileToOpen = new File(database.getDBName() + File.separator + tableName);
        if(!fileToOpen.exists()){
            setResultError("Table doesn't exist.");
            return;
        }
        table = database.getTable(database.getTableNumFromName(tableName));
        if(table.getNumRows() == 0){
            setResultError("Table is empty.");
            return;
        }
        String toke = tokens.nextToken();
        if(toke.equals(";")){
            if(!selectAll()){
                setResultError("Error while sorting.");
            }
            return;
        }
        if(!toke.toLowerCase().equals("where")){
            setResultError("Missing WHERE statement or is in the wrong place.");
            return;
        }
        conditions = new Condition();
        if(!conditions.parseConditions(tokens)){
            setResultError("Something wrong with conditions");
            return;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ;");
            return;
        }
        atts = conditions.returnAtts();
        GetRow getRow = new GetRow();
        if(!getRow.getRows(atts, table, conditions.isString())){
            setResultError("Error with Attributes");
            return;
        }
        rows = getRow.returnRows();
        if(!sortResults()){
            setResultError("Issue with sorting results.");
        }
    }

    private boolean selectAll(){
        rows = new ArrayList<String>();
        for(int i = 1; i < table.getNumRows(); i++){ //start at one so we miss the id row
             rows.add(table.getRow(i));
        }
        if(!sortResults()){
            return false;
        }
        return true;
    }

    private boolean sortResults(){
        wAList = wildAttributeList.returnWAList();
        if(wAList.get(0).equals("*")){
            setResults(table.getRow(0));
            for(int i = 0; i < rows.size(); i++){
                setResults(rows.get(i));
            }
        }else{
            if(!selectSomeAttributes()){
                return false;
            }
        }
        return true;
    }

    private boolean selectSomeAttributes(){
        ArrayList<Integer> attnums = new ArrayList<Integer>();
        for(int i = 0; i < wAList.size(); i++){ //this get the num of all atts
            if (isAttribute(wAList.get(i)) == error){
                return false;
            }
            attnums.add(isAttribute(wAList.get(i)));
        }
        String topline = table.getRow(0);
        String[] topLineSplit = topline.split("\t");
        topline = "";
        for(int i = 0; i < attnums.size(); i++){
            topline += topLineSplit[attnums.get(i)] + "\t";
        }
        setResults(topline);
        for(int i = 0; i < rows.size(); i++){
            String row = rows.get(i);
            String[] toke = row.split("\t");
            String result = "";
            for(int j = 0; j < attnums.size(); j++){
                result += toke[attnums.get(j)] + "\t";
            }
            setResults(result);
        }
        return true;
    }

    private int isAttribute(String attribute){
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
