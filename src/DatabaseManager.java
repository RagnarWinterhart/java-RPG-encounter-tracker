import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:encounter_tracker.db";

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        }
        return connection;
    }

    public static void initializeDatabase(){
        try(Connection connection = connect();
                InputStream input = DatabaseManager.class
                        .getResourceAsStream("resources/schema.sql")
                ){
            if(input == null){
                IO.println("Could not find schema.sql");
                return;
            }
            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );
            try(Statement statement = connection.createStatement()){
                for(String command : sql.split(";")){
                    if(!command.isBlank()){
                        statement.execute(command);
                    }
                }
            }

        }catch(SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void insertCombatant(Combatant combatant){
//        int rowsAffected = statement.executeUpdate("INSERT INTO combatant(name, max_hp, " +
//                "armor_class, category_id) VALUES ('Goblin', 7, 15, 1);");

        
    }

    public static int getCategoryId(String categoryName){
        int targetCategory = -1;
        String sql = "SELECT id FROM category WHERE name = ?;";
        try(Connection conn = DatabaseManager.connect();
            PreparedStatement selectStmt = conn.prepareStatement(sql)){
            selectStmt.setString(1, categoryName);
            try(ResultSet results = selectStmt.executeQuery()){

               if (results.next()) {
                    targetCategory = results.getInt("id");
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return targetCategory;
    }

}
