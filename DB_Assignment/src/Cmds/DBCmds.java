package Cmds;

import java.util.ArrayList;

public class DBCmds {
    private ArrayList<String> results;
    private String error;
    private ArrayList<String> AttributeNamesList = new ArrayList<String>();
    private boolean DBExists;
    public DBCmds() {
        results = new ArrayList<String>();
    }

    public void setResultError(String error){
        this.error = "[ERROR]: " + error;
    }

    public String returnError(){
        return error;
    }

    public void setResults(String toke){
        results.add(toke);
    }

    public void setDBExists(){
        DBExists = true;
    }
    public void setDBNotExists(){
        DBExists = false;
    }

    public boolean getDBExists(){
        return DBExists;
    }

    public ArrayList<String> returnResults(){
        return results;
    }

    public boolean isAlphanumeric(String toke){
        if(toke.matches("^[a-zA-Z0-9]*$")){
            return true;
        }
        return false;
    }

    public boolean isStringLiteral(String toke){
        if(toke.matches("^[ -+--~]*$")){
            return true;
        }
        return false;
    }
}
