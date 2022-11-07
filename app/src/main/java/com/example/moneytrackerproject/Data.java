package com.example.moneytrackerproject;

// The Data class stores the information of each spending instance
public class Data {

    private String item;
    private String date;
    private String id;
    private String notes;
    private int quantity;

    public Data(){

    }

    public Data(String item, String date, String id, String notes, int quantity) {
        this.item = item;
        this.date = date;
        this.id = id;
        this.notes = notes;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setAmount(int quantity) {
        this.quantity = quantity;
    }
}
