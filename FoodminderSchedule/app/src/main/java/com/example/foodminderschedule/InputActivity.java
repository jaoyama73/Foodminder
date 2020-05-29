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
        //saved= (ArrayList<Pair<String, Date>>) getIntent().getSerializableExtra("savedDate");
        int x = getIntent().getIntExtra("testNum", 0);
        //Log.i("tag",String.valueOf(saved)+String.valueOf(x));

        Intent intent = getIntent();

        dataArray = intent.getStringArrayListExtra("dataArray");
        if (dataArray != null) {
            dataSize = dataArray.size();
        } else {
            dataArray = new ArrayList<>();
            dataSize = 0;
        }

        Button add = findViewById(R.id.button_add);
        item = (EditText)findViewById(R.id.editText_item);
        date = (EditText)findViewById(R.id.editText_date);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCalendarActivity = new Intent(getApplicationContext(),CalendarForm.class);
                toCalendarActivity.putStringArrayListExtra("dataArray",dataArray);
                dataArray.add(String.valueOf(item.getText())+"~"+String.valueOf(date.getText()));
                dataSize++;

                SharedPreferences dataInfo = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = dataInfo.edit();
                editor.putInt("dataSize",dataSize);
                for(int i = 1;i<=dataSize;i++){
                    editor.putString("data_"+Integer.toString(i),dataArray.get(i-1));
                }
                editor.apply();
                startActivity(toCalendarActivity);
            }
        });



    }
    /*

    //The following method will run when 'Login' button is pressed
    public void onClick(View view) {
        item = (EditText)findViewById(R.id.editText_item);
        date = (EditText)findViewById(R.id.editText_date);

        //Pair<String,String> input= new Pair<String,String>(String.valueOf(item.getText()),String.valueOf(date.getText()));
        Intent intent = new Intent(this, CalendarForm.class);
        intent.putExtra("item",String.valueOf(item.getText()));
        intent.putExtra("date",String.valueOf(date.getText()));
        intent.putExtra("saved",saved);
        startActivity(intent);


    }


*/
}