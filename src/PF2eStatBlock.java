public class PF2eStatBlock {
    private int level;
    private int perception;
    private int fortitude;
    private int reflex;
    private int will;
    private int speed;

    public PF2eStatBlock(int level, int perception, int fortitude,
                         int reflex, int will, int speed) {
        this.level = level;
        this.perception = perception;
        this.fortitude = fortitude;
        this.reflex = reflex;
        this.will = will;
        this.speed = speed;
    }

    // Getters
    public int getLevel() {
        return level;
    }

    public int getPerception() {
        return perception;
    }

    public int getFortitude() {
        return fortitude;
    }

    public int getReflex() {
        return reflex;
    }

    public int getWill() {
        return will;
    }

    public int getSpeed() {
        return speed;
    }

    // Setters
    public void setLevel(int level) {
        this.level = level;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public void setFortitude(int fortitude) {
        this.fortitude = fortitude;
    }

    public void setReflex(int reflex) {
        this.reflex = reflex;
    }

    public void setWill(int will) {
        this.will = will;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
