package Cmds.Operators;

import Cmds.Table;

import java.util.ArrayList;

public class OppLessGreaterEquel {
    private int attriibnum;
    private String searchTerm;
    private Table table;
    private ArrayList<String> result;
    private String opp;
    private float searchNum;
    public OppLessGreaterEquel(int attriibnum, String searchTerm, String opp, Table table){
        this.attriibnum = attriibnum;
        this.searchTerm = searchTerm;
        this.table = table;
        this.opp = opp;
    }
    public boolean checkTable(){
        result = new ArrayList<String>();
        try{
        searchNum = Float.parseFloat(searchTerm);
        }catch (NumberFormatException nfe){
            return false;
        }
        switch (opp){
            case "<":
                if(!lessThen()){
                    return false;
                }
                break;
            case ">":
                if(!moreThen()){
                    return false;
                }
                break;
            case "<=":
                if(!lessThenOrEquel()){
                    return false;
                }
                break;
            case ">=":
                if(!moreThenOrEquel()){
                    return false;
                }
                break;
        }
        return true;
    }
    private boolean moreThenOrEquel(){
        float num;
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            try{
                num = Float.parseFloat(toke[attriibnum]);
            }catch (NumberFormatException nfe){
                return false;
            }
            if(num >= searchNum){
                result.add(row);
            }
        }
        return true;
    }

    private boolean moreThen(){
        float num;
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            try{
                num = Float.parseFloat(toke[attriibnum]);
            }catch (NumberFormatException nfe){
                return false;
            }
            if(num > searchNum){
                result.add(row);
            }
        }
        return true;
    }

    private boolean lessThen(){
        float num;
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            try{
                num = Float.parseFloat(toke[attriibnum]);
            }catch (NumberFormatException nfe){
                return false;
            }
            if(num < searchNum){
                result.add(row);
            }
        }
        return true;
    }
    private boolean lessThenOrEquel(){
        float num;
        for(int i = 1; i < table.getNumRows(); i++){ //start at one as we dont need to go over the
            String row = table.getRow(i);            //header row
            String[] toke = row.split("\t");
            try{
                num = Float.parseFloat(toke[attriibnum]);
            }catch (NumberFormatException nfe){
                return false;
            }
            if(num <= searchNum){
                result.add(row);
            }
        }
        return true;
    }

    public ArrayList<String> returnQureyResults(){
        return result;
    }
}
