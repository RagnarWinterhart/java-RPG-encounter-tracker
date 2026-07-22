import java.io.Serializable;

/**
 * Represents a temporary condition applied to a combatant for a number of rounds.
 */
public class Condition implements Serializable{
    private static final long serialVersionUID = 1L; // Explicit serial version ID so small changes don't mess with saves
    private String condition;
    private int duration;

    /**
     * Default constructor.
     * Creates an empty condition.
     */
    public Condition() {
        condition = "";
        duration = 0;
    }
    /**
     * Creates a Condition with specified values
     * @param condition condition's name
     * @param duration Duration in rounds, that decrement at the end of combatant turn
     */
    public Condition(String condition, int duration){
        this.condition = condition;
        this.duration = duration;
    }

    /**
     * Returns condition's name
     * @return condition's name
     */
    public String getconditionName(){return condition;}

    /**
     * Returns condition's duration
     * @return duration
     */
    public int getDuration(){return duration;}

    /**
     * Decreases condition duration by one round
     */
    public void decDuration(){
        duration--;
    }

    /**
     * Checks if an condition's duration is expired i.e. {@literal<=} 0
     * @return True if duration is expired false if not.
     */
    public boolean isExpired(){
        return duration < 0;
    }

    /**
     * Returns a formatted string representation of the condition.
     * @return formatted condition info
     */
    @Override
    public String toString(){
        String e = getconditionName() + "[" + getDuration() + "]";
        return e;
    }
}
