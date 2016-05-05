package com.example.mateusz.inteligentnywozek.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.inteligentnywozek.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ShopsPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_preferences);

        final TextView textView = (TextView) findViewById(R.id.getText);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.3.2:8080/Serwer/map/products?shop=123&ids=id1;id3;XXX")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(ShopsPreferencesActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                //TODO Usunąć zwracane przez serwer "a" przed JSONem
                String responseData = response.body().string().substring(1);
                JSONObject json = null;
                try {
                    json = new JSONObject(responseData);
                    String result = json.getString("Result");
                    String shopId = json.getString("ShopId");
                    System.out.println("Result: " + result);
                    System.out.println("ShopId: " + shopId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
