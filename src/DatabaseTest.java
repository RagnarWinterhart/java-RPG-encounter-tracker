import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:encounter_tracker.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to SQLite successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/*
CREATE TABLE category(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE combatant(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    max_hp INTEGER NOT NULL,
    armor_class INTEGER NOT NULL,
    category_id INTEGER NOT NULL REFERENCES category(id)
);

CREATE TABLE creature_type(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE combatant_creature_type(
    combatant_id INTEGER NOT NULL REFERENCES combatant(id),
    type_id INTEGER NOT NULL REFERENCES creature_type(id),
    PRIMARY KEY (combatant_id, type_id)
);
 */
