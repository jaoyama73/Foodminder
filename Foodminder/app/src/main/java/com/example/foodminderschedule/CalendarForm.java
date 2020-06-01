package com.example.foodminderschedule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
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
    private static ArrayList<ArrayList<String>> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_form);

        dataArray.clear();
        BottomNavigationView bottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(CalendarForm.this,MainActivity.class);
                        intent1.putStringArrayListExtra("dataArray",dataArray);
                        startActivity(intent1);
                        break;
                    case R.id.nav_calendar:

                        break;
                    case R.id.nav_list:
                        Intent intent3 = new Intent(CalendarForm.this,DateList.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }



    // Get array from SharedPreference during onResume
    @Override
    protected void onResume() {
        super.onResume();

        // Clear and then Modify the element [item,date] in dataArray accordingly
        dataArray.clear();
        SharedPreferences info = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        int index = info.getInt("dataSize", 0);
        for (int i = 1; i <= index; i++) {
            String eachData = info.getString("data_" + Integer.toString(i), "");
            dataArray.add(eachData);
        }

        //Set today and set the available selection range for 1 year in future
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final CalendarPickerView datePicker = findViewById((R.id.calendar));
        datePicker.init(today, nextYear.getTime())
                .withSelectedDate(today);

        // Process the dataArray received from SHaredPreference
        for (String i : dataArray) {
            // Split data element into item and date
            String itemReceived = i.split("-")[0];
            String dateReceived = i.split("-")[1];

            // Split date information into Year, Month, Date
            String[] YMD = dateReceived.split("/");
            String year = YMD[0];
            String month = YMD[1];
            String date = YMD[2];

            // Convert Year, Month, Date from String to Date format  (will be used for later comparison)
            Date input = new GregorianCalendar(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(date)).getTime();

            // Add information to dateArray and itemArray
            // If a date doesn't have any expiring food yet -> add date to dateArray, add item to newly added corresponding date
            if (!dates.contains(input)) {
                dates.add(input);
                ArrayList<String> addNew = new ArrayList<>();
                addNew.add(itemReceived);
                item.add(addNew);
            }
            // If a date already has expiring food -> add item to existing corresponding date
            else {
                int existedDateIndex = dates.indexOf(input);
                // Can only add the same item once to each date
                if (!item.get(existedDateIndex).contains(itemReceived))
                    item.get(existedDateIndex).add(itemReceived);
            }
        }

        // The dates with expiring food are highlighted
        datePicker.highlightDates(dates);

        // If a particular date is clicked
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                // If selected date has expiring food or highlighted/in date Array
                if (dates.indexOf(date) != -1) {
                    int index = dates.indexOf(date);
                    // Pass the item information and show Pop-Up window
                    // Directing to PopUp.java ...
                    Intent toPopUpActivity = new Intent(CalendarForm.this, PopUp.class);
                    toPopUpActivity.putStringArrayListExtra("item", item.get(index));
                    startActivity(toPopUpActivity);
                }
                // Otherwise, show nothing ... or Maybe a Toast
                else {
                    //Toast.makeText(getApplicationContext(),selectedDate,Toast.LENGTH_SHORT).show();
                }
            }

            // TBD
            @Override
            public void onDateUnselected(Date date) {
            }
        });
    }
    // If 'Return' button is pressed
    // Returning back to input page while passing the current info

}