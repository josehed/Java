package Cmds;
//fix needed around line 66 in parse table
import Cmds.subcmds.AttributeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Create extends DBCmds{
    private Tokenizer tokens;
    private Database database;
    private boolean dbInited;
    private String tableName;
    private AttributeList attributes;
    public Create(Tokenizer tokens, Database database, boolean dbInited) throws IOException {
        this.tokens = tokens;
        this.database = database;
        this.dbInited = dbInited;
        parseCreate();
    }
    private void parseCreate() throws IOException {
        switch (tokens.nextToken().toLowerCase()){
            case "database":
                parseDatabase();
                break;
            case "table":
                parseTable();
                break;
            default:
                setResultError("please put table or database after Create");
        }
    }

    private void parseDatabase(){
        String dbname = tokens.nextToken();
        if(!isAlphanumeric(dbname)){
            setResultError("Database names must only use alphanumeric sequences.");
            return;
        }
        File dbfile = new File(dbname);
        if(dbfile.exists()){
            setResultError("database already exists");
            return;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("missing closing ;");
        }else if (!dbfile.mkdir()) {
            setResultError("failed to make directory");
        }
    }

    private void parseTable() throws IOException {
        tableName = tokens.nextToken();
        if(!dbInited){
            setResultError("make sure your using a database.");
            return;
        }
        if(!isAlphanumeric(tableName)){
            setResultError("Table names must only use alphanumeric sequences.");
            return;
        }
        tableName += ".tab";
        if(tokens.peakNextToken().equals("(")) {
           attributes = new AttributeList();
            if(!attributes.getAttributes(tokens)){
                setResultError("something wrong with Attributes list.");
                return;
            }
        }
        // fix to do here, a lot of errors here say missing a ; when its something else.
        File dbfile = new File(database.getDBName() + File.separator + tableName);
        //checks to make sure the file is valid
        if(!tokens.nextToken().equals(";")){
            setResultError("missing end ;");
            return;
        }
        if(dbfile.exists()){
            setResultError("table " + tableName + " already exists");
            return;
        } else{
            dbfile.createNewFile();
        }
        if(attributes != null) {
            ArrayList names = attributes.returnAttributeNames();
            database.loadInDB(database.getDBFile(), database.getDBName());
            int tableNum = database.getTableNumFromName(tableName);
            String newRow = "id\t";
            for (int i = 0; i < names.size(); i++) {
                newRow = newRow + names.get(i) + "\t";
            }
            database.addToTable(newRow, tableNum);
        }
    }
}
