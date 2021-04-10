package Cmds.subcmds;

import Cmds.DBCmds;
import Cmds.Table;
import Cmds.Tokenizer;

import java.util.ArrayList;

public class NameValuePair extends DBCmds {
    private ArrayList<String> pair;
    public boolean getNameValuePairs(Tokenizer tokens) {
        pair = new ArrayList<String>();
        String attName = tokens.nextToken();
        if(!isAlphanumeric(attName)){
            return false;
        }
        pair.add(attName);
        if(!tokens.nextToken().equals("=")){
            return false;
        }
        String toke = tokens.nextToken();
        if(toke.equals("'")){
            toke = "";
            while(!tokens.peakNextToken().equals("'")){
                toke += tokens.nextToken() + " ";
                if(tokens.peakNextToken().equals("")){
                    return false;
                }
            }
            toke = toke.substring(0, toke.length() - 1);
            tokens.nextToken();
        }
        pair.add(toke);
        return true;
    }
    public ArrayList<String> returnPair(){
        return pair;
    }
}
