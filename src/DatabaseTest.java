import java.util.List;

public class DatabaseTest {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        Combatant goblin = new Combatant("Goblin", 10, 7, 7, 15, "Monster");
        DatabaseManager.insertCombatant(goblin);
        List<Combatant> results = DatabaseManager.searchCombatant("gob", "Name");
        if(results.isEmpty()){
            IO.println("No combatant found.");
        }else{
            IO.println(results.get(0).getName());
            IO.println(results.get(0).getDatabaseId());
            Combatant foundGoblin = results.get(0);

            foundGoblin.setArmorClass(20);

            IO.println(DatabaseManager.updateCombatant(foundGoblin));
        }





//        try (Connection conn = DatabaseManager.connect();
//             Statement statement = conn.createStatement();
//             ResultSet results = statement.executeQuery(
//                     "SELECT name, max_hp, armor_class, category_id FROM combatant;")) {
//             statement.executeUpdate("DELETE FROM combatant;");
//            while (results.next()) {
//                IO.println(
//                        results.getString("name") +
//                                " | HP: " + results.getInt("max_hp") +
//                                " | AC: " + results.getInt("armor_class") +
//                                " | Category ID: " + results.getInt("category_id")
//                );
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
