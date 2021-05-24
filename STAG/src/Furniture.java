import com.alexmerz.graphviz.objects.Node;

//stores the name and description from an single piece of furniture.
public class Furniture {
    private String name = null;
    private String description = null;

    public Furniture(Node node){
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
