package Cmds;

//look back at the end of parsedropdatabase uninitialising the database is janky

import java.io.File;
import java.io.IOException;

public class Drop extends DBCmds{
    private Tokenizer tokens;
    private boolean dbInited;
    private Database database;
    public Drop(Tokenizer tokens, boolean dbInited, Database database) throws IOException {
        this.tokens = tokens;
        this.dbInited = dbInited;
        this.database = database;
        parseDrop();
    }
    private void parseDrop() throws IOException {
        switch (tokens.nextToken().toLowerCase()){
            case "database":
                dropDatabase();
                break;
            case "table":
                dropTable();
                break;
            default: setResultError("After drop make sure you enter table or database.");
        }
    }

    private boolean dropTable(){
        String tableName = tokens.nextToken();
        if(!dbInited){
            setResultError("Make sure your in a database.");
            return false;
        }
        setDBExists();
        if(!isAlphanumeric(tableName)){
            setResultError("Table names must only use alphanumeric sequences.");
            return false;
        }
        tableName += ".tab";
        String dbName = database.getDBName();
        File table = new File(dbName + File.separator + tableName);
        if(table.exists()){
            if(!table.delete()){
                setResultError("Failed to delete table.");
                return false;
            }
        }else{
            setResultError("Table doesn't exist?");
            return false;
        }
        return true;
    }
    private boolean dropDatabase() throws IOException {
        String dbName = tokens.nextToken();
        if(!isAlphanumeric(dbName)){
            setResultError("Database names must only use alphanumeric sequences.");
            return false;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ; ?");
            return false;
        }
        File file = new File(dbName);
        if(file.exists()){
            setDBExists();
            if(!deleteFile(file, dbName)){
                setResultError("failed to delete database");
                return false;
            }
        }else{
            setResultError("Database doesn't exist.");
        }
        //if we drop the database that we are in make sure we change dbinited status;
        //this does that in a round about way. look at again
        if(dbInited && dbName.equals(database.getDBName())){
            setDBNotExists();
        }
        return true;
    }

    private boolean deleteFile(File file,String dbName){
        String[] subfiles = file.list();
        for(int i = 0; i < subfiles.length; i++){
            File subfile = new File(dbName + File.separator + subfiles[i]);
            if(!subfile.delete()){
                return false;
            }
        }
        if(!file.delete()){
            return false;
        }
        return true;
    }
}
