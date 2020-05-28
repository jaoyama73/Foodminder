package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static EditText item;
    private static EditText date;
    ArrayList<Pair<String, Date>> saved = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saved= (ArrayList<Pair<String, Date>>) getIntent().getSerializableExtra("savedDate");
        int x = getIntent().getIntExtra("testNum",0);
        Log.i("tag",String.valueOf(saved)+String.valueOf(x));
    }

    //The following method will run when 'Login' button is pressed
    public void onClick(View view) {
        item = (EditText)findViewById(R.id.editText_item);
        date = (EditText)findViewById(R.id.editText_date);


        //Pair<String,String> input= new Pair<String,String>(String.valueOf(item.getText()),String.valueOf(date.getText()));
        Intent intent = new Intent(this,Calendar.class);
        intent.putExtra("item",String.valueOf(item.getText()));
        intent.putExtra("date",String.valueOf(date.getText()));
        intent.putExtra("saved",saved);
        startActivity(intent);


    }
}
