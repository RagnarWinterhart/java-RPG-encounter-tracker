import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Graphical user interface for the Encounter Builder application
 * 
 * Displays combatants, encounter state, turn order, and user interactions like 
 * damage, healing, conditions, changing initiative, adding/removing combatants, saving, 
 * and loading encounters.
 * 
 * Uses Java Swing componenets to create interface.
 * 
 * @author Rowan Lynn
 * @version 1.0
 */
public class EncounterGUI {

    private JFrame frame;
    private Encounter encounter;

    private JButton nextTurnButton;
    private JButton damage;
    private JButton heal;
    private JButton addCombatant;
    private JButton removeCombatant;
    private JButton addCondition;
    private JButton removeCondition;
    private JButton saveButton;
    private JButton loadButton;
    private JButton moveInit;
    private JButton newEnc;
    private JButton editRound;

    private JPanel combatantPanel;
    private JLabel titleLabel;
    private JLabel roundLabel;
    private JPanel headerPanel;

    /**
     * Default constructor that initializes the EncounterGUI with a new encounter named "New Encounter".
     * Sets up the frame, header, combatant panel, and buttons, and makes the frame visible.
     */
    public EncounterGUI(){
        this(createEncounter());
    }

    /**
     * Initializes the EncounterGUI with a given encounter, sets up the frame, header, combatant panel, and buttons, and makes the frame visible.
     * @param encounter the encounter to be displayed and managed in the GUI
     */
    public EncounterGUI(Encounter encounter){
        this.encounter = encounter;
        setupFrame();
        setupHeader();
        setupCombatantPanel();
        setupButtons();
 
        refreshDisplay();
        frame.setVisible(true);
    }

    /**
     * Prompts user to create encounter. If user enters a blank name default name is used. 
     * If the dialog is cancelled the application exits.
     * @return New encounter object with entered name 
     */
    private static Encounter createEncounter(){
        String name = JOptionPane.showInputDialog(null, "Enter Encounter Name: ");
        if(name == null){
            System.exit(0);
        }
        if(name.isBlank()){
            name = "New Encounter";
        }
        return new Encounter(name);
    }

    /*
        Setup methods lie below...
        Each method is responsible for setting up a specific part of the GUI, such as the header
        or the combatant panel, and configuring its layout and components accordingly.
    */

    /**
     * Refreshes the display of the encounter information and combatants.
     */
    public void refreshDisplay(){
        combatantPanel.removeAll();

        titleLabel.setText(encounter.getEncounterName());
        roundLabel.setText("Round: " + encounter.getCurrentRound());
        JPanel currentTile = null;
        for(Combatant c : encounter.getCombatants()){
            JPanel tile = new CombatantTile(c, encounter.getCurrentCombatant());
            combatantPanel.add(tile);
            combatantPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            if(c == encounter.getCurrentCombatant()){
                currentTile = tile;
            }
        }
        combatantPanel.revalidate();
        combatantPanel.repaint();
        JPanel finalCurrentTile = currentTile;

        SwingUtilities.invokeLater(() -> { //Scroll to bring current combatant into view after the layout is updated.
            if(finalCurrentTile != null){
                Rectangle bounds = finalCurrentTile.getBounds();
                bounds.y = Math.max(0, bounds.y - 10);
                combatantPanel.scrollRectToVisible(bounds);
            }
        });
    }

    /**
     * Sets up the header panel with the encounter name and current round, and adds it to the frame.
     */
    private void setupHeader(){
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel = new JLabel(encounter.getEncounterName());
        headerPanel.add(titleLabel);
        roundLabel = new JLabel("Round: " + encounter.getCurrentRound());
        headerPanel.add(roundLabel);
        frame.add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Sets up the combatant panel with a vertical box layout and adds it to the center of the frame within a scroll pane.
     */
    private void setupCombatantPanel(){
        combatantPanel = new JPanel();
        combatantPanel.setLayout(new BoxLayout(combatantPanel, BoxLayout.Y_AXIS));
        JScrollPane combatantJScrollPane = new JScrollPane(combatantPanel);
        combatantPanel.setBorder(
        BorderFactory.createEmptyBorder(10,10,10,10));
        frame.add(combatantJScrollPane, BorderLayout.CENTER);
    }


    /**
     *  Initializes the main frame of the GUI with a title, size, default close operation, and layout.
     */
    private void setupFrame(){
        frame = new JFrame("Encounter Manager");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    /**
     *  Sets up the buttons for managing the encounter (Next Turn, Deal Damage, Heal, 
     *  Add Combatant, Remove Combatant, Add condition, Remove condition, Save file, Load from file, Move initiative, and Edit round) and adds them to a panel on the right side of the frame.
     *  Each button is associated with an action listener that triggers the corresponding method to perform its function when clicked.
     */
    private void setupButtons(){
        nextTurnButton = new JButton("Next Turn");
        nextTurnButton.addActionListener(e -> {advanceTurn();});

        damage = new JButton("Deal Damage");
        damage.addActionListener(e ->{dealDamage();}); 

        heal = new JButton("Heal");
        heal.addActionListener(e -> {healDamage();});

        addCombatant = new JButton("Add Combatant");
        addCombatant.addActionListener(e -> {newCombatant();});

        removeCombatant = new JButton("Remove Combatant");
        removeCombatant.addActionListener(e ->{remCombatant();});

        addCondition = new JButton("Add Condition");
        addCondition.addActionListener(e ->{newcondition();});

        removeCondition = new JButton("Remove Condition");
        removeCondition.addActionListener(e ->{remCondition();});

        moveInit = new JButton("Move Initiative");
        moveInit.addActionListener(e ->{moveInit();});

        newEnc = new JButton("New Encounter");
        newEnc.addActionListener(e ->{newEnc();});

        editRound = new JButton("Edit Round");
        editRound.addActionListener(e ->{editRound();});

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                encounter.saveEncounter(chooser.getSelectedFile().getAbsolutePath());
            }

        });

