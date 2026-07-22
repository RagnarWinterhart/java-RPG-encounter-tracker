import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a combatant with name, hitpoints (current/max), 
 * type, and active conditions. 
 */
public class Combatant implements Serializable{
    private static final long serialVersionUID = 1L; //Explicit serial version ID so small changes don't mess wit saves
    private String name;
    private int init; //Initiative score
    private int curHP;
    private int maxHP;
    private String type; //PC, NPC, Monster
    private ArrayList<Condition> conditions;

    /** 
     * Default constructor. 
     * Creates an empty combatant with default values. 
     */
    public Combatant() {
		name = "";
		init = 0;
		curHP = 0;
		maxHP = 0;
		type = "";
		conditions = new ArrayList<>();
    }
    
    /**
     * Creates a combatant with specified values.
     * @param name the combatant's name
     * @param init combatant's initiative score
     * @param curHP combatant's current hit points
     * @param maxHP combatant's max hit points
     * @param type combatant's type i.e. Player Character (PC), Non-Player Character (NPC), or Monster
     */
    public Combatant(String name, int init, int curHP, int maxHP, String type){
        this.name = name;
        this.init = init;
        this.curHP = Math.max(0, Math.max(curHP, this.maxHP));
        this.maxHP = Math.max(1, maxHP);
        this.type = type;
        this.conditions = new ArrayList<>();;
    }

    /**
     * Retruns combatant's name
     * @return the name
     */
    public String getName(){return name;}

    /**
     * Returns combatant's intitiative score
     * @return initiative score
     */
    public int getInit(){return init;}

    /**
     * Sets combatant's initiative score
     * @param init initiative score to set
     */
    public void setInit(int init){
        this.init = init;
    }

    /**
     * returns current HP
     * @return current HP
     */
    public int getCurHP(){return curHP;}

    /**
     * Returns combatant's max HP
     * @return max HP
     */
    public int getMaxHP(){return maxHP;}

    /**
     * Returns combatant's type
     * @return type
     */
    public String getType(){return type;}

    /**
     * Returns combatant's conditions
     * @return conditions
     */
    public ArrayList<Condition> getConditions(){
        return conditions;
    }

    /**
     * Prints combatant details to console 
     * Used in development
     */
    public void printDetails(){
        IO.println("Name: " + this.name);
        IO.println("Initiative: " + this.init);
        IO.println("HP: " + this.curHP);
        IO.println("Type: " + this.type);
    }

    /**
     * Applies damage to combatant 
     * HP cannot go below 0
     * @param damage amount to apply
     */
    public void takeDamage(int damage){
        if(damage < 0){return;}
        curHP -= damage;
        if(curHP <= 0) {
            curHP = 0;
        }
        
    }

    /**
     * Applies healing to combatant 
     * HP cannot go above combatant's max
     * @param heal amount to apply
     */
    public void heal(int heal){
        if(heal < 0){return;}
        curHP += heal;
        if(curHP >= maxHP) curHP = maxHP;
    }

    /**
     * Adds a condition to combatant
     * @param condition condition to add
     */
    public void addConditions(Condition condition){
        if(condition == null) return;
        conditions.add(condition);
    }

    /**
     * Removes an condition by name from a combatant
     * @param condition condition name to be removed
     */
    public void removeConditions(String condition){
        for(int i = 0; i < conditions.size(); i++){
            if(condition == null) return;
            if(conditions.get(i).getconditionName().equalsIgnoreCase(condition)){
                conditions.remove(i);
                return;
            }
        }
    }

    /**
     * Decrements duration of all conditions on combatant
     * Removes expired conditions automatically 
     */
    public void tickconditions(){
        for(int i = conditions.size() - 1; i >= 0; i--){
            Condition condition = conditions.get(i);
            condition.decDuration();
            if(condition.isExpired()){
                conditions.remove(i);
            }
            
        }
    }

    /**
     * Returns a formatted string represenation of combatant 
     * @return formatted combatant info 
     */
    @Override
    public String toString(){
        String result = name +
                " | HP: " +
                curHP + "/" +
                maxHP + " | Initiative: " +
                init + " | Type: " +
                type;
        if(!conditions.isEmpty()){
            result += " | conditions: ";
            for(Condition e : conditions){
                result += e + " ";
            }
        }
        return result;
    }
    /**
     * Test main for debugging combatants
     */
    void main() {
        Combatant helena = new Combatant("Helena", 15, 40, 50, "PC");
        IO.println(helena);
    }
}
