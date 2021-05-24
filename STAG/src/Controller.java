import com.alexmerz.graphviz.objects.Graph;
import org.json.simple.JSONArray;

import java.util.ArrayList;
public class Controller {
    private ArrayList<String> results;
    private ArrayList<Graph> entitiesGraphs;
    private JSONArray actionsGraphs;
    private ArrayList<Location> locations;
    private Location currentLocation;
    private Entities entities;
    private ArrayList<String[]> paths;
    private ArrayList<Player> players = null;
    private Player currentPlayer;
    private ArrayList<IndividualAction> actionsList;
    private Tokenizer tokenizer;
    public Controller(String entityFilename, String actionFilename){
        Graphs graphs = new Graphs(entityFilename, actionFilename);
        entitiesGraphs = graphs.returnEntities();
        actionsGraphs = graphs.returnActions();
        entities = new Entities(entitiesGraphs);
        paths = entities.getPaths();
        Actions actions = new Actions(actionsGraphs);
        actionsList = actions.getActions();
        locations = entities.getLocations();
        players = new ArrayList<Player>();
    }
    //checks for key words in command and then executes if allowed
    //if no key word is found then we check if a trigger word is present at then end
    public void doNextCommand(String line){
        tokenizer = new Tokenizer(line);
        currentPlayer = checkPlayer(line);
        currentLocation = currentPlayer.getPlayerLocation();
        results = new ArrayList<String>();
        int cmdCnt = 0;
        if(tokenizer.myContainsLower("look")){
            doLook();
            cmdCnt++;
        }
        if(tokenizer.myContainsLower("goto")){
            if(doGoTo()) {
                doLook();
            }
            cmdCnt++;
        }
        if (tokenizer.myContainsLower("get")){
            doGet();
            cmdCnt++;
        }
        if (tokenizer.myContainsLower("drop")) {
            doDrop();
            cmdCnt++;
        }
        if(tokenizer.myContainsLower("inv") || tokenizer.myContains("inventory")){
            doInv();
            cmdCnt++;
        }
        if(tokenizer.myContainsLower("health")){
            results.add("Your current level of health is: " + currentPlayer.getHealth());
            cmdCnt++;
        }
        if(checkForAction()){
            cmdCnt++;
        }
        if(cmdCnt != 1) {
            results = new ArrayList<String>();
            results.add("Invalid command.");
            return;
        }
        currentPlayer.setCurrentLocation(currentLocation);
        checkPlayerHealth();
    }
    //checks players health at the end of each go and if it is 0 all of the inv
    //is dropped in the current location and the players location is reset
    private void checkPlayerHealth(){
        if(currentPlayer.getHealth().equals("0")){
            results = new ArrayList<String>();
            results.add("You are dead.");
            ArrayList<Artefacts> inv = currentPlayer.getArifacsts();
            System.out.println(inv.size());
            for(int i = 0; i < inv.size(); i++){
                currentLocation.addArtefactToCurrentLocation(inv.get(i));
            }
            System.out.println(inv.size());
            for(int i = inv.size() - 1; i >= 0; i--){
                inv.remove(i);
            }
            currentPlayer.resetHealth();
            currentPlayer.setCurrentLocation(locations.get(0));
        }
    }
    //checks for a trigger word and then check to make sure all subjects are present
    //consume and produce are then enacted.
    private boolean checkForAction(){
        IndividualAction thisAction = checkTriggers();
        if(thisAction.getTriggers() == null){
            return false;
        }
        if(!checkSubjects(thisAction.getSubjects())){
            return false;
        }
        doConsumed(thisAction.getConsumed());
        doProduce(thisAction.getProduced());
        results.add(thisAction.getNarration());

        return true;
    }

    private void doProduce(ArrayList<String> produced){
        produceTheGoods(produced);
        producePaths(produced);
    }
    //this function produces the paths that maybe needed but also checks that they dont already exist
    private void producePaths(ArrayList<String> produced){
        for(int i = 0; i < paths.size(); i++){
            for(String k : produced){
                if(paths.get(i)[0].equals(k) && currentLocation.getLocationName().equals(paths.get(i)[1])){
                    String[] newPath = {currentLocation.getLocationName(), k};
                    if(checkPathDosentAlraedyExist(newPath)) {
                        paths.add(newPath);
                    }
                }
            }
        }
    }

    private boolean checkPathDosentAlraedyExist(String[] newPath){
        for(String[] j : paths){
            if(newPath[0].equals(j[0]) && newPath[1].equals(j[1])){
                return false;
            }
        }
        return true;
    }

