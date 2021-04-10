package Cmds.Operators;

import Cmds.Table;

import java.util.ArrayList;

public class OppEquels {
    private int attriibnum;
    private String searchTerm;
    private Table table;
    private ArrayList<String> result;
    public OppEquels(int attriibnum, String searchTerm, Table table){
        this.attriibnum = attriibnum;
        this.searchTerm = searchTerm;
        this.table = table;
    }
    public boolean checkTable(){
        result = new ArrayList<String>();
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            if(toke[attriibnum].equals(searchTerm)){
                result.add(row);
            }
        }
        return true;
    }

    public ArrayList<String> returnQureyResults(){
        return result;
    }
}
