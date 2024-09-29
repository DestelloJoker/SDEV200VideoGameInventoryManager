/*Program Name: DBUtil.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: (Not complete yet) This program is the utility class that helps to handle all parts related to the use of a database,
 * connecting to the database. This class also handles the inserting of items, querying of items,
 * deleting of items, and getting of items
 */
package com.example.javafinalvideogameinventory;

// Used to Log errors that may occur
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;

public class DBUtil {

    // The details for connection to the database
    private static final String DB_URL = "jdbc:mysql://yourhostname:port number usually 3306/yourschema";
    private static final String DB_USER = "username usually root";
    private static final String DB_PASSWORD = "password to your database";

    // Logger instance for logging errors
    private static final Logger logger = Logger.getLogger(DBUtil.class.getName());

    // Connect to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Insert an item into the database (with optional additional_info)
    public static void insertItem(Item item) {
        String sql = "INSERT INTO videogameinventoryschema.inventory (name, description, type, quantity, rarity, additional_info) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, item.getName());
                stmt.setString(2, item.getDescription());
                stmt.setString(3, item.getType());
                stmt.setInt(4, item.getQuantity());
                stmt.setString(5, item.getRarity());
                stmt.setString(6, item.getAdditionalInfo());  // This can be null

                stmt.executeUpdate();
            }
        } // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // Retrieve all items from the database
    public static ResultSet getItems() {
        String query = "SELECT * FROM videogameinventoryschema.inventory";

        try (Connection connection = connect()) {
            assert connection != null;
            try (Statement stmt = connection.createStatement()) {
                return stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Query and print all items (for console-based inspection)
    public static void queryItems() {
        String sql = "SELECT * FROM videogameinventoryschema.inventory";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    String rarity = rs.getString("rarity");
                    String additionalInfo = rs.getString("additional_info");

                    System.out.println(name + " - " + description + " (" + rarity + ")" +
                            (additionalInfo != null ? " with " + additionalInfo : ""));
                }
            } // Logs any errors that may occur during the runtime
        } // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // Remove all items from the database
    public static void removeAllItems() {
        String sql = "DELETE FROM videogameinventoryschema.inventory";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
                System.out.println("All items magically vanished without a trace.");
            } // Logs any errors that may occur during the runtime
        } // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }
}