    private void produceTheGoods(ArrayList<String> produced){
        Location unplaced = locations.get(locations.size() - 1);
        ArrayList<Artefacts> unplacedArtefacts = unplaced.getArtefacts();
        ArrayList<Characters> unplacedCharacters = unplaced.getCharacters();
        ArrayList<Furniture> unplacedFurniture = unplaced.getFurniture();
        //this looks through all of the unplaced artefacts
        for(String j : produced){
            if(j.toLowerCase().equals("health")){
                currentPlayer.addHealth();
            }
        }
        for(String j : produced){
            for(int i = 0; i < unplacedArtefacts.size(); i++){
                if(j.equals(unplacedArtefacts.get(i).getName())){
                    currentLocation.addArtefactToCurrentLocation(unplacedArtefacts.get(i));
                    unplacedArtefacts.remove(unplacedArtefacts.get(i));
                }
            }
        }
        //this looks through all of the unplaced characters
        for(String j : produced){
            for(int i = 0; i < unplacedCharacters.size(); i++){
                if(j.equals(unplacedCharacters.get(i).getName())){
                    currentLocation.addCharacterToCurrentLocation(unplacedCharacters.get(i));
                    unplacedCharacters.remove(unplacedCharacters.get(i));
                }
            }
        }
        //this looks through all of the unplaced furniture
        for(String j : produced){
            for(int i = 0; i < unplacedFurniture.size(); i++){
                if(j.equals(unplacedFurniture.get(i).getName())){
                    currentLocation.addFurnitureToCurrentLocation(unplacedFurniture.get(i));
                    unplacedFurniture.remove(unplacedFurniture.get(i));
                }
            }
        }
        //this looks through all of the locations for artifacts what may need to be moved
        lookThroughLocations(produced);
    }

    private void lookThroughLocations(ArrayList<String> produced){
        //looks looks through artifacts at locations to check when they are summoned like the lumberjack.
        int cnt = 0;
        for(int i = 0; i < locations.size(); i++){
            ArrayList<Artefacts> locationArtifacts = locations.get(i).getArtefacts();
            for(int j = 0; j < locationArtifacts.size(); j++){
                for(String k : produced){
                    if(locationArtifacts.get(j).getName().equals(k)){
                        currentLocation.addArtefactToCurrentLocation(locationArtifacts.get(j));
                        locationArtifacts.remove(j);
                    }
                }
            }
        }
        for(int i = 0; i < locations.size(); i++){
            ArrayList<Characters> locationCharacters = locations.get(i).getCharacters();
            for(int j = 0; j < locationCharacters.size(); j++){
                for(String k : produced){
                    if(locationCharacters.get(j).getName().equals(k)){
                        currentLocation.addCharacterToCurrentLocation(locationCharacters.get(j));
                        locationCharacters.remove(j);
                    }
                }
            }
        }
    }

    private void doConsumed(ArrayList<String> consumed){
        for(String j : consumed){
            if(j.toLowerCase().equals("health")){
                currentPlayer.takeDamage();
            }
        }
        consumeArtefacts(consumed);
        consumeFurnature(consumed);
        consumePlayerArtefacts(consumed);
        for(int i = 0; i < paths.size(); i++){
            for(String j : consumed){
                if(paths.get(i)[1].equals(j)){
                    paths.remove(i);
                }
            }
        }
    }

    private void consumePlayerArtefacts(ArrayList<String> consumed){
        ArrayList<Artefacts> currentPlayerArtefacts = currentPlayer.getArifacsts();
        for(int i = 0; i < currentPlayerArtefacts.size(); i++){
            for(String j : consumed){
                if(currentPlayerArtefacts.get(i).getName().equals(j)){
                    currentPlayerArtefacts.remove(i);
                }
            }
        }
    }

    private void consumeFurnature(ArrayList<String> consumed){
        ArrayList<Furniture> currentLocationFurniture = currentLocation.getFurniture();
        for(int i = 0; i < currentLocationFurniture.size(); i++){
            for(String j : consumed){
                if(currentLocationFurniture.get(i).getName().equals(j)){
                    currentLocationFurniture.remove(i);
                }
            }
        }
    }

    private void consumeArtefacts(ArrayList<String> consumed){
        ArrayList<Artefacts> currentLocationArtefacts = currentLocation.getArtefacts();
        for(int i = 0; i < currentLocationArtefacts.size(); i++){
            for(String j : consumed){
                if(currentLocationArtefacts.get(i).getName().equals(j)){
                    currentLocationArtefacts.remove(i);
                }
            }
        }
    }

    private boolean checkSubjects(ArrayList<String> thisActionSubjects){
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<Artefacts> locationArtifacts = currentLocation.getArtefacts();
        for(Artefacts j : locationArtifacts){
            subjects.add(j.getName());
        }
        ArrayList<Artefacts> playerArtifacts = currentPlayer.getArifacsts();
        for(Artefacts j : playerArtifacts){
            subjects.add(j.getName());
        }
        ArrayList<Characters> charsAtLocation = currentLocation.getCharacters();
        for(Characters j : charsAtLocation){
            subjects.add(j.getName());
        }
        ArrayList<Furniture> furnAtLocation = currentLocation.getFurniture();
        for(Furniture j : furnAtLocation){
            subjects.add(j.getName());
        }
        int numSubsRequired = thisActionSubjects.size();
        int numSubs = 0;
        for(String k : thisActionSubjects){
            for(String j : subjects){
                if(j.equals(k)){
                    numSubs++;
                }
            }
        }
        if(!checkCommandContainsSubject(subjects)){
            return false;
        }
        if(numSubsRequired == numSubs){
            return true;
        }
        return false;
    }

