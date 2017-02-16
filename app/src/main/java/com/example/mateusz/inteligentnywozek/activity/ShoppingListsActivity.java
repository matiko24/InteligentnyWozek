package com.example.mateusz.inteligentnywozek.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.mateusz.inteligentnywozek.adapter.PurchaseAdapter;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;
import com.example.mateusz.inteligentnywozek.R;

public class ShoppingListsActivity extends AppCompatActivity {

    ListView productsList;
    PurchaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        final Long listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
        final String listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));
        setTitle(listName);
        FloatingActionButton fabAddProduct = (FloatingActionButton) findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListsActivity.this, ProductsActivity.class);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_id), listId);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_name), listName);
                startActivity(intent);
            }
        });

        final PurchaseDBAdapter db = new PurchaseDBAdapter(getBaseContext());
        productsList = (ListView) findViewById(R.id.productListView);
        adapter = new PurchaseAdapter(this, db.getAllPurchases(listId));
        productsList.setAdapter(adapter);

        FloatingActionButton fabChooseShop = (FloatingActionButton) findViewById(R.id.fabChooseShop);
        fabChooseShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingListsActivity.this, ShopsActivity.class);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_id), listId);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_name), listName);
                String productsIds = db.getAllPurchasesString(listId);
                System.out.println("Products ids " + productsIds);
                intent.putExtra(getBaseContext().getString(R.string.extra_products_ids), productsIds);
                startActivity(intent);
            }
        });
        // TODO: 2015-12-09 Znikanie przycisku po usunieciu ostatniego produktu 
        if (adapter.getCount() == 0)
            fabChooseShop.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
