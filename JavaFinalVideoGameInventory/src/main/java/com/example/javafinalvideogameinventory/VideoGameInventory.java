/*Program Name: VideoGameInventory.java
 * Authors: Austin P
 * Date last Updated: 10/12/2024
 * Purpose: This program uses both javaFX and mySQL and is the driver class to
 * item.java, DBUtil.java, and InventoryManager.java
 * This class creates the GUI that allows the user to add their own items to a database,
 * it also allows for the user to delete all items from the database, query all items from the database
 * and finally do single query and deleting based on the inputted ID and can update the item at a specific id
 * by re-adding it.
 */

package com.example.javafinalvideogameinventory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.stage.Popup;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Objects;

public class VideoGameInventory extends Application {
    private final InventoryManager inventoryManager = new InventoryManager();
    private Button addButton;  // Declared as a class-level field


    // Declare fields at the class level
    private TextField nameField;
    private TextField descriptionField;
    private ComboBox<String> typeComboBox;
    private TextField quantityField;
    private TextField rarityField;
    private TextField idInputField;  // ID input field for deletion

    @Override
    public void start(Stage primaryStage) {
        // Initialize all the UI Components with labels
        Label nameLabel = new Label("Name: ");
        nameField = new TextField();  // Initialize at the class level

        Label descriptionLabel = new Label("Description: ");
        descriptionField = new TextField();

        Label typeLabel = new Label("Type: ");
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Weapon", "Potion", "Armor", "KeyItem");

        Label quantityLabel = new Label("Quantity: ");
        quantityField = new TextField();

        Label rarityLabel = new Label("Rarity: ");
        rarityField = new TextField();

        // ID input field that will be used for single delete and query functionality
        Label idLabel = new Label("ID: ");
        idInputField = new TextField();

        addButton = new Button("Add Item");
        Button deleteSingleButton = new Button("Delete");
        Button deleteButton = new Button("Delete All Items");
        Button queryAllButton = new Button("Query All Items");
        Button querySingleButton = new Button("Query Item by ID");

        // Event Handlers for the buttons
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String type = typeComboBox.getValue();  // Get selected value
                int quantity = Integer.parseInt(quantityField.getText());
                String rarity = rarityField.getText();

                if (name.isEmpty() || description.isEmpty() || type == null || rarity.isEmpty()) {
                    showErrorPopup("Error", "Please fill in all fields.");
                } else {
                    inventoryManager.addItem(name, description, type, quantity, rarity);
                    showItemPopup(type, name);  // Show item image popup based on type
                    clearInputFields();  // Clear input fields after adding an item
                }
            } catch (NumberFormatException ex) {
                showErrorPopup("Error", "Please enter a valid number for quantity.");
            }
        });

        deleteSingleButton.setOnAction(event -> {
            String idText = idInputField.getText();

            // Validate that the input is a number in the ID inputbox
            if (!idText.matches("\\d+")) {
                showErrorPopup("Invalid ID", "The ID must be a valid number.");
                return;
            }

            int id = Integer.parseInt(idText);

            // Call the delete method
            boolean isDeleted = DBUtil.deleteItemById(id);

            if (isDeleted) {
                showSuccessPopup("Item with ID " + id + " has been deleted.");
                // Clear input fields after successful deletion
                clearInputFields();
            } else {
                showErrorPopup("Error", "No item found with ID: " + id);
            }
        });

        deleteButton.setOnAction(e -> inventoryManager.removeAllItems());

        // Query all items and print them to the console
        queryAllButton.setOnAction(e -> DBUtil.queryItems());

        // Query a single item by its ID
        querySingleButton.setOnAction(e -> {
            String idText = idInputField.getText();

            if (!idText.matches("\\d+")) {
                showErrorPopup("Invalid ID", "The ID must be a valid number.");
                return;
            }

            int id = Integer.parseInt(idText);
            // Print an item to the console based on the id inputted if it exists at that ID
            DBUtil.queryItemById(id);
        });

        // Layout for each field
        HBox nameBox = new HBox(10, nameLabel, nameField);
        HBox descriptionBox = new HBox(10, descriptionLabel, descriptionField);
        HBox typeBox = new HBox(10, typeLabel, typeComboBox);
        HBox quantityBox = new HBox(10, quantityLabel, quantityField);
        HBox rarityBox = new HBox(10, rarityLabel, rarityField);
        HBox idBox = new HBox(10, idLabel, idInputField);

        // Overall layout
        HBox buttonBox = new HBox(10, addButton, deleteButton, deleteSingleButton);
        HBox queryBox = new HBox(10, queryAllButton, querySingleButton);
        VBox layout = new VBox(10, nameBox, descriptionBox, typeBox, quantityBox, rarityBox, idBox, buttonBox, queryBox);

        layout.setPadding(new Insets(20));

        // Color and theme styles added to the VBox and other components
        layout.setStyle("-fx-background-color: darkblue; -fx-border-color: lightgrey; -fx-border-width: 2px;");
        nameLabel.setStyle("-fx-text-fill: white;");
        descriptionLabel.setStyle("-fx-text-fill: white;");
        typeLabel.setStyle("-fx-text-fill: white;");
        quantityLabel.setStyle("-fx-text-fill: white;");
        rarityLabel.setStyle("-fx-text-fill: white;");
        idLabel.setStyle("-fx-text-fill: white;");
        nameField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        descriptionField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        typeComboBox.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        quantityField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        rarityField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        idInputField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        addButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        deleteSingleButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        querySingleButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        queryAllButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");

        // Scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Inventory");

        // Stops the random item generator and closes the application fully
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            // Stops the timer in InventoryManager so the program closes fully
            inventoryManager.stopRandomItemGenerator();
            // This line Ensures the program exits completely
            System.exit(0);
        });

        primaryStage.show();
    }

    // Method that clears all input fields
    private void clearInputFields() {
        nameField.clear();
        descriptionField.clear();
        typeComboBox.setValue(null);
        quantityField.clear();
        rarityField.clear();
        idInputField.clear();
    }

    // Method that shows a popup if an item is successfully deleted from the single delete button
    private void showSuccessPopup( String message) {
        Stage successStage = new Stage();
        successStage.initStyle(StageStyle.UTILITY);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label successMessage = new Label(message);
        successMessage.setStyle("-fx-text-fill: green;");

        Button closeButton1 = new Button("Close");
        closeButton1.setOnAction(e -> successStage.close());

        layout.getChildren().addAll(new Label("Item Deleted"), successMessage, closeButton1);

        Scene scene = new Scene(layout, 300, 150);
        successStage.setScene(scene);
        successStage.show();
    }

    // Method that shows a popup if an item isn't successfully deleted from the single delete button
    private void showErrorPopup(String title, String message) {
        Stage errorStage = new Stage();
        errorStage.initStyle(StageStyle.UTILITY);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label errorMessage = new Label(message);
        errorMessage.setStyle("-fx-text-fill: red;");

        Button closeButton2 = new Button("Close");
        closeButton2.setOnAction(e -> errorStage.close());

        layout.getChildren().addAll(new Label(title), errorMessage, closeButton2);

        Scene scene = new Scene(layout, 300, 150);
        errorStage.setScene(scene);
        errorStage.show();
    }
    // Store popups globally to manage them
    private Popup addedItemPopup;

    // Method to show the image popup for user-created items on the right side of the UI
    private void showItemPopup(String itemType, String itemName) {

        // Hide the previous user-created item popup if it exists
        if (addedItemPopup != null && addedItemPopup.isShowing()) {
            addedItemPopup.hide();
        }

        // Create a new popup instance for a user-created item
        addedItemPopup = new Popup();
        ImageView imageView = getImageView(itemType);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Set the same background color and border for the popup layout
        layout.setStyle("-fx-background-color: darkblue; -fx-border-color: lightgrey; -fx-border-width: 2px;");

        // Create a label for the item name and style it
        Label nameLabel = new Label(itemName);
        nameLabel.setStyle("-fx-text-fill: white;");

        // Set the image and the label in the layout
        layout.getChildren().addAll(nameLabel, imageView);

        // Add the layout to the popup content
        addedItemPopup.getContent().add(layout);
        addedItemPopup.setAutoHide(true);

        // Show the popup on the right side of the window
        Stage currentStage = getCurrentStage();
        addedItemPopup.show(currentStage, currentStage.getX() + currentStage.getWidth() + 20, currentStage.getY());

        // Close the popup after 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> addedItemPopup.hide());
        pause.play();
    }


    // Method that is meant to load the image view based on item type
    private ImageView getImageView(String itemType) {
        ImageView imageView = new ImageView();

        // Load image based on item type
        String imageUrl = switch (itemType) {
            case "Weapon" -> getClass().getResource("/images/sword_PNGForFinal.png") != null
                    ? Objects.requireNonNull(getClass().getResource("/images/sword_PNGForFinal.png")).toExternalForm()
                    : null;
            case "Potion" -> getClass().getResource("/images/Potion_ImageForFinal.png") != null
                    ? Objects.requireNonNull(getClass().getResource("/images/Potion_ImageForFinal.png")).toExternalForm()
                    : null;
            case "Armor" -> getClass().getResource("/images/Chestplate_JpegForFinal.jpg") != null
                    ? Objects.requireNonNull(getClass().getResource("/images/Chestplate_JpegForFinal.jpg")).toExternalForm()
                    : null;
            case "KeyItem" -> getClass().getResource("/images/KeyItem_PNGForFinal.png") != null
                    ? Objects.requireNonNull(getClass().getResource("/images/KeyItem_PNGForFinal.png")).toExternalForm()
                    : null;
            default -> null;
        };

        if (imageUrl != null) {
            Image image = new Image(imageUrl);
            imageView.setImage(image);
            imageView.setFitWidth(100);  // Adjust width
            imageView.setFitHeight(100); // Adjust height
        } else {
            System.out.println("Image for type " + itemType + " not found.");
        }

        return imageView;
    }

    // Utility method to get the current stage
    private Stage getCurrentStage() {
        return (Stage) addButton.getScene().getWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
