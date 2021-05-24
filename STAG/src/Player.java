import java.util.ArrayList;

public class Player {
    private String platerName = null;
    private ArrayList<Artefacts> artefacts;
    private int health = 3;
    private Location currentLocation;

    public Player(String playerName){
        this.platerName = playerName;
        artefacts = new ArrayList<Artefacts>();
    }

    public String getPlaterName(){
        return platerName;
    }

    public void addToInf(Artefacts art){
        artefacts.add(art);
    }

    public ArrayList<Artefacts> getArifacsts(){
        return artefacts;
    }

    public void removeArtefactFromInv(Artefacts art){
        for(int i = 0; i < artefacts.size(); i++){
            if(artefacts.get(i) == art){
                artefacts.remove(i);
                return;
            }
        }
    }

    public void setCurrentLocation(Location location){
        this.currentLocation = location;
    }

    public Location getPlayerLocation(){
        return currentLocation;
    }

    public void takeDamage(){
        health--;
    }

    public void addHealth(){
        health++;
    }

    public String getHealth(){
        String live = String.valueOf(health);
        return  live;
    }

    public void resetHealth(){
        health = 3;
    }
}
