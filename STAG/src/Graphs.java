import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
//this class uses a json and dot parser to parse the files which are passed to it.
public class Graphs {
    private String entityFilename;
    private String actionFilename;
    private ArrayList<Graph> entities;
    private JSONArray actions;
    public Graphs(String entityFilename, String actionFilename){
        this.entityFilename = entityFilename;
        this.actionFilename = actionFilename;
        checkArgs();
    }

    private void checkArgs(){
            parseDot();
            parseJson();
    }

    private void parseDot(){
        try{
        Parser parser = new Parser();
        FileReader reader = new FileReader(entityFilename);
        parser.parse(reader);
        entities = parser.getGraphs();
        } catch (FileNotFoundException fnfe) {
        System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
        System.out.println(pe);
        }
    }

    public ArrayList<Graph> returnEntities(){
        return entities;
    }

    private void parseJson(){
            JSONParser parser = new JSONParser();
            try {
                FileReader reader = new FileReader(actionFilename);
                JSONObject object = (JSONObject) parser.parse(reader);
                actions = (JSONArray) object.get("actions");
            } catch (Exception e) {
                System.err.println(e);
            }
    }

    public JSONArray returnActions(){
        return actions;
    }
}

