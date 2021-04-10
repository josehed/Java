package Cmds.Operators;

import Cmds.Table;

import java.util.ArrayList;

public class OppLike {
    private int attriibnum;
    private String searchTerm;
    private Table table;
    private ArrayList<String> result;
    private boolean isString;
    public OppLike(int attriibnum, String searchTerm, Table table, boolean isString){
        this.attriibnum = attriibnum;
        this.searchTerm = searchTerm;
        this.table = table;
        this.isString = isString;
    }
    public boolean checkTable(){
        result = new ArrayList<String>();
        if(!isString){
            return false;
        }
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            if(toke[attriibnum].contains(searchTerm)){
                result.add(row);
            }
        }
        return true;
    }

    public ArrayList<String> returnQureyResults(){
        return result;
    }
}
