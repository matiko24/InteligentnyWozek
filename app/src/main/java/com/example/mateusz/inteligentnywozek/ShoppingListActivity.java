package com.example.mateusz.inteligentnywozek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        final Long listId = getIntent().getExtras().getLong("ListId");
        final String listName = getIntent().getExtras().getString("ListName");
        setTitle(listName);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, ProductsActivity.class);
                intent.putExtra("ListId", listId);
                intent.putExtra("ListName", listName);
                startActivity(intent);
            }
        });

        PurchaseDBAdapter db = new PurchaseDBAdapter(getBaseContext());
        ListView productsListView = (ListView) findViewById(R.id.productListView);
        PurchaseAdapter adapter = new PurchaseAdapter(this, db.getAllPurchases(listId));
        productsListView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
