/*Program Name: VideoGameInventory.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: (Not complete yet) This program uses both javaFX and mySQL and is the driver class to
 * item.java, DBUtil.java, and InventoryManager.java
 * This class creates the GUI that allow the user to add their own items to a database,
 * the view function is still a work in progress
 */

package com.example.javafinalvideogameinventory;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VideoGameInventory extends Application {
    private final InventoryManager inventoryManager = new InventoryManager();

    @Override
    public void start(Stage primaryStage) {
        // Create all the UI Components with labels
        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField();

        Label descriptionLabel = new Label("Description: ");
        TextField descriptionField = new TextField();

        Label typeLabel = new Label("Type: ");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Weapon", "Potion", "Armor", "KeyItem");

        Label quantityLabel = new Label("Quantity: ");
        TextField quantityField = new TextField();

        Label rarityLabel = new Label("Rarity: ");
        TextField rarityField = new TextField();

        Button addButton = new Button("Add Item");
        Button viewButton = new Button("View Items");
        TableView<Item> tableView = new TableView<>();

        // Event Handlers
        addButton.setOnAction(e -> inventoryManager.addItem(
                nameField.getText(),
                descriptionField.getText(),
                typeComboBox.getValue(),
                Integer.parseInt(quantityField.getText()),
                rarityField.getText()
        ));

        viewButton.setOnAction(e -> inventoryManager.viewItems(tableView));

        // Layout for each field
        HBox nameBox = new HBox(10, nameLabel, nameField);
        HBox descriptionBox = new HBox(10, descriptionLabel, descriptionField);
        HBox typeBox = new HBox(10, typeLabel, typeComboBox);
        HBox quantityBox = new HBox(10, quantityLabel, quantityField);
        HBox rarityBox = new HBox(10, rarityLabel, rarityField);

        // Overall layout
        VBox layout = new VBox(10, nameBox, descriptionBox, typeBox, quantityBox, rarityBox, addButton, viewButton, tableView);
        layout.setPadding(new Insets(20));

        // Scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Inventory");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
