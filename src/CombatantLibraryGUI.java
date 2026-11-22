import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CombatantLibraryGUI {
    private JTextField searchField;
    private JComboBox<String> searchByBox;
    private final DefaultTableModel model;
    private final JFrame frame;
    private List<Combatant> currentResults;
    private final JTable combatantTable;

    //JTable
    //Search Controls --> search bar and drop down for criteria (name/category/creature_type)
    //New/Edit/Delete buttons
    //Add to Encounter button
    public CombatantLibraryGUI(){
        frame = new JFrame("Combatant Library");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String[] columns = {"Name", "Category", "Hit Points", "AC"};
        currentResults = DatabaseManager.getAllCombatants();
        model = new DefaultTableModel(columns, 0){ //prevents cells in the table from being directly edited
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        combatantTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(combatantTable);
        for(Combatant c : currentResults){
            Object[] row = {
                    c.getName(),
                    c.getCategory(),
                    c.getMaxHP(),
                    c.getArmorClass()
            };
            model.addRow(row);
        }
        frame.add(createSearchPanel(), BorderLayout.NORTH);
        frame.add(createActionPanel(), BorderLayout.SOUTH);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public JPanel createSearchPanel(){
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(30);
        String[] choices = {"Name", "Category", "Creature Type"};
        searchByBox = new JComboBox<>(choices);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String searchParam = searchField.getText();
            String searchBy = (String) searchByBox.getSelectedItem();
            List<Combatant> results = DatabaseManager.searchCombatant(searchParam, searchBy);
            updateTable(results);
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchByBox);
        return searchPanel;

    }

    //Creates pane at bottom of library for action buttons
    public JPanel createActionPanel(){
        JPanel actionPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");

        actionPanel.add(deleteButton);
        deleteButton.addActionListener(e -> showDeleteCombatantDialog());
        actionPanel.add(addButton);
        addButton.addActionListener(e -> showAddCombatantDialog());
        actionPanel.add(editButton);
        editButton.addActionListener(e -> showEditCombatantDialog());
        return actionPanel;
    }

    //The functionality of the delete button in the action pane
    public void showDeleteCombatantDialog(){
        int selectedRow = combatantTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(frame, "Please select a combatant to delete.");
            return;
        }
        int modelRow = combatantTable.convertRowIndexToModel(selectedRow);
        Combatant selectedCombatant = currentResults.get(modelRow);
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        int result = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + selectedCombatant.getName() + "?",
                "Delete Combatant",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        boolean success = DatabaseManager.deleteCombatant(selectedCombatant);

        if (success) {
            updateTable(DatabaseManager.getAllCombatants());
            JOptionPane.showMessageDialog(frame, "Combatant deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to delete combatant.");
        }
    }


    //The functionality of the edit button in the action pane
    public void showEditCombatantDialog(){
        int selectedRow = combatantTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(frame, "Please select a combatant to edit.");
            return;
        }
        int modelRow = combatantTable.convertRowIndexToModel(selectedRow);
        Combatant selectedCombatant = currentResults.get(modelRow);
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(selectedCombatant.getName(), 20);

        JLabel maxHPLabel = new JLabel("Max HP:");
        JTextField maxHPField = new JTextField(String.valueOf(selectedCombatant.getMaxHP()), 10);

        JLabel acLabel = new JLabel("Armor Class:");
        JTextField acField = new JTextField(String.valueOf(selectedCombatant.getArmorClass()), 10);

        JLabel categoryLabel = new JLabel("Category:");
        String[] choices = {"Player", "NPC", "Monster"};
        JComboBox<String> categoryBox = new JComboBox<>(choices);
        categoryBox.setSelectedItem(selectedCombatant.getCategory());
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(maxHPLabel);
        inputPanel.add(maxHPField);
        inputPanel.add(acLabel);
        inputPanel.add(acField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryBox);
        int result = JOptionPane.showConfirmDialog(
                frame,
                inputPanel,
                "Edit Combatant",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            String name = nameField.getText().trim();
            if(name.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name.");
                return;
            }
            int maxHP = Integer.parseInt(maxHPField.getText().trim());
            int armorClass = Integer.parseInt(acField.getText().trim());
            String category = (String) categoryBox.getSelectedItem();
            if(maxHP < 1 || armorClass < 1){
                JOptionPane.showMessageDialog(frame, "Please enter Max HP of at least 1, and Armor Class of at least 1.");
                return;
            }
            selectedCombatant.setMaxHP(maxHP);
            selectedCombatant.setArmorClass(armorClass);
            selectedCombatant.setCategory(category);
            selectedCombatant.setName(name);

            boolean success = DatabaseManager.updateCombatant(selectedCombatant);//NEED SOME TRY/CATCH FOR THIS AS WELL CURRENTLY NO CHECKING FOR IF THIS FAILS

            if (success) {
                updateTable(DatabaseManager.getAllCombatants());
                JOptionPane.showMessageDialog(frame, "Combatant updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update combatant.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter valid numbers for Max HP and Armor Class."
            );
        }
    }

    //The functionality of the add button in the action pane
    public void showAddCombatantDialog(){
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel statsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JPanel basicPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.add(basicPanel);
        inputPanel.add(statsPanel);
        JTextField[] statFields = addStats(statsPanel);
        statsPanel.setVisible(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel maxHPLabel = new JLabel("Max HP:");
        JTextField maxHPField = new JTextField();

        JLabel acLabel = new JLabel("Armor Class:");
        JTextField acField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        String[] choices = {"Player", "NPC", "Monster"};
        JComboBox<String> categoryBox = new JComboBox<>(choices);
        JCheckBox checkBox = new JCheckBox("Add Stats?");
        checkBox.setSelected(false);

        basicPanel.add(nameLabel);
        basicPanel.add(nameField);
        basicPanel.add(maxHPLabel);
        basicPanel.add(maxHPField);
        basicPanel.add(acLabel);
        basicPanel.add(acField);
        basicPanel.add(categoryLabel);
        basicPanel.add(categoryBox);
        basicPanel.add(checkBox);
        checkBox.addActionListener(e -> {
            statsPanel.setVisible(checkBox.isSelected());
            inputPanel.revalidate();
            inputPanel.repaint();
        });
        int result = JOptionPane.showConfirmDialog(
                frame,
                inputPanel,
                "New Combatant",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            if(name.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name.");
                return;
            }

            int maxHP = Integer.parseInt(maxHPField.getText().trim());
            int armorClass = Integer.parseInt(acField.getText().trim());
            String category = (String) categoryBox.getSelectedItem();

            if(maxHP < 1 || armorClass < 1){
                JOptionPane.showMessageDialog(frame, "Please enter Max HP of at least 1, and Armor Class of at least 1.");
                return;
            }

            Combatant newCombatant = new Combatant(name, 0, maxHP, maxHP, armorClass, category);

            // Parse stats before inserting the combatant
            PF2eStatBlock newStats = getNewStats(checkBox, statFields);

            boolean inserted = DatabaseManager.insertCombatant(newCombatant);

            if (!inserted) {
                JOptionPane.showMessageDialog(frame, "Could not insert combatant.");
                return;
            }

            if(newStats != null) {
                DatabaseManager.insertStats(newCombatant, newStats);
            }

            updateTable(DatabaseManager.getAllCombatants());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter valid numbers for all stat fields."
            );
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "A database error occurred: " + ex.getMessage()
            );
        }

    }

    private static PF2eStatBlock getNewStats(JCheckBox checkBox, JTextField[] statFields) {
        if(!checkBox.isSelected()) {
            return null;
        }

        int level = Integer.parseInt(statFields[0].getText().trim());
        int perception = Integer.parseInt(statFields[1].getText().trim());
        int fortitude = Integer.parseInt(statFields[2].getText().trim());
        int reflex = Integer.parseInt(statFields[3].getText().trim());
        int will = Integer.parseInt(statFields[4].getText().trim());
        int speed = Integer.parseInt(statFields[5].getText().trim());

        return new PF2eStatBlock(level, perception, fortitude, reflex, will, speed);
    }


    public JTextField[] addStats(JPanel statsPanel){
        JLabel levelLabel = new JLabel("Level:");
        JTextField levelField = new JTextField();

        JLabel perceptionLabel = new JLabel("Perception:");
        JTextField perceptionField = new JTextField();

        JLabel fortLabel = new JLabel("Fortitude:");
        JTextField fortField = new JTextField();

        JLabel refLabel = new JLabel("Reflex:");
        JTextField refField = new JTextField();

        JLabel willLabel = new JLabel("Will:");
        JTextField willField = new JTextField();

        JLabel speedLabel = new JLabel("Speed:");
        JTextField speedField = new JTextField();

        statsPanel.add(levelLabel);
        statsPanel.add(levelField);
        statsPanel.add(perceptionLabel);
        statsPanel.add(perceptionField);
        statsPanel.add(fortLabel);
        statsPanel.add(fortField);
        statsPanel.add(refLabel);
        statsPanel.add(refField);
        statsPanel.add(willLabel);
        statsPanel.add(willField);
        statsPanel.add(speedLabel);
        statsPanel.add(speedField);

        return new JTextField[] {
                levelField,
                perceptionField,
                fortField,
                refField,
                willField,
                speedField
        };
    }

    //Helper update method to refresh library table
    private void updateTable(List<Combatant> combatants) {
        currentResults = combatants;
        model.setRowCount(0);
        for (Combatant c : combatants) {
            Object[] row = {
                    c.getName(),
                    c.getCategory(),
                    c.getMaxHP(),
                    c.getArmorClass()
            };

            model.addRow(row);
        }
    }

}
