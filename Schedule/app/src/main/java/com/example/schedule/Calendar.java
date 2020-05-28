package com.example.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Calendar extends AppCompatActivity {
    ArrayList<Pair<String,Date>> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        Date today = new Date();
        java.util.Calendar nextYear = java.util.Calendar.getInstance();
        nextYear.add(java.util.Calendar.YEAR,1);

        CalendarPickerView datePicker = findViewById((R.id.calendar));
        datePicker.init(today,nextYear.getTime())
                .withSelectedDate(today);



        // Getting the intent passed from MainActivity
        final Intent intent = getIntent();

        // Getting the data from the intent
        final String itemReceived =  intent.getStringExtra("item");
        final String dateReceived =  intent.getStringExtra("date");
        ArrayList<Pair<String,Date>> saved = new ArrayList<Pair<String,Date>>();


        // Using the data from the intent
        String msg = dateReceived;
        String [] YMD = msg.split("/");
        String year = YMD[0];
        String month = YMD[1];
        String date = YMD[2];


        final Date input = new GregorianCalendar(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(date)).getTime();
        final List<Date> dates = new ArrayList<Date>();
        //dates.add(input);
        Log.i("tag","item: "+itemReceived+" date: "+String.valueOf(input));
        array = (ArrayList<Pair<String, Date>>) intent.getSerializableExtra("saved");
        if(array== null){
            array = new ArrayList<>();
            array.add(new Pair<String, Date>(itemReceived,input));
        }
        //saved.add(new Pair<String, Date>(itemReceived,input));

        for(int i = 0; i<array.size();i++){
            dates.add(array.get(i).second);
        }


        Log.i("tag",String.valueOf(array));
        //datePicker.highlightDates(array);

        datePicker.highlightDates(dates);
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
                String inputDate = DateFormat.getDateInstance(DateFormat.SHORT).format(input);
                if(dates.indexOf(date)!= -1){
                    int index = dates.indexOf(date);
                    // Toast.makeText(getApplicationContext(),"Item: "+itemReceived,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),array.get(index).first,Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),selectedDate,Toast.LENGTH_SHORT).show();
                }
                //Calendar sel = Calendar.getInstance();

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        Button re = findViewById(R.id.revert);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this,MainActivity.class);
                intent.putExtra("savedDate",array);
                intent.putExtra("testNum",5);

                startActivity(intent);
            }
        });

    }


}
