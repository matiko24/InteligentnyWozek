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

import com.example.mateusz.inteligentnywozek.adapter.ProductAdapter;
import com.example.mateusz.inteligentnywozek.database.ProductDBAdapter;
import com.example.mateusz.inteligentnywozek.general.Purchase;
import com.example.mateusz.inteligentnywozek.database.PurchaseDBAdapter;
import com.example.mateusz.inteligentnywozek.R;


public class ProductsActivity extends AppCompatActivity {

    EditText productName;
    ListView productList;
    private ProductAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        final ProductDBAdapter db = new ProductDBAdapter(getBaseContext());
        final PurchaseDBAdapter purchaseDB = new PurchaseDBAdapter(getBaseContext());
        productList = (ListView) findViewById(R.id.productList);

        productName = (EditText) findViewById(R.id.productToAddName);
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    adapter = new ProductAdapter(getBaseContext(),db.getAllProducts(s.toString()));
                    productList.setAdapter(adapter);
                } else {
                    adapter = new ProductAdapter(getBaseContext(),db.getAllProducts(""));
                    productList.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adapter = new ProductAdapter(getBaseContext(),db.getAllProducts(""));
        productList.setAdapter(adapter);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
                Cursor cursor = adapter.getCursor();
                Long productId = cursor.getLong(0);
                Purchase purchase = new Purchase(listId,productId);
                purchaseDB.addPurchase(purchase);

                Intent intent = new Intent(ProductsActivity.this,ShoppingListsActivity.class);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_id),listId);
                intent.putExtra(getBaseContext().getString(R.string.extra_list_name), getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name)));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ShoppingListsActivity.class);
        intent.putExtra(getBaseContext().getString(R.string.extra_list_id),getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id)));
        intent.putExtra(getBaseContext().getString(R.string.extra_list_name), getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name)));
        startActivity(intent);
    }

}
