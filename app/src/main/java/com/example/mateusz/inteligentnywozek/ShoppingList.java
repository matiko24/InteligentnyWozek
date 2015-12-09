package com.example.mateusz.inteligentnywozek;

/**
 * Created by Mateusz on 2015-11-28.
 */
public class ShoppingList {
    long id;
    String name;

    public ShoppingList(String name) {
        this.name = name;
    }

    public ShoppingList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShoppingList(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
