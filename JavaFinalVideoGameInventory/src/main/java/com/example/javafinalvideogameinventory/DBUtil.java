/*Program Name: DBUtil.java
 * Authors: Austin P
 * Date last Updated: 10/12/2024
 * Purpose: This program is the utility class that helps to handle all parts related to the use of a database,
 * connecting to the database. This class also handles the inserting of items, querying of items, and
 * deleting of items.
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

    // Tries to connect to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
            return null;
        }
    }

    // Inserts an item into the database from the user's inputted values for it
    public static void insertItem(Item item) {
        String sql = "INSERT INTO inventory (name, description, type, quantity, rarity, additional_info) VALUES (?, ?, ?, ?, ?, ?)";

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

    // Retrieves all items from the database
    public static ResultSet getItems() {
        String query = "SELECT * FROM inventory";

        try (Connection connection = connect()) {
            assert connection != null;
            try (Statement stmt = connection.createStatement()) {
                return stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
            return null;
        }
    }

    // Queries and print all items to the console if any are in the database
    public static void queryItems() {
        String sql = "SELECT * FROM inventory";

        // Checks to see if the program is connected
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
                    // Print all the relevant fields
                    System.out.println("Name: " + name +
                            ", Description: " + description +
                            ", Type: " + type +
                            ", Quantity: " + quantity +
                            ", Rarity: " + rarity +
                            (additionalInfo != null ? ", Additional Info: " + additionalInfo : ""));
                }
            } // Logs any errors that may occur during the runtime
        } // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }
    // Prints a query to the console of an item at the ID you inputted if the ID and item at the ID is valid
    public static void queryItemById(int id) {
        String sql = "SELECT * FROM inventory WHERE id = ?";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);  // Set the ID parameter
                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        String name = rs.getString("name");
                        String description = rs.getString("description");
                        String type = rs.getString("type");
                        int quantity = rs.getInt("quantity");
                        String rarity = rs.getString("rarity");
                        String additionalInfo = rs.getString("additional_info");

                        // Print all the relevant fields
                        System.out.println("Name: " + name +
                                ", Description: " + description +
                                ", Type: " + type +
                                ", Quantity: " + quantity +
                                ", Rarity: " + rarity +
                                (additionalInfo != null ? ", Additional Info: " + additionalInfo : ""));
                    } else {
                        System.out.println("No item found with ID: " + id);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // Removes an Item from the database from the inputted ID from the user
    public static boolean deleteItemById(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                // If at least one row is affected, an item at the ID is found and then deleted, return true
                return rowsAffected > 0;  
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
        // Return false if the item could not be deleted
        return false;
    }

    // Remove all items from the database
    public static void removeAllItems() {
        String sql = "DELETE FROM inventory";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
                System.out.println("All items magically vanished without a trace. How could this happen.");
            } // Logs any errors that may occur during the runtime
        } // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }
}
