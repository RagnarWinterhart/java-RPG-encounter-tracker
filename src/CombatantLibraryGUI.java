import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CombatantLibraryGUI {
    private JTextField searchField;
    private JComboBox<String> searchByBox;
    private final DefaultTableModel model;

    //JTable
    //Search Controls --> search bar and drop down for criteria (name/category/creature_type)
    //New/Edit/Delete buttons
    //Add to Encounter button
    public CombatantLibraryGUI(){
        JFrame frame = new JFrame("Combatant Library");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String[] columns = {"Name", "Category", "Hit Points", "AC"};
        List<Combatant> combatants = DatabaseManager.searchCombatant("", "Name");
        model = new DefaultTableModel(columns, 0){ //prevents cells in the table from being directly edited
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable combatantTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(combatantTable);
        for(Combatant c : combatants){
            Object[] row = {
                    c.getName(),
                    c.getCategory(),
                    c.getMaxHP(),
                    c.getArmorClass()
            };
            model.addRow(row);
        }

        frame.add(createSearchPanel(), BorderLayout.NORTH);
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
            model.setRowCount(0);
            for(Combatant c : results){
                Object[] row = {
                        c.getName(),
                        c.getCategory(),
                        c.getMaxHP(),
                        c.getArmorClass()
                };
                model.addRow(row);
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchByBox);
        return searchPanel;

    }

//    public searchButton(){
//
//    }

}
