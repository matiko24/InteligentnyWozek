package com.example.mateusz.inteligentnywozek.DBAdaptersTests;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.example.mateusz.inteligentnywozek.database.ProductDBAdapter;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;

import org.junit.Test;

/**
 * Created by Mateusz on 2016-09-12.
 */
public class PurchaseDBAdapterTest extends AndroidTestCase {

    Context context = new MockContext();
    PurchaseDBAdapter db = new PurchaseDBAdapter(context);

    @Test
    public void getAllPurchases(){
        assertNotNull(db.getAllPurchases(1L));
    }
}