    private boolean checkCommandContainsSubject(ArrayList<String> subjects){
        for(int i = 0; i < subjects.size(); i++){
            if(tokenizer.myContains(subjects.get(i))){
                return true;
            }
        }
        return false;
    }

    private IndividualAction checkTriggers(){
        IndividualAction error = new IndividualAction();
        for(IndividualAction j : actionsList){
            ArrayList<String> triggers = j.getTriggers();
            for(String i : triggers){
                if(tokenizer.myContains(i)){
                    return j;
                }
            }
        }
        return error;
    }

    private void doInv(){
        ArrayList<Artefacts> arts = currentPlayer.getArifacsts();
        if(arts.size() == 0){
            results.add("Your inventory is empty.");
            return;
        }
        results.add("In your Inventory you have:");
        for(Artefacts j : arts){
            results.add(j.getDescription());
        }
    }

    private void doDrop(){
        ArrayList<Artefacts> artifacts = currentPlayer.getArifacsts();
        boolean dropSuccess = false;
        for(int i = 0; i < artifacts.size(); i++){
            if(tokenizer.myContains(artifacts.get(i).getName().toLowerCase())){
                results.add(artifacts.get(i).getDescription());
                currentLocation.addArtefactToCurrentLocation(artifacts.get(i));
                currentPlayer.removeArtefactFromInv(artifacts.get(i));
                dropSuccess = true;
            }
        }
        if(!dropSuccess){
            results = new ArrayList<String>();
            results.add("That item isn't in your inventory.");
        }
    }

    private void doGet(){
        ArrayList<Artefacts> artifacts = currentLocation.getArtefacts();
        results.add("You have picked up ");
        boolean getSuccess = false;
        for(int i = 0; i < artifacts.size(); i++){
            if(tokenizer.myContains(artifacts.get(i).getName().toLowerCase())){
                results.add(artifacts.get(i).getDescription());
                currentPlayer.addToInf(artifacts.get(i));
                currentLocation.removeArt(artifacts.get(i));
                getSuccess = true;
            }
        }
        if(!getSuccess){
            results = new ArrayList<String>();
            results.add("That object doesn't exist here or cannot be picked up.");
        }
    }

    private void doLook(){
        results.add("You are in " + currentLocation.getDescription());
        results.add("You can see:");
        getWhatYouCanSee();
        getOtherPlayerNames();
        results.add("You can access from here:");
        getWhatCanYouAccess();
    }

    private void getOtherPlayerNames(){
        for(Player j : players){
            if(j.getPlayerLocation().getLocationName().equals(currentLocation.getLocationName()) &&
                    j.getPlaterName() != currentPlayer.getPlaterName()){ //what even is this, fix it.
                results.add("Another player named " + j.getPlaterName());
            }
        }
    }

    private boolean doGoTo(){
        boolean locationChanged = false;
        ArrayList<String> posLocations = new ArrayList<String>();
        for(String[] j : paths){
            if(j[0].equals(currentLocation.getLocationName())){
                posLocations.add(j[1]);
            }
        }
        if(posLocations.size() == 0){
            results.add("You have no paths to chose from.");
        }
        for(String j : posLocations){
            if(tokenizer.myContains(j)){
//                currentPlayer.setCurrentLocation(entities.getLocationFromName(j)); look at this tomorrow
                currentLocation = entities.getLocationFromName(j);
                locationChanged = true;
            }
        }
        if(locationChanged == false){
            results.add("Locations doesn't exist.");
            return false;
        }
        return true;
    }

    private void getWhatCanYouAccess(){
        for(String[] j : paths){
            if (j[0].equals(currentLocation.getLocationName())){
                results.add(j[1]);
            }
        }
    }

    private void getWhatYouCanSee(){
        ArrayList<Artefacts> artObjects = currentLocation.getArtefacts();
        ArrayList<Characters> charObjects = currentLocation.getCharacters();
        ArrayList<Furniture> furnObjects = currentLocation.getFurniture();
        for(Artefacts j : artObjects){
            results.add(j.getDescription());
        }
        for(Characters j : charObjects){
            results.add(j.getDescription());
        }
        for(Furniture j : furnObjects){
            results.add(j.getDescription());
        }
    }

    public ArrayList<String> getResult(){
        return results;
    }

    private Location getStartLocation(){
        return locations.get(0);
    }

    private Player checkPlayer(String line){
        String[] split = line.split(":");
        String currentPlayerName = split[0];
        for (Player j : players) {
            if (j.getPlaterName().equals(currentPlayerName)) {
                return j;
            }
        }
        Player player = new Player(currentPlayerName);
        player.setCurrentLocation(getStartLocation());
        players.add(player);
        return player;
    }
}
