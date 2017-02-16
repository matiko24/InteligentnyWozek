package com.example.mateusz.inteligentnywozek.DBAdaptersTests;

import android.content.Context;
import android.test.ActivityTestCase;
import android.test.mock.MockContext;

import com.example.mateusz.inteligentnywozek.database.ProductDBAdapter;

import org.junit.Test;

/**
 * Created by Mateusz on 2016-09-12.
 */
public class ProductDBAdapterTest extends ActivityTestCase {

    Context context = new MockContext();
    ProductDBAdapter db = new ProductDBAdapter(context);

    @Test
    public void getProductLastPurchaseDateTest(){
        assertNotNull(db.getProductLastPurchaseDate(1L));
    }

    @Test
    public void getAllProductsTest(){
        assertNotNull(db.getAllProducts(""));
    }
}
