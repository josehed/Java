package Cmds;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tokenizer {
    private String[] tokens;
    private String[] split;
    private int cw = 0;
//make it so that if the command contains a tab we throw an error;
    public Tokenizer(String command){
        //this functions is just here to tokenize and remove all the weird things and somehow now works.
        //come back and have another look...
        split = command.split("(?=[ ',();*!<>=])|(?<=[ ',()*;!<>=])");
        split = putBackTogether(split);
        int cnt = 0;
        for(int i = 0; i < split.length; i++){
            //takes out spaces before some strings
            split[i] = split[i].replaceAll("\\s+", "");
            if(split[i].equals("")){
                cnt++;
            }
        }
        int newlen = split.length - cnt;
        String copy[] = new String[newlen + 1]; //+1 for a buffer
        //removes empty cells
        for(int i = 0, j = 0; i < split.length; i++){
            if(!split[i].equals("")){
                copy[j] = split[i];
                j++;
            }
        }
        copy[copy.length - 1] = ""; //this is to just make the buffer cell
                                    //something so it doesn't break
        for(int i = 0; i < copy.length; i++){
            System.out.println(copy[i]);
        }
        tokens = copy;
    }

    public String nextToken(){
        if(tokens[cw] != "") {
            String nextToke = tokens[cw];
            cw++;
            return nextToke;
        }
        return "";
    }
    public String peakNextToken(){
        if(tokens[cw] != "") {
            String nextToke = tokens[cw];
            return nextToke;
        }
        return "";
    }
    public String lookBackAToken(){
        if (tokens.length > 0 && cw > 0) {
           return tokens[cw - 2]; //used 2 as when we need this we need to jump back
                                  //2 places, look at get attributenames for info.
        }
        return "shouldn't get here";
    }
    //this function puts the string back together after splitting on everything like < & =
    private String[] putBackTogether(String[] split){
        ArrayList<String> splitList = new ArrayList<String>();
        for(int i = 0; i < split.length; i++){
            if(isOpp(split[i])){
                if(i < split.length && split[i + 1].equals("=")){
                    String toke = split[i] + split[i + 1];
                    splitList.add(toke);
                    i++;
                }else{
                    splitList.add(split[i]);
                }
            }else {
                splitList.add(split[i]);
            }
        }
        String[] splitToReturn = new String[splitList.size()];
        for(int i = 0; i < splitList.size(); i++){
            splitToReturn[i] = splitList.get(i);
        }
        return splitToReturn;
    }
    private boolean isOpp(String toke){
        switch (toke){
            case "!":
            case "<":
            case ">":
            case "=":
                return true;
            default: return false;
        }
    }
}
