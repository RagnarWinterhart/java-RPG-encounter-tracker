import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CombatantLibraryGUI {
    private EncounterGUI encounterGUI;
    private JTextField searchField;
    private JComboBox<String> searchByBox;
    private final DefaultTableModel model;
    private final JFrame frame;
    private List<Combatant> currentResults;
    private final JTable combatantTable;
    private JLabel levelLabel;
    private JLabel perceptionLabel;
    private JLabel fortitudeLabel;
    private JLabel reflexLabel;
    private JLabel willLabel;
    private JLabel speedLabel;
    private JPanel statsPanel;

    //JTable
    //Search Controls --> search bar and drop down for criteria (name/category/creature_type)
    //New/Edit/Delete buttons
    //Add to Encounter button
    public CombatantLibraryGUI(EncounterGUI encounterGUI){
        this.encounterGUI = encounterGUI;
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
        combatantTable.getSelectionModel().addListSelectionListener(event -> {
            if(!event.getValueIsAdjusting()){
                boolean selected = combatantTable.getSelectedRow() != -1;
                statsPanel.setVisible(selected);
                if (selected){
                    displaySelectedStats();
                }
                statsPanel.getParent().revalidate();
                statsPanel.getParent().repaint();
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(createStatsPanel(), BorderLayout.EAST);
        statsPanel.setVisible(false);
        frame.add(createSearchPanel(), BorderLayout.NORTH);
        frame.add(createActionPanel(), BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void displaySelectedStats() {
        int selectedRow = combatantTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }
        int modelRow = combatantTable.convertRowIndexToModel(selectedRow);
        Combatant selectedCombatant = currentResults.get(modelRow);
        PF2eStatBlock stats = DatabaseManager.getPF2eStats(selectedCombatant.getDatabaseId());
        if(stats == null){
            levelLabel.setText("Level: N/A");
            perceptionLabel.setText("Perception: N/A");
            fortitudeLabel.setText("Fortitude: N/A");
            reflexLabel.setText("Reflex: N/A");
            willLabel.setText("Will: N/A");
            speedLabel.setText("Speed: N/A");
        }else{
            levelLabel.setText("Level: " + stats.getLevel());
            perceptionLabel.setText("Perception: " + stats.getPerception());
            fortitudeLabel.setText("Fortitude: +" + stats.getFortitude());
            reflexLabel.setText("Reflex: +" + stats.getReflex());
            willLabel.setText("Will: +" + stats.getWill());
            speedLabel.setText("Speed: " + stats.getSpeed());
        }
    }

    public JPanel createStatsPanel(){
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setPreferredSize(new Dimension(190, 300));
        statsPanel.setBorder(BorderFactory.createTitledBorder("PF2e Statistics"));

        levelLabel = new JLabel("Level: ");
        perceptionLabel = new JLabel("Perception: ");
        fortitudeLabel = new JLabel("Fortitude: ");
        reflexLabel = new JLabel("Reflex: ");
        willLabel = new JLabel("Will: ");
        speedLabel = new JLabel("Speed: ");

        statsPanel.add(levelLabel);
        statsPanel.add(perceptionLabel);
        statsPanel.add(fortitudeLabel);
        statsPanel.add(reflexLabel);
        statsPanel.add(willLabel);
        statsPanel.add(speedLabel);

        return statsPanel;
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
        JButton addToEncounterButton = new JButton("Add to Encounter");

        actionPanel.add(deleteButton);
        deleteButton.addActionListener(e -> showDeleteCombatantDialog());
        actionPanel.add(addButton);
        addButton.addActionListener(e -> showAddCombatantDialog());
        actionPanel.add(editButton);
        editButton.addActionListener(e -> showEditCombatantDialog());
        actionPanel.add(addToEncounterButton);
        addToEncounterButton.addActionListener(e -> addSelectedToEncounter());
        return actionPanel;
    }

    public void addSelectedToEncounter(){
        int selectedRow = combatantTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }
        int modelRow = combatantTable.convertRowIndexToModel(selectedRow);
        encounterGUI.addExistingCombatant(currentResults.get(modelRow));
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
        PF2eStatBlock selectedStats = DatabaseManager.getPF2eStats(selectedCombatant.getDatabaseId());
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel statsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JPanel basicPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.add(basicPanel);
        inputPanel.add(statsPanel);
        JTextField[] statFields = addStats(statsPanel);
        if(selectedStats != null){
            statFields[0].setText(String.valueOf(selectedStats.getLevel()));
            statFields[1].setText(String.valueOf(selectedStats.getPerception()));
            statFields[2].setText(String.valueOf(selectedStats.getFortitude()));
            statFields[3].setText(String.valueOf(selectedStats.getReflex()));
            statFields[4].setText(String.valueOf(selectedStats.getWill()));
            statFields[5].setText(String.valueOf(selectedStats.getSpeed()));
        }

        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(selectedCombatant.getName());

        JLabel maxHPLabel = new JLabel("Max HP:");
        JTextField maxHPField = new JTextField(String.valueOf(selectedCombatant.getMaxHP()));

        JLabel acLabel = new JLabel("Armor Class:");
        JTextField acField = new JTextField(String.valueOf(selectedCombatant.getArmorClass()));

        JLabel categoryLabel = new JLabel("Category:");
        String[] choices = {"Player", "NPC", "Monster"};
        JComboBox<String> categoryBox = new JComboBox<>(choices);
        categoryBox.setSelectedItem(selectedCombatant.getCategory());
        basicPanel.add(nameLabel);
        basicPanel.add(nameField);
        basicPanel.add(maxHPLabel);
        basicPanel.add(maxHPField);
        basicPanel.add(acLabel);
        basicPanel.add(acField);
        basicPanel.add(categoryLabel);
        basicPanel.add(categoryBox);
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

            boolean allEmpty = true;
            boolean anyEmpty = false;

            for (JTextField field : statFields) {
                if (field.getText().trim().isEmpty()) {
                    anyEmpty = true;
                } else {
                    allEmpty = false;
                }
            }
            if (anyEmpty && !allEmpty) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please fill in all stat fields, or leave them all empty."
                );
                return;
            }

            if(maxHP < 1 || armorClass < 1){
                JOptionPane.showMessageDialog(frame, "Please enter Max HP of at least 1, and Armor Class of at least 1.");
                return;
            }
            //Update combatant info
            selectedCombatant.setMaxHP(maxHP);
            selectedCombatant.setArmorClass(armorClass);
            selectedCombatant.setCategory(category);
            selectedCombatant.setName(name);

            boolean success = DatabaseManager.updateCombatant(selectedCombatant);//NEED SOME TRY/CATCH FOR THIS AS WELL CURRENTLY NO CHECKING FOR IF THIS FAILS

            if (!success) {
                JOptionPane.showMessageDialog(frame, "Failed to update combatant.");
            }

            //Handle stat block changes
            boolean statsSuccess = true;

            if (allEmpty) {
                if (selectedStats != null) {
                    statsSuccess = DatabaseManager.deleteStats(selectedCombatant);
                }
            } else {
                PF2eStatBlock newStats = getNewStats(statFields);

                if (selectedStats != null) {
                    statsSuccess = DatabaseManager.updateStats(selectedCombatant, newStats);
                } else {
                    statsSuccess = DatabaseManager.insertStats(selectedCombatant, newStats);
                }
            }
            if(statsSuccess){
                updateTable(DatabaseManager.getAllCombatants());
                JOptionPane.showMessageDialog(frame, "Combatant updated successfully");
            }else{
                JOptionPane.showMessageDialog(frame, "Combatant updated, but failed to update stat block.");
            }

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

    private static PF2eStatBlock getNewStats(JTextField[] statFields) {
        int level = Integer.parseInt(statFields[0].getText().trim());
        int perception = Integer.parseInt(statFields[1].getText().trim());
        int fortitude = Integer.parseInt(statFields[2].getText().trim());
        int reflex = Integer.parseInt(statFields[3].getText().trim());
        int will = Integer.parseInt(statFields[4].getText().trim());
        int speed = Integer.parseInt(statFields[5].getText().trim());
        PF2eStatBlock newStats = new PF2eStatBlock(
                level, perception, fortitude, reflex, will, speed);
        newStats.setLevel(level);
        newStats.setPerception(perception);
        newStats.setFortitude(fortitude);
        newStats.setReflex(reflex);
        newStats.setWill(will);
        newStats.setSpeed(speed);
        return newStats;
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
