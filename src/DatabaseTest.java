import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        Combatant goblin = new Combatant("Goblin", 10, 7, 7, 15, "Monster");
        DatabaseManager.insertCombatant(goblin);

        try (Connection conn = DatabaseManager.connect();
             Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(
                     "SELECT name, max_hp, armor_class, category_id FROM combatant;")) {
//            statement.executeUpdate("DELETE FROM combatant;");
            while (results.next()) {
                IO.println(
                        results.getString("name") +
                                " | HP: " + results.getInt("max_hp") +
                                " | AC: " + results.getInt("armor_class") +
                                " | Category ID: " + results.getInt("category_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
