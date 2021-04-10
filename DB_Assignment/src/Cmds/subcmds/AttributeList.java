package Cmds.subcmds;

import Cmds.DBCmds;
import Cmds.Tokenizer;

import java.util.ArrayList;

public class AttributeList extends DBCmds {
    private ArrayList<String> AttributeNames;
    private boolean isName = true;
    public boolean getAttributes(Tokenizer tokens) {
        AttributeNames = new ArrayList<String>();
        tokens.nextToken(); // should be pointing at first name;
        if (!getAttributeNames(tokens)) {
            return false;
        }
        if (AttributeNames.size() == 0){
            return false;
        }
        //check to make sure they have added some attributes
        return true;
    }

    private boolean getAttributeNames(Tokenizer tokens){
        String toke = tokens.nextToken();
        //maybe come back and look at this function as shes a mess.... but works, i think
        if(toke.equals("")){
            return false;
        }
        if(toke.equals(")") && !tokens.lookBackAToken().equals(",")){
            return true;
        }
        else if(isAlphanumeric(toke) && isName == true){
            isName = false;
            AttributeNames.add(toke);
            if(getAttributeNames(tokens)) {
                return true;
            }
        }
        else if(toke.equals(",") && isName == false){
            isName = true;
            if(getAttributeNames(tokens)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> returnAttributeNames(){
        return AttributeNames;
    }
}
