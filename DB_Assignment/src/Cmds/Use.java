package Cmds;

import java.io.File;

public class Use extends DBCmds {
    Tokenizer tokens;
    Database database;
    public Use(Tokenizer tokens, Database database) {
        this.tokens = tokens;
        this.database = database;
        parseUse();
    }
    public boolean parseUse() {
        String dbName = tokens.nextToken();
        File data = new File(dbName);
        if(!isAlphanumeric(dbName)){
            setResultError("Database names must only use alphanumeric sequences.");
            return false;
        }
        database.setDBFile(data);
        if (data.exists() && data.isDirectory()) {
            if(tokens.nextToken().equals(";")) {
                database.loadInDB(data, dbName);
                setDBExists();
                return true;
            }
            setResultError("missing last ;");
            return false; // false as missing last;
        }
        setResultError("db doesn't exist");
        return false; // false as db doesn't exist
    }
}