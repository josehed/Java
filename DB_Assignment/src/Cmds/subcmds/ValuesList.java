package Cmds.subcmds;

import Cmds.DBCmds;
import Cmds.Tokenizer;

import java.awt.*;
import java.util.ArrayList;

public class ValuesList extends DBCmds {
    private Tokenizer tokens;
    private ArrayList<String> values;
    public boolean getValuesList(Tokenizer tokens){
        this.tokens = tokens;
        values = new ArrayList<String>();
        if(!parseValues()){
            return false;
        }
        return true;
    }
    private boolean parseValues(){
        String toke = tokens.nextToken();
        if (toke.equals("'") && !tokens.peakNextToken().equals("'")){
            toke = ""; //getting rid of the first '

            while(!tokens.peakNextToken().equals("'")) {
                if(tokens.peakNextToken().equals("")){ //breaks loop if no closing '
                    return false;
                }
                toke += tokens.nextToken() + " ";
            }
            //this is to remove the extra space on the end of the string
            //sorry for magic numbers but making vars seemed unnecessary
            toke = toke.substring(0, toke.length() - 1);
            tokens.nextToken(); // so that were pointing to just after the closing '
            if(!isStringLiteral(toke)){
                return false;
            }
            values.add(toke);
        }else{
            if(isStringLiteral(toke)){
                values.add(toke);
            }
        }
        toke = tokens.nextToken();
        if(toke.equals(")")){
            return true;
        }
        if(!toke.equals(",")){
            return false;
        }
        if(!parseValues()){
            return false;
        }
        return true;
    }
    public ArrayList<String> returnValuesList(){
        return values;
    }
}
