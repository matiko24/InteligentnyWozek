package com.example.mateusz.inteligentnywozek;

import java.util.Date;

/**
 * Created by Mateusz on 2015-11-25.
 */
public class Product {

    private long id;
    private String name;

    public Product(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
