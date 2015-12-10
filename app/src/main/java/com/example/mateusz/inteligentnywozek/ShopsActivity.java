package com.example.mateusz.inteligentnywozek;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Date;

public class ShopsActivity extends AppCompatActivity {

    ListView shopsList;
    EditText productName;
    ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        final Long listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
        String listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));

        shopsList = (ListView) findViewById(R.id.productList);
        final ShopDBAdapter db = new ShopDBAdapter(this);
        adapter = new ShopAdapter(this, db.getShops(""));
        shopsList.setAdapter(adapter);
        shopsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                String s = cursor.getString(1);
                db.updateShop(s);

                PurchaseDBAdapter db = new PurchaseDBAdapter(getBaseContext());
                cursor = db.getAllPurchases(listId);
                if(cursor != null)
                    cursor.moveToFirst();
                ProductsDBAdapter productDB = new ProductsDBAdapter(getBaseContext());
                for(int i=0;i<cursor.getCount();i++){
                    productDB.updateProduct(cursor.getInt(1),new Date());
                    cursor.moveToNext();
                }

                Intent intent = new Intent(ShopsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        productName = (EditText) findViewById(R.id.productToAddName);
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    adapter = new ShopAdapter(getBaseContext(), db.getShops(s.toString()));
                    shopsList.setAdapter(adapter);
                } else {
                    adapter = new ShopAdapter(getBaseContext(), db.getShops(""));
                    shopsList.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
