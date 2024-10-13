/*Program Name: InventoryManager.java
 * Authors: Austin P
 * Date last Updated: 10/12/2024
 * Purpose: This program is meant to handle all management interactions for the inventory table. Such as randomly
 * generating a new item (simulating obtaining a random item pickup in a video game in a way), adding an item, removing
 * items, viewing items, and the creation of the items
 */

package com.example.javafinalvideogameinventory;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class InventoryManager {

    private final List<Item> inventory;
    // Timer meant for controlling things like the popup time and length of RNG generation time
    private Timer timer;
    // Logger instance for logging errors
    private static final Logger logger = Logger.getLogger(InventoryManager.class.getName());

    public InventoryManager() {
        inventory = new ArrayList<>();
        startRandomItemGenerator();
    }

    public void addItem(String name, String description, String type, int quantity, String rarity) {
        Item item = createItem(name, description, type, quantity, rarity);
        inventory.add(item);
        DBUtil.insertItem(item);
    }

    public void addItem(Item item) {
        inventory.add(item);
        DBUtil.insertItem(item);
    }

    public void removeAllItems() {
        inventory.clear();
        DBUtil.removeAllItems();
        System.out.println("All items magically vanished without a trace.");
    }

    private void startRandomItemGenerator() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                Item randomItem = generateRandomItem(random);
                addItem(randomItem);
                System.out.println("Randomly generated item: " + randomItem.getDetails());

                // Show the pop-up window for the new item
                Platform.runLater(() -> showNewItemWindow(randomItem));
            }
        }, 0, 30000); // Runs every 30 seconds
    }
    // Method meant to stop the random item generator
    public void stopRandomItemGenerator() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // Method for generating a random item and making sure you don't get a random item with weird stats
    // Like a sword the is of type potion that has the description to unlock doors
    private Item generateRandomItem(Random random) {
        String randomName;
        String randomDescription;
        String randomType;
        String randomRarity;
        String additionalInfo = null;

        // Initialize parallel arrays of all set potential items and their descriptions
        String[] weaponNames = {"Sword", "Axe", "Bow", "CrossBow"};
        String[] weaponDescriptions = {"Sharp blade", "Heavy axe", "Long-range bow", "Short-range CrossBow"};

        String[] potionNames = {"Health Potion", "Mana Potion", "Strength Potion"};
        String[] potionDescriptions = {"Restores health", "Restores mana", "Raises strength"};

        String[] armorNames = {"Helmet", "Chest plate", "Leggings", "Boots"};
        String[] armorDescriptions = {"Protects the head", "Protects the body", "Protects the legs", "Protects the feet"};

        String[] keyItemNames = {"Ancient Key", "Magic Amulet", "Half Crescent Moon Shard"};
        String[] keyItemDescriptions = {"Unlocks doors", "Grants magical powers", "Necessary for completing the game"};

        // Initialized array of the potential rarity types that the randomly generate item could have
        String[] rarities = {"Common", "Uncommon", "Rare", "Epic", "Legendary"};

        int typeChoice = random.nextInt(4);

        // Switch case that is based on the random generation between 1-4 for the item you'll get
        // THe description and names are based on a random choice as well
        switch (typeChoice) {
            case 0 -> {
                randomType = "Weapon";
                int weaponChoice = random.nextInt(weaponNames.length);
                randomName = weaponNames[weaponChoice];
                randomDescription = weaponDescriptions[weaponChoice];
            }
            case 1 -> {
                randomType = "Potion";
                int potionChoice = random.nextInt(potionNames.length);
                randomName = potionNames[potionChoice];
                randomDescription = potionDescriptions[potionChoice];
            }
            case 2 -> {
                randomType = "Armor";
                int armorChoice = random.nextInt(armorNames.length);
                randomName = armorNames[armorChoice];
                randomDescription = armorDescriptions[armorChoice];
            }
            case 3 -> {
                randomType = "KeyItem";
                int keyItemChoice = random.nextInt(keyItemNames.length);
                randomName = keyItemNames[keyItemChoice];
                randomDescription = keyItemDescriptions[keyItemChoice];
            }
            default -> throw new IllegalArgumentException("Invalid item type");
        }

        randomRarity = rarities[random.nextInt(rarities.length)];

        if (randomRarity.equals("Legendary")) {
            additionalInfo = "Special item info";
        }

        return createItem(randomName, randomDescription, randomType, random.nextInt(5) + 1, randomRarity, additionalInfo);
    }

    // Method for creating an item
    private Item createItem(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        return switch (type) {
            case "Weapon" -> new Weapon(name, description, type, quantity, rarity, additionalInfo);
            case "Potion" -> new Potion(name, description, type, quantity, rarity, additionalInfo);
            case "Armor" -> new Armor(name, description, type, quantity, rarity, additionalInfo);
            case "KeyItem" -> new KeyItem(name, description, type, quantity, rarity, additionalInfo);
            default -> throw new IllegalArgumentException("Invalid item type");
        };
    }

    private Item createItem(String name, String description, String type, int quantity, String rarity) {
        return createItem(name, description, type, quantity, rarity, null);
    }

    // Method to show the new item window for the randomly made item on the left of the UI
    private void showNewItemWindow(Item item) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED); // Remove default window decorations

        // Sets the color scheme to be the same as the main UI
        VBox layout = new VBox();
        layout.setStyle("-fx-background-color: darkblue; -fx-padding: 10px; -fx-border-color: lightgrey; -fx-border-width: 2px;");
        layout.setSpacing(10);

        Label headerLabel = new Label("New Item Got");
        headerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label nameLabel = new Label("Name: " + item.getName());
        nameLabel.setStyle("-fx-text-fill: white;");
        Label typeLabel = new Label("Type: " + item.getType());
        typeLabel.setStyle("-fx-text-fill: white;");
        Label descriptionLabel = new Label("Description: " + item.getDescription());
        descriptionLabel.setStyle("-fx-text-fill: white;");
        Label quantityLabel = new Label("Quantity: " + item.getQuantity());
        quantityLabel.setStyle("-fx-text-fill: white;");
        Label rarityLabel = new Label("Rarity: " + item.getRarity());
        rarityLabel.setStyle("-fx-text-fill: white;");

        layout.getChildren().addAll(headerLabel, nameLabel, typeLabel, descriptionLabel, quantityLabel, rarityLabel);

        Scene scene = new Scene(layout);
        stage.setScene(scene);

        // Position the stage on the left side of the screen
        stage.setX(700);
        stage.setY(500);

        stage.show();

        // Close the window after 3 seconds
        TimerTask closeTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(stage::close);
            }
        };
        Timer closeTimer = new Timer();
        closeTimer.schedule(closeTask, 3000); // 3 seconds delay
    }
}