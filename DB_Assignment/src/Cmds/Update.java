package Cmds;

import Cmds.subcmds.Condition;
import Cmds.subcmds.GetRow;
import Cmds.subcmds.NameValuePair;

import java.io.File;
import java.util.ArrayList;

public class Update extends DBCmds{
    private Tokenizer tokens;
    private Database database;
    private boolean dbInited;
    private String tableName;
    private ArrayList<String> atts;
    private ArrayList<String> pair;
    private Table table;
    private ArrayList<String> rows;
    private Condition condition;
    private static final int error = -1;
    public Update(Tokenizer tokens, Database database, boolean dbInited) {
        this.database = database;
        this.dbInited = dbInited;
        this.tokens = tokens;
        doUpdate();
    }
    private void doUpdate(){
        if(!dbInited){
            setResultError("Make sure your using a database");
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
        if(!tokens.nextToken().toLowerCase().equals("set")){
            setResultError("SET follows the Name Value.");
            return;
        }
        NameValuePair nVP = new NameValuePair();
        if(!nVP.getNameValuePairs(tokens)){
            setResultError("Error with Name Value Pairs");
            return;
        }

        pair = nVP.returnPair();
        if(!tokens.nextToken().toLowerCase().equals("where")){
            setResultError("WHERE should follow a Name Value Pair");
            return;
        }
        condition = new Condition();
        if(!condition.parseConditions(tokens)){
            setResultError("Something wrong with conditions");
            return;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ;");
            return;
        }
        if(!changeRows()){
            setResultError("Something went wrong when updating rows.");
        }


    }

    private boolean changeRows(){
        atts = condition.returnAtts();
        GetRow getRow = new GetRow();
        if(!getRow.getRows(atts, table, condition.isString())){
            return false;
        }
        rows = getRow.returnRows();
        ArrayList<String> results = new ArrayList<String>();
        int cellToChange = isAttribute(pair.get(0));
        if(cellToChange == error){
            return false;
        }
        for(int i = 0; i < rows.size(); i++){
            String newRow = "";
            String[] split = rows.get(i).split("\t");
            split[cellToChange] = pair.get(1);
            for(int j = 0; j < split.length; j++){         //this is hideous i know but hey
                newRow += split[j] + "\t";                 //she works. if you have time come
            }                                              //back and maybe change it?
            for(int j = 0; j < table.getNumRows(); j++){
                if(table.getRow(j).equals(rows.get(i))){
                    table.setRow(newRow, j);
                }
            }
        }
        table.overWriteTableFile(database.getDBName());
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
