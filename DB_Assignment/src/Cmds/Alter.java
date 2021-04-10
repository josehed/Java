package Cmds;

import java.io.File;

public class Alter extends DBCmds{
    private Tokenizer tokens;
    private Database database;
    private boolean dbInited;
    private String tableName;
    private Table table;
    private String colName;
    public Alter(Tokenizer tokens, Database database, boolean dbInited) {
        this.tokens = tokens;
        this.database = database;
        this.dbInited = dbInited;
        doAlter();
    }
    private void doAlter(){
        if(!tokens.nextToken().toLowerCase().equals("table")){
            setResultError("Make sure TABLE comes after ALTER");
            return;
        }
        tableName = tokens.nextToken() + ".tab";
        if(!dbInited){
            setResultError("Make sure your using a database");
            return;
        }
        File fileToOpen = new File(database.getDBName() + File.separator + tableName);
        if(!fileToOpen.exists()){
            setResultError("Table doesn't exist.");
            return;
        }
        String toke = tokens.nextToken();
        colName = tokens.nextToken();
        table = database.getTable(database.getTableNumFromName(tableName));
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ;");
            return;
        }
        if(toke.toLowerCase().equals("add")){
            if(!doAdd()){
                setResultError("Something went wrong with ADD.");
                return;
            }
        }else if(toke.toLowerCase().equals("drop")){
           if(!doDrop()){
               setResultError("Something went wrong with DROP.");
           }
        }else{
            setResultError("Make sure ADD or DROP comes after the table name.");
        }
    }

    private boolean doAdd(){
        if(!isAlphanumeric(colName)){
            return false;
        }
        if(!table.addColumn(colName)){
            return false;
        }
        table.overWriteTableFile(database.getDBName());
        return true;
    }
    private boolean doDrop(){
        if(!isAlphanumeric(colName)){
            return false;
        }
        if(!table.removeColumn(colName)){
            return false;
        }
        table.overWriteTableFile(database.getDBName());
        return true;
    }
}
