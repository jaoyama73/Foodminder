package com.example.foodminderschedule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PopUp extends AppCompatActivity {
    private ArrayList<String> itemReceived = new ArrayList<>();
    private ArrayAdapter adapter;
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        //Setup PopUp Window Size: 75% of width of screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        getWindow().setLayout((int)(width*0.75),(int)(width*0.75));

        // Receive item Array from Calendar and print as a ListView
        itemReceived.clear();
        itemReceived= getIntent().getStringArrayListExtra("item");
        Log.i("TAG",String.valueOf(itemReceived));
        itemList = findViewById(R.id.itemList);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,itemReceived);
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
