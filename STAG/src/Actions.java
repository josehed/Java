import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Actions {
    ArrayList<IndividualAction> actions;
    public Actions(JSONArray actionGraphs){
        actions = new ArrayList<IndividualAction>();
        for(Object action : actionGraphs){
            JSONObject i = (JSONObject) action;
            ArrayList<String> triggers = (JSONArray) i.get("triggers");
            ArrayList<String>  subjects = (JSONArray) i.get("subjects");
            ArrayList<String> consumed = (JSONArray) i.get("consumed");
            ArrayList<String>  produced = (JSONArray) i.get("produced");
            String narration = (String) i.get("narration");
            IndividualAction individualAction = new IndividualAction();
            individualAction.setTriggers(triggers);
            individualAction.setSubjects(subjects);
            individualAction.setConsumed(consumed);
            individualAction.setProduces(produced);
            individualAction.setNarration(narration);
            actions.add(individualAction);
        }
    }
    public ArrayList<IndividualAction> getActions(){
        return actions;
    }
}
