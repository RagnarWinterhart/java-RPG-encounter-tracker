import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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
        String sql = "INSERT INTO combatant(name, max_hp, armor_class, category_id) " +
                "VALUES (?, ?, ?, ?);";
        try(Connection conn = DatabaseManager.connect();
        PreparedStatement insertStmt = conn.prepareStatement(sql)){
            insertStmt.setString(1, combatant.getName());
            insertStmt.setInt(2, combatant.getMaxHP());
            insertStmt.setInt(3, combatant.getArmorClass());
            insertStmt.setInt(4, getCategoryId(combatant.getCategory()));
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Should sanitize and trim the param before it gets here (and make case insensitive)
    // searchBy should be a dropdown list of each column for the combatant, and joined columns
    public static List<Combatant> searchCombatant(String searchParam, String searchBy){
        StringBuilder sql = new StringBuilder("SELECT combatant.*, category.name AS category_name FROM combatant " +
                "INNER JOIN category ON combatant.category_id = category.id");
        searchParam = "%" + searchParam + "%";
        try(Connection conn = DatabaseManager.connect();){
            switch(searchBy){
                case "Name": sql.append(" WHERE combatant.name LIKE ?");
                            break;
                //I need to refactor old "Type" that meant NPC, PC, Monster into category, since we have creature type now.
                case "Category":
                    sql.append(" WHERE category.name LIKE ?");
                    break;
                case "Creature Type":
                    sql.append(" INNER " +
                            "JOIN combatant_creature_type ON " +
                            "combatant.id = combatant_creature_type.combatant_id " +
                            "INNER JOIN creature_type ON combatant_creature_type.creature_type_id " +
                            "= creature_type.id WHERE creature_type.name LIKE ?;");
                    break;
                default:
                    sql.append(" WHERE 1=1");
                    break;
            }
            PreparedStatement selectStmt = conn.prepareStatement(String.valueOf(sql));
            selectStmt.setString(1, searchParam);
            ResultSet results = selectStmt.executeQuery();

            List<Combatant> search = new ArrayList<>();
            while(results.next()){
                int id = results.getInt("id");
                String name = results.getString("name");
                int maxHP = results.getInt("max_hp");
                int armorClass = results.getInt("armor_class");
                String creatureCategory = results.getString("category_name");

                Combatant newCom = new Combatant(name, 0, maxHP, maxHP, armorClass, creatureCategory);
                newCom.setDatabaseId(id);
                search.add(newCom);
            }
            return search;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public static boolean updateCombatant(Combatant combatant){
        if(combatant.getDatabaseId() == null){
            return false;
        }
        int rowsAffected = -1;
        String sql = "UPDATE combatant SET name = ?, max_hp = ?, " +
                "armor_class = ?, category_id = ? WHERE id = ?";
        try(Connection conn = DatabaseManager.connect();
            PreparedStatement updateStmt = conn.prepareStatement(sql)){
            updateStmt.setString(1, combatant.getName());
            updateStmt.setInt(2, combatant.getMaxHP());
            updateStmt.setInt(3, combatant.getArmorClass());
            int categoryId = getCategoryId(combatant.getCategory());
            if(categoryId == -1){
                return false;
            }else{
                updateStmt.setInt(4, categoryId);
            }
            updateStmt.setInt(5, combatant.getDatabaseId());
            rowsAffected = updateStmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return rowsAffected == 1;
    }

    /*
    One thing to revisit once pf2e_stat_block and creature-type
    relationships are populated is what should happen to related rows when
    a combatant is deleted. That's where ON DELETE CASCADE may become relevant.
     */
    public static boolean deleteCombatant(Combatant combatant){
        if(combatant.getDatabaseId() == null){
            return false;
        }
        String sql = "DELETE FROM combatant WHERE id = ?";
        try(Connection conn = DatabaseManager.connect();
            PreparedStatement deleteStmt = conn.prepareStatement(sql)){
            deleteStmt.setInt(1, combatant.getDatabaseId());
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected == 1;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
