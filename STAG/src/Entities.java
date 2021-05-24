import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;

import java.util.ArrayList;
// this class stores and sorts all of the entities data
public class Entities {
    private ArrayList<Graph> entitiesGraphs;
    private ArrayList<Location> locations;
    private ArrayList<String[]> paths;
    public Entities(ArrayList<Graph> entitiesGraphs){
        this.entitiesGraphs = entitiesGraphs;
        extractEntitieData();
    }
    //this functions breaks the entities graph down into a locations and an edges graph.
    private void extractEntitieData(){
        ArrayList<Graph> locationsGraph = entitiesGraphs.get(0).getSubgraphs();
        extractLocations(locationsGraph.get(0));
        extractEdges(locationsGraph.get(1));
    }
    //creates an instance of the location class for each individual location graph
    //and then sends one graph to each to populate it.
    private void extractLocations(Graph locationsGraph){
        ArrayList<Graph> locationsList = locationsGraph.getSubgraphs();
        locations = new ArrayList<Location>();
        for(Graph loc : locationsList){
            Location location = new Location(loc);
            locations.add(location);
        }
    }
    //extract paths from the edges graph and then add them to the paths arraylist.
    private void extractEdges(Graph e){
        ArrayList<Edge> edges = e.getEdges();
        paths = new ArrayList<String[]>();
        for (int i = 0; i < edges.size(); i++){
            String[] path = {edges.get(i).getSource().getNode().getId().getId(),
                             edges.get(i).getTarget().getNode().getId().getId()};
            paths.add(path);
        }
    }
    //return locations
    public ArrayList<Location> getLocations(){
        return locations;
    }
    //returns paths
    public ArrayList<String[]> getPaths(){
        return paths;
    }
    //returns a location class instance from a locations name
    public Location getLocationFromName(String name){
        for(Location j : locations){
            if(j.getLocationName().equals(name)){
                return j;
            }
        }
        return locations.get(0);
    }
}
