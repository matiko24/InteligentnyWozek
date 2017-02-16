package com.example.mateusz.inteligentnywozek.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mateusz.inteligentnywozek.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mateusz on 2017-02-15.
 */

public class MapActivity extends AppCompatActivity {
    TouchImageView drawingImageView;
    ListView productList;
    ArrayAdapter<String> adapter;
    Canvas canvas;
    Handler handler;
    int screenSizeWidth;
    int screenSizeHight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final int columnNumber = 4;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSizeHight = size.y;
        screenSizeWidth = size.x;

        final Long shopId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_shop_id));
        final Long listId = getIntent().getExtras().getLong(getBaseContext().getString(R.string.extra_list_id));
        final String listName = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_list_name));
        final String productsIds = getIntent().getExtras().getString(getBaseContext().getString(R.string.extra_products_ids));

        drawingImageView = (TouchImageView) this.findViewById(R.id.mapImageView);
        productList = (ListView) this.findViewById(R.id.productListMap);
        String[] array = {"Start"};
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(array));
        adapter = new ArrayAdapter<String>(this, R.layout.row_product_on_map, lst);
        productList.setAdapter(adapter);

        drawShelfs(columnNumber, 2);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String results = (String) msg.obj;
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(results);
                    Paint paint = new Paint();
                    paint.setStrokeWidth(5);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.GREEN);
                    Path p = new Path();
                    System.out.println(canvas.getHeight());
                    p.moveTo(0, (8*screenSizeHight)/9);

                    for(int i=0; i<jsonArray.length();i++){
                        addListViewItem(jsonArray.getJSONObject(i).getString("id"));
                        int x = Integer.valueOf(jsonArray.getJSONObject(i).getString("columnNumber"));
                        int y = Integer.valueOf(jsonArray.getJSONObject(i).getString("rowNumber"));
                        p.lineTo((x+1) * (screenSizeWidth) / (2 * columnNumber), screenSizeHight - y*((screenSizeHight)/9) );
                        if(jsonArray.getJSONObject(i).getBoolean("containsRequiredProduct"))
                            p.addCircle((x+1) * (screenSizeWidth) / (2 * columnNumber), screenSizeHight - y*((screenSizeHight)/9), 10, Path.Direction.CW);
                    }
                    canvas.drawPath(p, paint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        askServer(productsIds,shopId);
    }

    public void addListViewItem(String productName) {
        adapter.add("Product_"+ productName);
        adapter.notifyDataSetChanged();
        ViewGroup.LayoutParams params = productList.getLayoutParams();
        params.height = adapter.getCount() * 40;
        productList.setLayoutParams(params);
        productList.requestLayout();
    }

    public void drawShelfs(int x, int y) {

        Bitmap bitmap = Bitmap.createBitmap(screenSizeWidth, screenSizeHight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);

        Paint rectanglePaint = new Paint();
        rectanglePaint.setARGB(255, 0, 0, 0);
        rectanglePaint.setStrokeWidth(20 - x);
        rectanglePaint.setColor(Color.BLACK);
        rectanglePaint.setStyle(Paint.Style.STROKE);

        int numberToScreenDivideX = (screenSizeWidth) / (2 * x);
        int numberToScreenDivideY = (screenSizeHight) / 9;

        for (int i = 0; i < x; i++) {
            Rect rectangle = new Rect(numberToScreenDivideX  * (i + 1) + numberToScreenDivideX * i, numberToScreenDivideY, numberToScreenDivideX * (i + 1) + numberToScreenDivideX * (i + 1), numberToScreenDivideY*4);
            canvas.drawRect(rectangle, rectanglePaint);
            rectangle = new Rect(numberToScreenDivideX  * (i + 1) + numberToScreenDivideX * i, numberToScreenDivideY * 5, numberToScreenDivideX  * (i + 1) + numberToScreenDivideX * (i + 1), numberToScreenDivideY*8);
            canvas.drawRect(rectangle, rectanglePaint);
        }
    }

    public String askServer(String productsIds, Long shopId) {
        final String[] returnString = new String[1];
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.3.2:8080/Serwer/map/getShortestPath?shop=" + shopId + "&" + productsIds)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("ERROR BŁAD W ZAPYTANIU DO SERWERA");
                /*String responseData = "[{\"columnNumber\":0,\"id\":302,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":303,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":304,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":305,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":318,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true}]";
                returnString[0] = responseData;
                Message message = new Message();
                message.obj = responseData;
                handler.sendMessage(message);*/
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                String responseData = response.body().string();
                //responseData = "[{\"columnNumber\":0,\"id\":302,\"shopId\":1,\"rowNumber\":1,\"containsRequiredProduct\":true},{\"columnNumber\":0,\"id\":303,\"shopId\":1,\"rowNumber\":2,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":304,\"shopId\":1,\"rowNumber\":3,\"containsRequiredProduct\":false},{\"columnNumber\":0,\"id\":305,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":318,\"shopId\":1,\"rowNumber\":4,\"containsRequiredProduct\":false},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true},{\"columnNumber\":1,\"id\":319,\"shopId\":1,\"rowNumber\":5,\"containsRequiredProduct\":true}]";
                returnString[0] = responseData;
                Message message = new Message();
                message.obj = responseData;
                handler.sendMessage(message);
            }
        });
        return returnString[0];
    }
}

