package com.example.mateusz.inteligentnywozek.activity;

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

import com.example.mateusz.inteligentnywozek.database.ProductDBAdapter;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;
import com.example.mateusz.inteligentnywozek.R;
import com.example.mateusz.inteligentnywozek.adapter.ShopAdapter;
import com.example.mateusz.inteligentnywozek.database.ShopDBAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        final String listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));
        final String productsIds = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_products_ids));

        shopsList = (ListView) findViewById(R.id.productList);
        final ShopDBAdapter db = new ShopDBAdapter(this);
        adapter = new ShopAdapter(this, db.getShops(""));
        shopsList.setAdapter(adapter);
        shopsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                long shopId = cursor.getLong(0);
                String shopName = cursor.getString(1);
                db.incrementNumberOfShopingInShop(shopName);

                PurchaseDBAdapter db = new PurchaseDBAdapter(getBaseContext());
                cursor = db.getAllPurchases(listId);
                if (cursor != null)
                    cursor.moveToFirst();
                ProductDBAdapter productDB = new ProductDBAdapter(getBaseContext());
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < cursor.getCount(); i++) {
                    try {
                        Date lastPurchaseDate = dateFormat.parse(productDB.getProductLastPurchaseDate(cursor.getLong(1)));
                        Date nextPurchaseDate = new Date(currentDate.getTime() + (currentDate.getTime() - lastPurchaseDate.getTime()));
                        productDB.updateProduct(cursor.getInt(1), currentDate, nextPurchaseDate);
                        cursor.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(ShopsActivity.this, MapActivity.class);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_id), listId);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_name), listName);
                intent.putExtra(getBaseContext().getString(R.string.extra_products_ids), productsIds);
                intent.putExtra(getBaseContext().getString(R.string.extra_shop_id), shopId);
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
