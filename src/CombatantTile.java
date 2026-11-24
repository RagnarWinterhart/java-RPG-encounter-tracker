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
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        detailsPanel.setOpaque(false);

        JPanel basicPanel = new JPanel();
        basicPanel.setOpaque(false);
        basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.Y_AXIS));

        basicPanel.add(new JLabel("Name: " + combatant.getName()));
        basicPanel.add(new JLabel("HP: " + combatant.getCurHP() + "/" + combatant.getMaxHP()));
        basicPanel.add(new JLabel("AC: " + combatant.getArmorClass()));
        basicPanel.add(new JLabel("Initiative: " + combatant.getInit()));
        basicPanel.add(new JLabel("Category: " + combatant.getCategory()));

        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        PF2eStatBlock stats =
                DatabaseManager.getPF2eStats(combatant.getDatabaseId());

        if (stats != null) {
            statsPanel.add(new JLabel("Level: " + stats.getLevel()));
            statsPanel.add(new JLabel("Perception: +" + stats.getPerception()));
            statsPanel.add(new JLabel("Fortitude: +" + stats.getFortitude()));
            statsPanel.add(new JLabel("Reflex: +" + stats.getReflex()));
            statsPanel.add(new JLabel("Will: +" + stats.getWill()));
            statsPanel.add(new JLabel("Speed: " + stats.getSpeed()));
        }

        detailsPanel.add(basicPanel);
        detailsPanel.add(statsPanel);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, detailsPanel.getPreferredSize().height)
        );

        JPanel conditionPanel = new JPanel();
        conditionPanel.setOpaque(false);
        conditionPanel.setLayout(new BoxLayout(conditionPanel, BoxLayout.Y_AXIS));
        conditionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        conditionPanel.add(new JLabel("Conditions:"));

        for(Condition e : combatant.getConditions()){
            JLabel conditionLabel = new JLabel("- " + e);
            conditionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            conditionPanel.add(conditionLabel);
        }
        add(detailsPanel);
        add(conditionPanel);
    }
}
