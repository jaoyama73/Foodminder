package com.example.foodminderschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodminderschedule.loginapp.R;

import java.util.ArrayList;
import java.util.Date;

public class LoginPage extends AppCompatActivity {
    private static EditText item;
    private static EditText date;
    private static TextView attempts;
    private static Button add_btn;
    ArrayList<Pair<String, Date>> saved = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        saved= (ArrayList<Pair<String, Date>>) getIntent().getSerializableExtra("savedDate");
        int x = getIntent().getIntExtra("testNum",0);
        Log.i("tag",String.valueOf(saved)+String.valueOf(x));
    }

    //The following method will run when 'Login' button is pressed
    public void onClick(View view) {
        item = (EditText)findViewById(R.id.editText_item);
        date = (EditText)findViewById(R.id.editText_date);
        attempts = (TextView)findViewById(R.id.textView_numattempts);
        add_btn = (Button)findViewById(R.id.button_login);

        //Pair<String,String> input= new Pair<String,String>(String.valueOf(item.getText()),String.valueOf(date.getText()));
        Intent intent = new Intent(this, UserPage.class);
        intent.putExtra("item",String.valueOf(item.getText()));
        intent.putExtra("date",String.valueOf(date.getText()));
        intent.putExtra("saved",saved);
        startActivity(intent);


    }



}