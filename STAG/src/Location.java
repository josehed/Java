import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import java.util.ArrayList;

public class Location {
    private Graph graph;
    private String locationName;
    private String description;
    private ArrayList<Artefacts> artefacts;
    private ArrayList<Characters> characters;
    private ArrayList<Furniture> furniture;
    public Location(Graph graph){
        this.graph = graph;
        artefacts = new ArrayList<Artefacts>();
        characters = new ArrayList<Characters>();
        furniture = new ArrayList<Furniture>();
        extractNameAndDescription();
        if(!extractData()){
            System.out.println("error with extracting data from locations");
        }
    }

    public void removeArt(Artefacts art){
        for(int i = 0; i < artefacts.size(); i++){
            if(artefacts.get(i) == art){
                artefacts.remove(i);
            }
        }
    }

    public String getDescription(){
        return description;
    }

    public String getLocationName(){
        return locationName;
    }

    public ArrayList<Artefacts> getArtefacts(){
        return artefacts;
    }

    public ArrayList<Characters> getCharacters(){
        return characters;
    }

    public ArrayList<Furniture> getFurniture(){
        return furniture;
    }

    private void extractNameAndDescription(){
        ArrayList<Node> nodesLoc = graph.getNodes(false);
        Node nLoc = nodesLoc.get(0);
        locationName = (nLoc.getId().getId());
        description = nLoc.getAttribute("description");
    }

    public void addArtefactToCurrentLocation(Artefacts art){
        artefacts.add(art);
    }

    public void addCharacterToCurrentLocation(Characters cha){
        characters.add(cha);
    }

    public void addFurnitureToCurrentLocation(Furniture furn){
        furniture.add(furn);
    }
    //this function checks which arrayList each structure should go to.
    private boolean extractData(){
        ArrayList<Graph> locationGraphs = graph.getSubgraphs();
        for (Graph g : locationGraphs){
          switch (g.getId().getId()){
              case "artefacts":
                  extractArtifacts(g);
                  break;
              case "characters":
                  extractCharacters(g);
                  break;
              case "furniture":
                  extractFurniture(g);
                  break;
              default:
                  return false;
          }
        }
        return true;
    }

    private void extractArtifacts(Graph g){
        ArrayList<Node> artsList = g.getNodes(false);
        for (Node j : artsList){
            Artefacts art = new Artefacts(j);
            artefacts.add(art);
        }
    }

    private void extractCharacters(Graph g){
        ArrayList<Node> charList = g.getNodes(false);
        for(Node j : charList){
            Characters cha = new Characters(j);
            characters.add(cha);
        }
    }

    private void extractFurniture(Graph g){
        ArrayList<Node> furnList = g.getNodes(false);
        for(Node j : furnList){
            Furniture furn = new Furniture(j);
            furniture.add(furn);
        }
    }
}
