package com.example.main;

public class CartItem {
    public enum Type { ROLLO, PITA, BOX, WRAP }

    private final Type type;
    private final String size;  
    private final String meat;   
    private final int price;     

    public CartItem(Type type, String size, String meat, int price) {
        this.type = type;
        this.size = size;
        this.meat = meat;
        this.price = price;
    }

    public Type getType() { 
        return type; 
    }
   
    public String getSize() {
        return size; 
    }
    public String getMeat() {
        return meat; 
    }
    public int getPrice() {
        return price; 
    }

    @Override
    public String toString() {
        return type + " • " + size + " • " + meat + " • " + price + " zł";
    }
}