package Cmds.subcmds;

import Cmds.DBCmds;
import Cmds.Tokenizer;

import java.util.ArrayList;

public class WildAttributeList extends DBCmds {
    private Tokenizer tokens;
    private ArrayList<String> wAList;
    public boolean parseWAL(Tokenizer tokens){
        this.tokens = tokens;
        wAList = new ArrayList<String>();
        if(!parse()){
            return false;
        }
        if(wAList.size() == 0){
            return false;
        }
        return true;
    }

    private boolean parse(){
        String toke = tokens.nextToken();
        if(toke.toLowerCase().equals("from")){
            return true;
        }
        if(toke.equals("")){
            return false;
        }
        if(toke.equals("*")){
            wAList.add(toke);
        }
        if(!isStringLiteral(toke)){
            return false;
        }else {
            wAList.add(toke);
        }
        if(!parse()){
            return false;
        }
        return true;
    }

    public ArrayList<String> returnWAList(){
        return wAList;
    }
}
