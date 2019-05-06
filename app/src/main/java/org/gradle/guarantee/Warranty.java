package org.gradle.guarantee;

public class Warranty {
    int id;
    String itemName;
    String date;

    // Constructor without params
    public Warranty() { }

    // Constructor with params
    public Warranty(int pID, String pItemName, String pDate) {
        this.id = pID;
        this.itemName = pItemName;
        this.date = pDate;
    }

    public  int getID() {
        return id;
    }

    public void setID(int pID) {

        this.id = pID;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }
}