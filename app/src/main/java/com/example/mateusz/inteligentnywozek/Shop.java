package com.example.mateusz.inteligentnywozek;

/**
 * Created by Mateusz on 2015-12-09.
 */
public class Shop {
    Long id;
    String name;
    Integer numberOfPurchases;

    public Shop(Long id, String name, Integer numberOfPurchases) {
        this.id = id;
        this.name = name;
        this.numberOfPurchases = numberOfPurchases;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfPurchases() {
        return numberOfPurchases;
    }

    public void setNumberOfPurchases(Integer numberOfPurchases) {
        this.numberOfPurchases = numberOfPurchases;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
