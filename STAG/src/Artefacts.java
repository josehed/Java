import com.alexmerz.graphviz.objects.Node;

public class Artefacts {
    private String name = null;
    private String description = null;

    public Artefacts(Node node){
        if(node.getId() != null) {
            name = (node.getId().getId());
            description = node.getAttribute("description");
        }
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
}
