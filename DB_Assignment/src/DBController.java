import Cmds.*;

import java.io.IOException;
import java.util.ArrayList;

public class DBController {
    private Database database;
    private boolean dbInited = false;
    private ArrayList<String> result;
    public void control(String command) throws IOException {

        Tokenizer tokens = new Tokenizer(command);
        DBCmds cmd;
        switch (tokens.nextToken().toLowerCase()){
            case "use": //done?
                database = new Database();
                cmd = new Use(tokens, database);
                if(cmd.getDBExists()){           //this is a way to make sure your in a db, kinda works;
                    dbInited = true;
                }
                break;
            case "create": //done?
                cmd = new Create(tokens, database, dbInited);
                break;
            case "alter": //done?
                cmd = new Alter(tokens, database, dbInited);
                break;
            case "delete": //done?
                cmd = new Delete(tokens, database, dbInited);
                break;
            case "drop": //done?
                cmd = new Drop(tokens, dbInited, database);
                if (!cmd.getDBExists()){
                    dbInited = false;
                }
                break;
            case "insert": //done?
                cmd = new Insert(tokens, dbInited, database);
                break;
            case "join":
                cmd = new Join(tokens, database, dbInited);
                break;
            case "update": //done?
                cmd = new Update(tokens, database, dbInited);
                break;
            case "select": //done?
                cmd = new Select(tokens, database, dbInited);
                break;
            default:
                cmd = new DBCmds();
                cmd.setResultError("Please Start your query with a key word.");
        }
        setResult(cmd);
    }
    private void setResult(DBCmds cmd){
        result = new ArrayList<String>();
        if(cmd.returnError() != null){
            result.add(cmd.returnError());
        }else {
            result.add("[OK]");
            ArrayList<String> theRest = cmd.returnResults();
            for (int i = 0; i < theRest.size(); i++){                       //i added tabs to the end of every
                String noTabEnd = theRest.get(i);                           //row at some point and so am removing
                if(noTabEnd.charAt(noTabEnd.length() - 1) == '\t'){         //them here, its dumb i know but it works
                    noTabEnd = noTabEnd.substring(0, noTabEnd.length() - 1);//and i dont have time to clean up
                }                                                           //maybe come back to it later... lol
                result.add(noTabEnd);
            }
        }
    }
    public ArrayList<String> getResult(){
        return result;
    }
}
