/*Program Name: Item.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: (Not complete yet) This program is the item class that defines what each item is
 */

package com.example.javafinalvideogameinventory;

public abstract class Item {
    protected String name;
    protected String description;
    protected String type;
    protected int quantity;
    protected String rarity;
    protected String additionalInfo;

    public Item(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.rarity = rarity;
        this.additionalInfo = additionalInfo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRarity() {
        return rarity;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public abstract String getDetails();
}

class Weapon extends Item {
    public Weapon(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        super(name, description, type, quantity, rarity, additionalInfo);
    }

    @Override
    public String getDetails() {
        return "Weapon: " + name + " - " + description + " (" + rarity + ")" +
                (additionalInfo != null ? " with " + additionalInfo : "");
    }
}

class Potion extends Item {
    public Potion(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        super(name, description, type, quantity, rarity, additionalInfo);
    }

    @Override
    public String getDetails() {
        return "Potion: " + name + " - " + description + " (" + rarity + ")" +
                (additionalInfo != null ? " with " + additionalInfo : "");
    }
}

class Armor extends Item {
    public Armor(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        super(name, description, type, quantity, rarity, additionalInfo);
    }

    @Override
    public String getDetails() {
        return "Armor: " + name + " - " + description + " (" + rarity + ")" +
                (additionalInfo != null ? " with " + additionalInfo : "");
    }
}

class KeyItem extends Item {
    public KeyItem(String name, String description, String type, int quantity, String rarity, String additionalInfo) {
        super(name, description, type, quantity, rarity, additionalInfo);
    }

    @Override
    public String getDetails() {
        return "KeyItem: " + name + " - " + description + " (" + rarity + ")" +
                (additionalInfo != null ? " with " + additionalInfo : "");
    }
}
