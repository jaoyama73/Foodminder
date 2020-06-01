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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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

        /*
        if (savedInstanceState !=null)
        {
            //saveed from rotation
            cardArray=savedInstanceState.getStringArrayList("food_list");
        }
        else
        {
            //nosaved date
            sharedPreferences=getSharedPreferences("fooddate",MODE_PRIVATE);
            String json=sharedPreferences.getString("allfooddate", "");
            if (json.length()!=0){
                //convert from json to card array
                Gson gson=new Gson();
                cardArray=gson.fromJson(json,new TypeToken<ArrayList<String>>(){}.getType());
            }
            else{
                cardArray = new ArrayList<>();
            }

        }*/

        dataArray.clear();
        SharedPreferences info = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        int index = info.getInt("dataSize", 0);
        for (int i = 1; i <= index; i++) {
            String eachData = info.getString("data_" + Integer.toString(i), "");
            dataArray.add(eachData);
        }

        Log.i("tag",String.valueOf(dataArray));
        Collections.sort(dataArray);

        cardList = findViewById(R.id.date_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        cardList.setAdapter(adapter);
        adapter.notifyDataSetChanged();





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
        /*
        sharedPreferences=getSharedPreferences("fooddate",MODE_PRIVATE);
        String cardNumber=sharedPreferences.getString("foodinfo", "");
        if (cardNumber.length()!=0)
        {
            dataArray.add(cardNumber);
            adapter.notifyDataSetChanged();
            Toast.makeText(this,"date was saved", Toast.LENGTH_SHORT).show();
        };
        sharedPreferencesEditor=sharedPreferences.edit();
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.commit();*/
        Collections.sort(dataArray);




    }

    @Override protected  void onPause()
    {
        super.onPause();
        sharedPreferences=getSharedPreferences("fooddate",MODE_PRIVATE);
        sharedPreferencesEditor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(dataArray);
        sharedPreferencesEditor.putString("allfooddate",json);
        sharedPreferencesEditor.apply();
    }


}
