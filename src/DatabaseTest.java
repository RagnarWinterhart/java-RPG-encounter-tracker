import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        try(Connection connection = DatabaseManager.connect();
            Statement statement = connection.createStatement();){
                int rowsAffected = statement.executeUpdate("INSERT INTO combatant(name, max_hp, " +
                    "armor_class, category_id) VALUES ('Goblin', 7, 15, 1);");
//                int delete = statement.executeUpdate("DELETE FROM combatant;");
                try(ResultSet results = statement.executeQuery(
                        "SELECT name, id FROM combatant;")){
                    while (results.next()) {
                        IO.print(results.getInt("id"));
                        IO.print(" ");
                        IO.println(results.getString("name"));
                    }
                }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}
