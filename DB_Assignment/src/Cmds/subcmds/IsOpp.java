package Cmds.subcmds;

public class IsOpp {
    public boolean checkOpp(String opp){
        switch (opp.toLowerCase()){
            case "==":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "!=":
            case "like":
                return true;
        }
        return false;
    }
}
