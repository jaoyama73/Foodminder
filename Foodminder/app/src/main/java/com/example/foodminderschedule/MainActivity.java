package com.example.foodminderschedule;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;

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
        Log.e("tag","RECEIVED"+dataArray.toString());
        LinkedHashSet<String> dataArrayNoDuplicate = new LinkedHashSet<>(dataArray);
        dataArray = new ArrayList<>(dataArrayNoDuplicate);

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



        addByCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toInputActivity = new Intent(MainActivity.this, BarcodeActivity.class);
                toInputActivity.putStringArrayListExtra("dataArray",dataArray);
                startActivity(toInputActivity);
                scanBarcode();
            }
        });




    }



    private void scanBarcode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(BarcodeActivity.class);

        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();

    }

    public static String prodName = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        fetchData fetchData = new fetchData();
        String code= result.getContents();
        String link = "https://api.upcdatabase.org/product/"+code+"?apikey=17FF0064CF458FB08A2CDB89B7D075F6";
        fetchData.execute(link);
        if(prodName != null){
            if(prodName!=""){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(prodName);
                Log.i("TAG",prodName);
                builder.setTitle(("Scanning Result"));
                builder.setPositiveButton("Add Date", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent toTextActivity = new Intent(getApplicationContext(),TextRecognize.class);
                                toTextActivity.putStringArrayListExtra("dataArray",dataArray);
                                toTextActivity.putExtra("singleItem",prodName);
                                startActivity(toTextActivity);

                            }
                        }
                ).setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanBarcode();
                    }
                }).setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent toMainActivity = new Intent(getApplicationContext(),MainActivity.class);
                        toMainActivity.putStringArrayListExtra("dataArray",dataArray);
                        startActivity(toMainActivity);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No matching item in database");
                Log.i("TAG",prodName);
                builder.setTitle(("Scanning Result"));
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent toMainActivity = new Intent(getApplicationContext(),MainActivity.class);
                                toMainActivity.putStringArrayListExtra("dataArray",dataArray);
                                startActivity(toMainActivity);
                            }
                        }
                ).setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanBarcode();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }








}
