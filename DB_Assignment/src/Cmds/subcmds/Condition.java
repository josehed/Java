package Cmds.subcmds;

import Cmds.DBCmds;
import Cmds.Tokenizer;

import java.util.ArrayList;

public class Condition extends DBCmds {
    private ArrayList<String> conditions;
    private Tokenizer tokens;
    private boolean isString;
    public boolean parseConditions(Tokenizer tokens){
        isString = false;
        conditions = new ArrayList<String>();
        this.tokens = tokens;
      if(!parseConn()){
          return false;
      }
      return true;
    }

    private boolean parseConn(){
        IsOpp isOpp = new IsOpp();
        //checking attribute name
        String attName = tokens.nextToken();
        if(!isAlphanumeric(attName)){
            return false;
        }
        //checking opp
        String opp = tokens.nextToken();
        if(!isOpp.checkOpp(opp)){
            return false;
        }
        //checking value
        String toke = tokens.peakNextToken();
        String value = "";
        if(toke.equals("'")){
            isString = true;
            tokens.nextToken();
            while(!tokens.peakNextToken().equals("'")){
                value += tokens.nextToken() + " ";
                if(tokens.peakNextToken().equals("")){
                    return false;
                }
            }
            tokens.nextToken(); // this is to move it past the closing '
            value = value.substring(0, value.length() - 1); //taking of the extra space on the end
        }else {
            value = toke;
            tokens.nextToken(); //when we got toke we used a peak
        }
        if(!isStringLiteral(value)){
            return false;
        }
        conditions.add(attName);
        conditions.add(opp);
        conditions.add(value);
        return true;
    }

    public ArrayList<String> returnAtts(){
        return conditions;
    }

    public boolean isString(){
        return isString;
    }
}
