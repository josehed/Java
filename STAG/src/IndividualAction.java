import java.util.ArrayList;
//this method holds the information for each individual action and is
//all just getters and setters as wells as some arrayLists
public class IndividualAction {
    private ArrayList<String> triggers;
    private ArrayList<String>  subjects;
    private ArrayList<String> consumed;
    private ArrayList<String>  produced;
    private String narration;
    //setters
    public void setTriggers(ArrayList<String> triggers){
        this.triggers = triggers;
    }

    public void setSubjects(ArrayList<String> subjects){
        this.subjects = subjects;
    }

    public void setConsumed(ArrayList<String> consumed){
        this.consumed = consumed;
    }

    public void setProduces(ArrayList<String> produced){
        this.produced = produced;
    }

    public void setNarration(String narration){
        this.narration = narration;
    }
    //getters
    public ArrayList<String> getTriggers(){
        return triggers;
    }

    public ArrayList<String> getSubjects(){
        return subjects;
    }

    public ArrayList<String> getConsumed(){
        return consumed;
    }

    public  ArrayList<String> getProduced(){
        return produced;
    }

    public String getNarration(){
        return narration;
    }
}
