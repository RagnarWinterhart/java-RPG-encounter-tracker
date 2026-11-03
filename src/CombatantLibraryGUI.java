import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CombatantLibraryGUI {
    private JFrame frame;
    private JTable combatantTable;


    public CombatantLibraryGUI(){
        frame = new JFrame("Combatant Library");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String[] columns = {"Name", "Category", "Hit Points", "AC"};
        List<Combatant> combatants = DatabaseManager.searchCombatant("", "Name");
        DefaultTableModel model = new DefaultTableModel(columns, 0){ //prevents cells in the table from being directly edited
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        combatantTable = new JTable(model);
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
        frame.add(scrollPane);
        frame.setVisible(true);
    }




    public static void main(String[] args) {
        //JTable
        //Search Controls --> search bar and drop down for criteria (name/category/creature_type)
        //New/Edit/Delete buttons
        //Add to Encounter button


    }

}
