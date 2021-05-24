import com.alexmerz.graphviz.objects.Node;

//simple class which stores each characters name and description.

public class Characters {
    private String name = null;
    private String description = null;
    public Characters(Node node){
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
