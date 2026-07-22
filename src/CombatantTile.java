import javax.swing.*;
import java.awt.*;

/**
 * GUI class for creating combatant tiles, used in EncounterGUI. 
 */
public class CombatantTile extends JPanel {
        /**
        * Creates a visual tile representing a combatant in the encounter. Displays the combatant's name, current and maximum HP, initiative, and any active conditions.
        * If the combatant is the current combatant taking their turn, the tile is highlighted with a different background color.
        *
        * @param combatant the Combatant object whose information will be displayed on the tile
        * @param currentCombatant the Combatant object representing the current combatant taking their turn in the encounter
        */
    public CombatantTile(Combatant combatant, Combatant currentCombatant){
        setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    Color.BLACK, //Black box border
                    2
                ),
                BorderFactory.createEmptyBorder(
                    10,10,10,10 //padding border
                )
            )
        );
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setOpaque(true);

        if (combatant == currentCombatant) {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(212, 175, 55), // gold
                    3
                ),
                BorderFactory.createEmptyBorder(10,10,10,10)
            ));

            setBackground(new Color(55, 55, 70)); // very subtle tint
        }
        add(new JLabel("Name: " + combatant.getName()));
        add(new JLabel("HP: " + combatant.getCurHP() + "/" + combatant.getMaxHP()));
        add(new JLabel("AC: " + combatant.getArmorClass()));
        add(new JLabel("Initiative: " + combatant.getInit()));
        add(new JLabel("Type: " + combatant.getType()));
        JPanel conditionPanel = new JPanel();
        conditionPanel.setOpaque(false);
        conditionPanel.setLayout(new BoxLayout(conditionPanel, BoxLayout.Y_AXIS));
        conditionPanel.add(new JLabel("Conditions:"));

        for(Condition e : combatant.getConditions()){
            conditionPanel.add(new JLabel("- " + e));
        }
        add(conditionPanel);
    }
}
