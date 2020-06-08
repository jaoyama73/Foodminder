package com.example.foodminderschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateList extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private ArrayList<String> dataArray = new ArrayList<>();
    private ArrayAdapter adapter;

    private ListView cardList;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_list);

        // Get previous dataArray if there is any
        dataArray.clear();
        SharedPreferences info = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        int index = info.getInt("dataSize", 0);
        for (int i = 1; i <= index; i++) {
            String eachData = info.getString("data_" + Integer.toString(i), "");
            dataArray.add(eachData);
        }



        Log.e("tag","RECEIVED"+dataArray.toString());

        // Sort the date
        Collections.sort(dataArray);


        // Add the arraylist to listView through adapter
        cardList = findViewById(R.id.date_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        cardList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        // NavBar config
        BottomNavigationView bottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(DateList.this,MainActivity.class);
                        intent1.putStringArrayListExtra("dataArray",dataArray);
                        startActivity(intent1);
                        break;
                    case R.id.nav_calendar:
                        Intent intent2 = new Intent(DateList.this,CalendarForm.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_list:

                        break;
                }
                return false;
            }
        });
    }




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("food_list",dataArray);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Sort the array (2)
        if(dataArray!=null) {
            Log.e("TAG","BEFORE SORT: "+dataArray.toString());
            Collections.sort(dataArray, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String date1 = o1.toString().split("-")[1];
                    String date2 = o2.toString().split("-")[1];
                    int year1 = Integer.parseInt(date1.split("/")[0]);
                    int month1 = Integer.parseInt(date1.split("/")[1]);
                    int day1 = Integer.parseInt(date1.split("/")[2]);
                    int year2 = Integer.parseInt(date2.split("/")[0]);
                    int month2 = Integer.parseInt(date2.split("/")[1]);
                    int day2 = Integer.parseInt(date2.split("/")[2]);
                    Date d1 = new GregorianCalendar(year1, month1 - 1, day1).getTime();
                    Date d2 = new GregorianCalendar(year2, month2 - 1, day2).getTime();
                    return d1.compareTo(d2);
                }
            });
            Log.e("TAG","AFTER SORT: "+dataArray.toString());
        }



    }

    @Override protected  void onPause()
    {
        super.onPause();
        // TBD
        sharedPreferences=getSharedPreferences("fooddate",MODE_PRIVATE);
        sharedPreferencesEditor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(dataArray);
        sharedPreferencesEditor.putString("allfooddate",json);
        sharedPreferencesEditor.apply();
    }


}
