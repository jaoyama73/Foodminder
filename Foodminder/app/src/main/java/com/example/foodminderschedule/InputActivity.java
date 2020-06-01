package com.example.foodminderschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodminderschedule.R;

import java.util.ArrayList;
import java.util.Date;

public class InputActivity extends AppCompatActivity {
    private static EditText item;
    private static EditText date;
    private int dataSize = 0;
    private ArrayList<String>dataArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);

        // Initialize dataArray or receive dataArray from previous activity
        Intent intent = getIntent();
        dataArray.clear();
        dataArray = intent.getStringArrayListExtra("dataArray");
        //SharedPreferences data = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        // If received previous data -> get the number of data/items
        if (dataArray != null) {
            dataSize = dataArray.size();
        }
        // If no previous data  (opening this app for the first time) -> initialize the dataArray
        else {
            dataArray = new ArrayList<>();
            dataSize = 0;
        }


        Button add = findViewById(R.id.button_add);
        Button back =findViewById(R.id.button_return);
        item = (EditText)findViewById(R.id.editText_item);
        date = (EditText)findViewById(R.id.editText_date);







        // If 'Add' button is clicked
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass data as String array to Calendar
                // Format: item '~' date
                Intent toCalendarActivity = new Intent(getApplicationContext(),CalendarForm.class);
                toCalendarActivity.putStringArrayListExtra("dataArray",dataArray);
                dataArray.add(String.valueOf(item.getText())+"~"+String.valueOf(date.getText()));
                dataSize++;

                // Store in SharedPreference
                SharedPreferences dataInfo = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = dataInfo.edit();
                editor.putInt("dataSize",dataSize);
                for(int i = 1;i<=dataSize;i++){
                    editor.putString("data_"+Integer.toString(i),dataArray.get(i-1));
                }
                editor.apply();
                //startActivity(toCalendarActivity);


            }
        });


        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toMainActivity = new Intent(getApplicationContext(),MainActivity.class);
                toMainActivity.putStringArrayListExtra("dataArray",dataArray);
                startActivity(toMainActivity);
            }
        });





    }
}