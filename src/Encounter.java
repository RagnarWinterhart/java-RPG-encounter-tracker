import java.util.ArrayList;
import java.io.*;

/**
 * Represents combat encounter, containing combatants, initiative order, 
 * rounds, and current turn tracking.
 */
public class Encounter implements Serializable{
    private static final long serialVersionUID = 1L; //Explicit serial version ID so small changes don't mess wit saves
    private String encounterName;
    private ArrayList<Combatant> combatants;
    private int currentRound;
    private Combatant currentCombatant; //Track the combatant object, we assign current combatant later

    /**
     * Default constructor.
     * Creates an empty encounter with default values;
     */
    public Encounter(){
        encounterName = "";
        combatants = new ArrayList<>();
        currentRound = 1;
        this.currentCombatant = null;
    }

    /**
     * Creates a new empty Encounter with the given name.
     * Initializes the combatant list and sets the starting round to 1.
     * The current combatant is initially null until combatants are added.
     *
     * @param encounterName the name of the encounter
     */
    public Encounter(String encounterName){
        this.encounterName = encounterName;
        this.combatants = new ArrayList<>();
        this.currentRound = 1;
        this.currentCombatant = null;
        
    }

    /**
     * Getter for encounter name.
     * @return String name of the encounter
     */
    public String getEncounterName(){return encounterName;}

    /**
     * Retrieves list of combatants.
     * @return combatant objects
     */
    public ArrayList<Combatant> getCombatants(){
        return combatants;
    }

    /**
     * Returns specific combatant at current turn
     * @return the combatant who is currently taking their turn
     */
    public Combatant getCurrentCombatant(){
        return currentCombatant;
    }

    /**
     * Retrieves the current round
     * @return the current round.
     */
    public int getCurrentRound(){
        return currentRound;
    }

    public void setCurrentRound(int currentRound){
        this.currentRound = currentRound;
    }

    /**
     * Returns the index of the current combatant in the initiative order.
     * If the current combatant is not found, returns 0 as a fallback.
     *
     * @return the zero-based index of the current turn, or 0 if not found
     */
    public int getCurrentTurn(){ //For redundancy 
        int index = combatants.indexOf(currentCombatant);
        if(index < 0){
            return 0;
        }
        return index;
    }

    /**
     * Add a combatant to the current list of combatants in encounter
     * @param c the combatant to be added
     */
    public void addCombatant(Combatant c){
        combatants.add(c);
        if(currentCombatant == null && !combatants.isEmpty()){
            currentCombatant = combatants.get(0);
        }
       // IO.println("added: " + c.getName()); //for testing
    }

    /**
     * Removes a combatant from the encounter by name (case-insensitive).
     *
     * If the removed combatant is the current turn, the next valid combatant
     * in initiative order becomes the new current combatant. If the removed
     * combatant was the last in the list, the turn wraps to the start.
     *
     * If removal results in an empty encounter, the current combatant is set to null.
     *
     * @param name the name of the combatant to remove
     */
    public void removeCombatant(String name){ //Replaced old remove with this, since we swapped from index based tracking to object based.
        for(int i = 0; i < combatants.size(); i++){
            Combatant c = combatants.get(i);

            if(c.getName().equalsIgnoreCase(name)){
                boolean removingCurrent = (c == currentCombatant);
                combatants.remove(i);
                // CASE 1: encounter becomes empty
                if(combatants.isEmpty()){
                    currentCombatant = null;
                    return;
                }

                // CASE 2: removed the current turn combatant
                if(removingCurrent){
                    // safe fallback: stay at same index if possible
                    int newIndex = i;

                    if(newIndex >= combatants.size()){
                        newIndex = 0;
                    }
                    currentCombatant = combatants.get(newIndex);
                    return;
                }
                return;
            }
        }
    }

    /**
     * Iterate to find combatant by name, ignoring case 
     * @param name of combatant to be found
     * @return combatant or null if not found
     */
    public Combatant findCombatant(String name){
        for(Combatant c : combatants){
            if(c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }

    /**
     * Sorts initiative order, by highest to lowest score.
     */
    public void sortInitiative(){
       combatants.sort((c1, c2) -> Integer.compare(c2.getInit(), c1.getInit())); //Sorts in descending order
    }

    /**
     * Retrieves current combatant, ticks their conditions (end of turn), then 
     * advances the turn to next combatant. 
     * Wraps to new round if we advance past last combatant.
     */
    public void nextTurn(){
        if(combatants.isEmpty()){
            currentCombatant = null;
            return;
        }
        currentCombatant.tickconditions();
        int index = combatants.indexOf(currentCombatant);

        if(index == -1){
            currentCombatant = combatants.get(0);
            return;
        }
        index++;

        if(index >= combatants.size()){
            index = 0;
            currentRound++;
        }
        currentCombatant = combatants.get(index); //Update actual current combatant tracker
    }

    /**
     * Returns a formatted string representation of the whole encounter.
     * @return formatted encounter info
     */
    public String toDisplay(){
        String result = "";
        result += "=== " + encounterName + " ===\n";
        result += "Round -> " + currentRound + "\n";
        for(Combatant c : combatants){
            if(c == getCurrentCombatant()){
                result += c + " " + "<-(Current Turn)" + "\n";
            }else{
                result += c + "\n";
            }
        }
        return result;
    }

    /**
     * Saves the current encounter state to a file with the given filename using serialization.
     * The encounter object is written to the file, allowing it to be loaded later with the loadEncounter method.
     * Citation: <a href="https://www.geeksforgeeks.org/java/serialization-and-deserialization-in-java/">GeeksForGeeks Java Serialization</a>
     * Citation: <a href="https://www.geeksforgeeks.org/java/throwable-printstacktrace-method-in-java-with-examples/">GeeksForGeeks printStackTrace</a>
     * @param filename the name of the file to save the encounter to
     */
    public void saveEncounter(String filename){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)); // Create an ObjectOutputStream to write the encounter object to the specified file
            out.writeObject(this); // Write the current encounter object to the file using serialization
            out.close();
        }catch(IOException e){
            e.printStackTrace(); // Print the stack trace for debugging if saving fails

        }
    }

    /**
     * Loads an encounter from a file with the given filename using deserialization.
     * Citation: <a href="https://www.geeksforgeeks.org/java/serialization-and-deserialization-in-java/">GeeksForGeeks Java Serialization</a>
     * Citation: <a href="https://www.geeksforgeeks.org/java/throwable-printstacktrace-method-in-java-with-examples/">GeeksForGeeks printStackTrace</a>
     * @param filename the name of the file to load the encounter from
     * @return the loaded Encounter object, or null if loading fails due to an IOException or ClassNotFoundException
     */
    public static Encounter loadEncounter(String filename){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename)); // Load the encounter object from the file 
            Encounter loaded = (Encounter) in.readObject(); // Read the encounter object from the file and cast it to an Encounter type
            in.close();
            return loaded;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace(); // Print the stack trace for debugging if loading fails
            return null;
        }
    }
}