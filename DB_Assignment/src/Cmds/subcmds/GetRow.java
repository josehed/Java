package Cmds.subcmds;

import Cmds.Operators.OppDoesntEquels;
import Cmds.Operators.OppEquels;
import Cmds.Operators.OppLessGreaterEquel;
import Cmds.Operators.OppLike;
import Cmds.Table;

import java.util.ArrayList;

public class GetRow {
    private static final int error = -1;
    private ArrayList<String> rows;
    private Table table;
    public boolean getRows(ArrayList<String> atts, Table table, boolean isString){
        this.table = table;
        if(atts.size() < 3){
            return false;
        }
        String attName = atts.get(0); //these just pull out the attributes from Conditions
        String opp = atts.get(1);
        String value = atts.get(2);
        int attNum = isAttribute(attName);
        if(attNum == error){
            return false;
        }
        switch (opp.toLowerCase()){
            case "==":
                OppEquels oppEq = new OppEquels(attNum, value, table);
                if(!oppEq.checkTable()){
                    return false;
                }
                rows = oppEq.returnQureyResults();
                break;
            case "!=":
                OppDoesntEquels oppDNE = new OppDoesntEquels(attNum, value, table);
                if(!oppDNE.checkTable()){
                    return false;
                }
                rows = oppDNE.returnQureyResults();
                break;
            case "like":
                OppLike oppLike = new OppLike(attNum, value, table, isString);
                if(!oppLike.checkTable()){
                    return false;
                }
                rows = oppLike.returnQureyResults();
                break;
            default:
                OppLessGreaterEquel oppOther = new OppLessGreaterEquel(attNum, value, opp, table);
                if(!oppOther.checkTable()){
                    return false;
                }
                rows = oppOther.returnQureyResults();
                break;
        }
        return true;
    }
    private int isAttribute(String attribute){
        String attribString = table.getRow(0);
        String[] attribList = attribString.split("\t");
        for(int i = 0; i < attribList.length; i++){
            if(attribList[i].equals(attribute)){
                return i;
            }
        }
        return error;
    }
    public ArrayList<String> returnRows(){
        return rows;
    }

}
