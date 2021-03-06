package com.example.mateusz.inteligentnywozek.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.mateusz.inteligentnywozek.database.ProductDBAdapter;
import com.example.mateusz.inteligentnywozek.adapter.ProductPreferenceAdapter;
import com.example.mateusz.inteligentnywozek.R;

/**
 * Created by Mateusz on 2015-11-30.
 */
public class ProductsPreferencesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 2015-12-09   Usunięcie floatingActionButtona z widoku preferencji
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddProduct);
        fab.setVisibility(View.GONE);

        productList = (ListView) findViewById(R.id.listsView);
        ProductDBAdapter db = new ProductDBAdapter(getBaseContext());
        ProductPreferenceAdapter adapter = new ProductPreferenceAdapter(this,db.getAllProductsSortOnlyByStar(""));
        productList.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_shop_list) {
            Intent intent = new Intent(ProductsPreferencesActivity.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_products) {
            finish();
            startActivity(getIntent());
        } else if (id == R.id.nav_shops) {}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
