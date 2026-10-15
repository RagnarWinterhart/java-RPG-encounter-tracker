import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
}
