package com.example.mateusz.inteligentnywozek.DBAdaptersTests;

import android.content.Context;
import android.test.AndroidTestCase;

import android.test.mock.MockContext;

import com.example.mateusz.inteligentnywozek.database.ShopDBAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Mateusz on 2016-09-12.
 */
public class ShopDBAdapterTest extends AndroidTestCase {

    Context context = new MockContext();
    ShopDBAdapter db = new ShopDBAdapter(context);

    @Test
    public void getShopTest() {
        assertNotNull(db.getShop("1"));
    }

    @Test
    public void getShopsTest() {
        assertNotNull(db.getShops(""));
    }
}
