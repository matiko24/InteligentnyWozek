package com.example.mateusz.inteligentnywozek.general;

/**
 * Created by Mateusz on 2015-11-29.
 */
public class Purchase {
    long list_id;
    long product_id;

    public Purchase(long list_id, long product_id) {
        this.list_id = list_id;
        this.product_id = product_id;
    }

    public long getProduct_id() {

        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getList_id() {

        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }
}
