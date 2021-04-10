package Cmds;

import Cmds.subcmds.ValuesList;

import java.util.ArrayList;

public class Insert extends DBCmds{
    private Tokenizer tokens;
    private boolean dbInited;
    private Database database;
    private ValuesList values;
    private String tableName;
    public Insert(Tokenizer tokens, boolean dbInited, Database database) {
        this.tokens = tokens;
        this.dbInited = dbInited;
        this.database = database;
        parseInsert();
    }
    private void parseInsert(){
        if (!tokens.nextToken().toLowerCase().equals("into")){
            setResultError("Make sure into follows insert.");
            return;
        }
        if(!dbInited){
            setResultError("Make sure your using a database.");
            return;
        }
        tableName = tokens.nextToken();
        if(!isAlphanumeric(tableName)){
            setResultError("Table names must only use alphanumeric sequences.");
            return;
        }
        tableName += ".tab";
        if(!database.tableExists(tableName)){
            setResultError("table " + tableName + " doesn't exist.");
            return;
        }
        if(!tokens.nextToken().toLowerCase().equals("values")){
            setResultError("Make sure values comes after the table name.");
            return;
        }
        if(!getValues()){
            return;
        }
    }
    private boolean getValues(){
        if(!tokens.nextToken().equals("(")){
            setResultError("missing opening bracket");
            return false;
        }
        values = new ValuesList();
        if(!values.getValuesList(tokens)){
            setResultError("Something wrong with values list.");
            return false;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("missing closing ;");
            return false;
        }
        if(values != null) {
            ArrayList vars = values.returnValuesList();
            if(!insertToTable(vars)){
                setResultError("something went wrong while setting in value's");
                return false;
            }
            //check that there the same number of columns as vars
            //add id numbers as you go
        }else {
            setResultError("something went wrong while setting in value's");
            return false;
        }
        return true;
    }

    private boolean insertToTable(ArrayList<String> vars){
        int tableNum = database.getTableNumFromName(tableName);
        if ((vars.size() + 1) != database.getNumAttributes(tableNum)){ //adding one to var.size for ID.
            return false;
        }
        int idNum = database.getNumRowsFromTable(tableNum);
        String newRow = "" + idNum +"\t";
        for(int i = 0; i < vars.size(); i++){
            newRow += vars.get(i) + "\t";
        }
        database.addToTable(newRow, tableNum);
        return true;
    }
}
