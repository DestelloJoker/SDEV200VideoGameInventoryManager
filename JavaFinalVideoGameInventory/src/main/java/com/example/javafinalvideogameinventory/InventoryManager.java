/*Program Name: InventoryManager.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: (Not complete yet) This program is meant to handle all management interactions for the inventory table. Such as randomly
 * generating a new item (simulating obtaining a random item pickup in a video game in a way), adding an item, removing
 * items, viewing items, and the creation of the items
 */

package com.example.javafinalvideogameinventory;

import com.almasb.fxgl.inventory.Inventory;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
// Used to Log errors that may occur
import java.util.logging.Logger;
import java.util.logging.Level;

public class InventoryManager {

    private final List<Item> inventory;
    // Logger instance for logging errors
    private static final Logger logger = Logger.getLogger(InventoryManager.class.getName());
    public InventoryManager() {
        inventory = new ArrayList<>();
        startRandomItemGenerator();
    }

    // Add an item to the database under the table inventory
    public void addItem(String name, String description, String type, int quantity, String rarity) {
        Item item = createItem(name, description, type, quantity, rarity);
        inventory.add(item);
        DBUtil.insertItem(item);
    }

    // Overload method that adds an item directly to the inventory table
    public void addItem(Item item) {
        inventory.add(item);
        DBUtil.insertItem(item);
    }

    // This method is meant to view all items in a TableView (for JavaFX GUI) Not complete
    public void viewItems(TableView<Item> tableView) {
        try {
            ResultSet results = DBUtil.getItems();
            tableView.getItems().clear();

            while (true) {
                assert results != null;
                if (!results.next()) break;
                Item item = createItem(results.getString("name"), results.getString("description"), results.getString("type"), results.getInt("quantity"), results.getString("rarity"));
                tableView.getItems().add(item);
            }
        }  // Logs any errors that may occur during the runtime
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // This method is meant to remove all items from the table inventory in the database, not complete
    public void removeAllItems() {
        inventory.clear();
        DBUtil.removeAllItems();
        System.out.println("All items magically vanished without a trace.");
    }

    // This method randomly generates an item every 30 seconds
    private void startRandomItemGenerator() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                Item randomItem = generateRandomItem(random);
                addItem(randomItem);
                System.out.println("Randomly generated item: " + randomItem.getDetails());
            }
        }, 0, 30000);  // Every 30 seconds
    }

    // Generates a completely random item that has completely random stats, work in progress still
    private Item generateRandomItem(Random random) {
        String[] itemNames = {"Sword", "Health Potion", "Helmet", "Ancient Key"};
        String[] descriptions = {"Sharp blade", "Restores health", "Protects head", "Unlocks door"};
        String[] types = {"Weapon", "Potion", "Armor", "KeyItem"};
        String[] rarities = {"Common", "Uncommon", "Rare", "Legendary"};

        String randomName = itemNames[random.nextInt(itemNames.length)];
        String randomDescription = descriptions[random.nextInt(descriptions.length)];
        String randomType = types[random.nextInt(types.length)];
        String randomRarity = rarities[random.nextInt(rarities.length)];
        String additionalInfo = randomRarity.equals("Legendary") ? "Special item info" : null;

        return createItem(randomName, randomDescription, randomType, random.nextInt(5) + 1, randomRarity, additionalInfo);
    }

    // Create an item based on the type of the item
    private Item createItem(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        return switch (type) {
            case "Weapon" -> new Weapon(name, description, type, quantity, rarity, additionalInfo);
            case "Potion" -> new Potion(name, description, type, quantity, rarity, additionalInfo);
            case "Armor" -> new Armor(name, description, type, quantity, rarity, additionalInfo);
            case "KeyItem" -> new KeyItem(name, description, type, quantity, rarity, additionalInfo);
            default -> throw new IllegalArgumentException("Invalid item type");
        };
    }

    // Create an item without any additional info (used for the ResultSet and UI interaction)
    private Item createItem(String name, String description, String type, int quantity, String rarity) {
        return createItem(name, description, type, quantity, rarity, null);
    }
}

