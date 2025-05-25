package projects.farmersbay.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URL;
import java.io.File;

/**
 * Database connection handler for SQLite.
 */
public class Database {
    
    private static Connection connection;

    /**
     * Connect to the SQLite database in the resources/database folder.
     * @return Connection object or null if failed
     */
    public static Connection connect() {
        if (connection != null) {
            return connection;
        }

        try {
            // Get the file from the classpath
            URL resource = Database.class.getResource("/database/farmersbay.sqlite");
            if (resource == null) {
                throw new IllegalArgumentException("Database file not found in resources/database folder!");
            }

            // Convert URL to a usable file path
            String url = "jdbc:sqlite:" + new File(resource.toURI()).getAbsolutePath();

            // Connect to the database
            connection = DriverManager.getConnection(url);
            System.out.println("✅ Connected to SQLite database.");
            return connection;

        } catch (Exception e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Close the connection (optional).
     */
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database: " + e.getMessage());
        }
    }
}