        loadButton = new JButton("Load From File");
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                Encounter loaded = Encounter.loadEncounter(chooser.getSelectedFile().getAbsolutePath());
                if(loaded != null){
                    encounter = loaded;
                    refreshDisplay();
                }
            }

        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton[] buttons = { 
            nextTurnButton, damage, heal, addCombatant, removeCombatant, addCondition, 
            removeCondition, moveInit, editRound, saveButton, loadButton, newEnc
        };

        for (JButton button : buttons){
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(button);
            panel.add(Box.createVerticalStrut(8));
        }
        frame.add(panel, BorderLayout.EAST);
    }

    /*
    Button Methods lie below... 
    Each method corresponds to a button and performs the necessary actions to update the encounter state and refresh the display accordingly.
    */

    /** 
     * Allows for round number to be edited without disturbing other elements. Returns, cancelling the operation if null is inputed for any field.
    */
    private void editRound(){
        Integer round = DialogUtils.promptForInt(frame, "Enter new round number:");
        if(round == null){return;}
        encounter.setCurrentRound(round);
        refreshDisplay();
    }

    /**
     * Allows for new encounter to be created while application is running
     */
    private void newEnc(){
        int result = JOptionPane.showConfirmDialog(frame, "Create a new encounter? Unsaved changes will be lost.", "New Encounter", JOptionPane.YES_NO_CANCEL_OPTION);
        if(result == JOptionPane.YES_OPTION){
            encounter = createEncounter();
            refreshDisplay();
        }else{
            return;
        }
    }

    /**
     * Changes selected target's initiative, moving them accordingly. Returns, cancelling the operation if null is inputed for any field.
     */
    private void moveInit(){
        String selected = selectTarget();
        Combatant target = encounter.findCombatant(selected);
        if(target == null){return;}
        Integer init = DialogUtils.promptForInt(frame, "Enter New Initiative:");
        if(init == null){return;}
        target.setInit(init);
        encounter.sortInitiative(); //Actually sort and move the combatant to new initiative, can be weird if moving current combatant
        refreshDisplay();
    }

    /**
     *  Adds a new condition to the selected combatant. Returns, cancelling the operation if null is inputed for any field.
     */
    private void newcondition(){ 
        String selected = selectTarget();
        Combatant target = encounter.findCombatant(selected);
        if(target == null){
            return;
        }
        String name = JOptionPane.showInputDialog(frame, "Enter Condition:");
        if(name == null || name.isBlank()){
                return;
            }
        Integer duration = DialogUtils.promptForInt(frame, "Enter Condition Duration:");
        if(duration == null){
            return;
        }
        Condition condition = new Condition(name, duration);
        target.addConditions(condition);
        refreshDisplay(); 
    }

    /**
     *  Removes a condition from the selected combatant. Prompts the user to select a combatant, 
     * then displays a list of that combatant's current conditions for the user to choose from. If the user selects an condition, 
     * it is removed from the combatant and the display is refreshed.
     */
    private void remCondition(){
        String selected = selectTarget();
        if(selected == null){
            return;
        }
        Combatant target = encounter.findCombatant(selected);
        if(target == null){
            //JOptionPane.showMessageDialog(frame,"Combatant not found.");
            return;
        }
        ArrayList<Condition> conditions = target.getConditions();
        if(conditions.isEmpty()){
            JOptionPane.showMessageDialog(frame, "This Combatant Has No conditions");
            return;
        }
        String[] choices = new String[conditions.size()];
        for(int i = 0; i < conditions.size(); i++){
            choices[i] = conditions.get(i).getconditionName();
        }
        JComboBox<String> comboBox = new JComboBox<>(choices);
        int result = JOptionPane.showConfirmDialog(frame, comboBox, "Select condition", JOptionPane.OK_CANCEL_OPTION);
        if(result != JOptionPane.OK_OPTION){
            return;
        }
        String chosen = (String) comboBox.getSelectedItem();
        target.removeConditions(chosen);
        refreshDisplay(); 
    }
    
    /**
     * Prompts the user to enter details for a new combatant (name, initiative, current HP, max HP, and type), 
     * creates a new Combatant object with those details, adds it to the encounter, sorts the combatants by initiative, 
     * and refreshes the display. If the user enters invalid input for initiative or HP values, an error message is shown.
     */
    private void newCombatant(){
        try {
            String name = DialogUtils.promptForString(frame, "Enter Combatant Name:");
            if(name == null || name.isBlank()){
                return;
            }

            Integer init = DialogUtils.promptForInt(frame, "Enter Initiative:");
            if(init == null){return;}

            Integer armorClass = DialogUtils.promptForInt(frame, "Enter Armor Class:");
            if(armorClass == null){return;}

            Integer curHP = DialogUtils.promptForInt(frame, "Enter Current HP:");
            if(curHP == null){return;}

            Integer maxHP = DialogUtils.promptForInt(frame, "Enter Max HP:");
            if(maxHP == null){return;}

            String[] choices = {"PC", "NPC", "Monster"};
            JComboBox<String> comboBox = new JComboBox<>(choices);
            JOptionPane.showMessageDialog(frame, comboBox, "Select Combatant Type", JOptionPane.QUESTION_MESSAGE);
            String type = (String) comboBox.getSelectedItem();


            Combatant newCombatant = new Combatant(name, init, curHP, maxHP, armorClass, type);
            encounter.addCombatant(newCombatant);
            encounter.sortInitiative();
            refreshDisplay();
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
        }
    }

    /**
     * Removes a combatant from the encounter based on user selection. Prompts the user to select a combatant from a list of current combatants.
     * If combatant is removed display is refreshed. If action is cancelled or not combatant selected, method returns without any chnages.
     */
    private void remCombatant(){
        String selected = selectTarget();    
        if(selected != null){
            encounter.removeCombatant(selected);      
        }
        refreshDisplay();
    }
    
    /**
     * Prompt user for a target combatant selected from a dropdown, then prompts for damage amount.
     * If input is valid, damage is applied to the target and display is refreshed. If user cancels or inputs invalid damage,
     * method returns without any changes.
     */
    private void dealDamage(){
        String selected = selectTarget();
        if(selected == null){return;}//Safety check
        Combatant target = encounter.findCombatant(selected);
        Integer damage = DialogUtils.promptForInt(frame, "Enter damage amount:");
        if(damage == null){return;}
        target.takeDamage(damage);
        refreshDisplay(); 
    }

    /**
     * Prompt user for a target combatant selected from a dropdown, then prompts for heal amount.
     * If input is valid, healing is applied to the target and display is refreshed. If user cancels or inputs invalid healing amount,
     * method returns without any changes.
     */
    private void healDamage(){
        String selected = selectTarget();   
        if(selected == null){return;} //Safety check
        Combatant target = encounter.findCombatant(selected);
        Integer healing =  DialogUtils.promptForInt(frame, "Enter heal amount:");
        if(healing == null){return;}
        target.heal(healing);
        refreshDisplay();
    }

    /**
     * Advances the encounter to the next turn by calling the nextTurn method 
     * on the encounter object and refreshing the display to reflect any changes in combatant or turn order.
     */
    private void advanceTurn(){
        encounter.nextTurn();
        refreshDisplay();
    }

    /**
     * Prompts the user to select a combatant from a dropdown list of current combatants in the encounter.
     * If the user selects a combatant and confirms, the name of the selected combatant is returned. 
     * If the user cancels the selection or if there are no combatants, the method returns null.
     * @return the name of the selected combatant, or null if no selection is made or if there are no combatants
     */
    private String selectTarget(){
        ArrayList<Combatant> combatants = encounter.getCombatants();
        if(combatants.isEmpty()){
            JOptionPane.showMessageDialog(frame, "No combatants available.");
            return null;
        }
        String[] choices = new String[combatants.size()];
        for(int i = 0; i < combatants.size(); i++){
            choices[i] = combatants.get(i).getName();
        }
        return DialogUtils.dropdown(frame, "Select Target", choices);
    }
}
