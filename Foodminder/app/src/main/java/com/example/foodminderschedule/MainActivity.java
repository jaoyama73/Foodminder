package com.example.foodminderschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String>dataArray = new ArrayList<>();
    private int dataSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);


        // Receive intent from other activities (for update dataArray)
        Intent intent = getIntent();
        dataArray.clear();
        dataArray = intent.getStringArrayListExtra("dataArray");
        // If received previous data -> get the number of data/items
        if (dataArray != null) {
            dataSize = dataArray.size();
        }
        // If no previous data  (opening this app for the first time) -> initialize the dataArray
        else {
            dataArray = new ArrayList<>();
            dataSize = 0;
        }
        Log.i("tag",String.valueOf(dataArray));

        // NavBar Config
        BottomNavigationView bottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:

                        break;
                    case R.id.nav_calendar:
                        Intent intent2 = new Intent(MainActivity.this,CalendarForm.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_list:
                        Intent intent3 = new Intent(MainActivity.this,DateList.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });



        ImageView addByText = findViewById(R.id.input_text);
        ImageView addByCamera = findViewById(R.id.input_camera);

        // If addByText is clicked, move to InputActivity
        addByText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toInputActivity = new Intent(MainActivity.this, InputActivity.class);
                toInputActivity.putStringArrayListExtra("dataArray",dataArray);
                startActivity(toInputActivity);
            }
        });





    }





}
