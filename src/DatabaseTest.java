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
