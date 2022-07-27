package com.photon.model;

/**
 * Creates InvoiceLine that holds all line details.
 */
public class InvoiceLine {
    private String itemName;
    private double itemPrice;
    private int count;

    public InvoiceLine(String itemName, double itemPrice, int count){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    public String[] toArray(){
        return new String[]{itemName, String.valueOf(itemPrice), String.valueOf(count)};
    }

    public double totalPrice(){
        return Math.round((itemPrice * (double) count) * 100) / 100.0;
    }

    public String getDescription() {
        return itemName;
    }

    public void setDescription(String description) {
        this.itemName = description;
    }

    public double getPrice() {
        return itemPrice;
    }

    public void setPrice(double price) {
        this.itemPrice = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return itemName + ", " + itemPrice + ", " + count;
    }
}
