package com.example.foodminderschedule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodminderschedule.R;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarForm extends AppCompatActivity {
    private static ArrayList<String> dataArray = new ArrayList<>();
    private static ArrayList<Date> dates = new ArrayList<>();
    private static ArrayList<String> item = new ArrayList<>();

    //ArrayList<Pair<String,Date>> array = new ArrayList<>();
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_form);

    }

    public void showPopUp(View view){
        TextView textclose;
        mDialog.setContentView(R.layout.pop_up);
        textclose = (TextView)mDialog.findViewById(R.id.close);
        textclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    //get array from Sharedpreference onresume
    @Override
    protected void onResume(){
        super.onResume();
        //Modify the data in dataArray accordingly
        dataArray.clear();
        SharedPreferences info = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        int index = info.getInt("dataSize",0);
        for (int i =1;i<=index;i++){
            String eachData = info.getString("data_"+Integer.toString(i),"");
            dataArray.add(eachData);
        }


        //Set today and set the range for 1 year in future
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR,1);


        final CalendarPickerView datePicker = findViewById((R.id.calendar));
        datePicker.init(today,nextYear.getTime())
                .withSelectedDate(today);

        for (String i:dataArray) {
            String itemReceived = i.split("~")[0];
            String dateReceived = i.split("~")[1];
            String[] YMD = dateReceived.split("/");
            String year = YMD[0];
            String month = YMD[1];
            String date = YMD[2];
            //convert string YMD to proper time format
            Date input = new GregorianCalendar(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(date)).getTime();
            //add to date array
            //final List<Date> dates = new ArrayList<Date>();
            dates.add(input);
            Log.i("tag","item: "+itemReceived+" date: "+String.valueOf(input));
            item.add(itemReceived);
        }


        //Log.i("tag",String.valueOf(array));

        datePicker.highlightDates(dates);
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
                //String inputDate = DateFormat.getDateInstance(DateFormat.SHORT).format(input);
                if(dates.indexOf(date)!= -1){
                    int index = dates.indexOf(date);
                    //showPopUp(datePicker);
                    // Toast.makeText(getApplicationContext(),"Item: "+itemReceived,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Item: "+ item.get(index),Toast.LENGTH_SHORT).show();

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
                Intent toInputActivity = new Intent(getApplicationContext(),InputActivity.class);
                toInputActivity.putStringArrayListExtra("dataArray",dataArray);

                startActivity(toInputActivity);
            }
        });

    }


}