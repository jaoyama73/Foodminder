package com.example.foodminderschedule;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.*;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TextRecognize extends AppCompatActivity {

    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;

    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;
    private String year = "";
    private String month= "";
    private String day = "";
    private String space = "";
    private ArrayList<String>dataArray = new ArrayList<>();
    private int dataSize = 0;
    private String singleItem = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_recognize);
        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);
        startCameraSource();
       /*Intent intentscan = getIntent();
        scandataArray.clear();
        scandataArray = intentscan.getStringArrayListExtra("scandataArray");
        //SharedPreferences data = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
        // If received previous data -> get the number of data/items
        if (scandataArray != null) {
            scandataSize = scandataArray.size();
        }
        // If no previous data  (opening this app for the first time) -> initialize the dataArray
        else {
            scandataArray = new ArrayList<>();
            scandataSize = 0;
        }*/

        Intent intent = getIntent();
        dataArray.clear();
        dataArray = intent.getStringArrayListExtra("dataArray");
        singleItem = intent.getStringExtra("singleItem");
        singleItem = singleItem.replaceAll("[-+.^:,]"," ");
        // If received previous data -> get the number of data/items
        if (dataArray != null) {
            dataSize = dataArray.size();
        }
        // If no previous data  (opening this app for the first time) -> initialize the dataArray
        else {
            dataArray = new ArrayList<>();
            dataSize = 0;
        }
        Log.i("tag",String.valueOf(dataArray));
        Log.i("tag",singleItem);

        Button add = findViewById(R.id.buttoncorrect);
        Button backtoMain = findViewById(R.id.buttonBack);

        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(isNumeric(year)&& isNumeric(month) && isNumeric(day)){
                    Toast.makeText(getApplicationContext(),"Successfully added",Toast.LENGTH_SHORT).show();
                    String combined = singleItem+"-"+year.replaceAll("[^0-9]","")+"/"+month.replaceAll("[^0-9]","")+"/"+day.replaceAll("[^0-9]","");

                    dataArray.add(combined);
                    dataSize++;
                    Log.e("TAG",dataArray.toString());
                    SharedPreferences dataInfo = getSharedPreferences("dataInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = dataInfo.edit();
                    editor.putInt("dataSize", dataSize);
                    for (int i = 1; i <= dataSize; i++) {
                        editor.putString("data_" + Integer.toString(i), dataArray.get(i - 1));
                    }
                    editor.apply();
                    /*
                    Intent toMainActivity = new Intent(getApplicationContext(), Calendar.class);
                    toMainActivity.putStringArrayListExtra("dataArray",dataArray);
                    startActivity(toMainActivity);*/
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid date format, try again",Toast.LENGTH_SHORT).show();
                }

            }
        });


        backtoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainActivity = new Intent(getApplicationContext(),MainActivity.class);
                toMainActivity.putStringArrayListExtra("dataArray",dataArray);
                startActivity(toMainActivity);
            }
        });

    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {
        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {
            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();
            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(TextRecognize.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                String s=stringBuilder.toString();
                                if (s.contains("2020")|s.contains("2021"))
                                {
                                    try{
                                    // day=s.substring(s.indexOf("Jan")-3,s.indexOf("Jan"))
                                    if (s.contains("2020"))
                                    {year="2020";}
                                    if (s.contains("2021"))
                                    {year="2021";}
                                    if(s.contains("Jan")|s.contains("JAN"))
                                    { month="01";}
                                    if(s.contains("Feb")|s.contains("FEB"))
                                    { month="02";}
                                    if(s.contains("Mar")|s.contains("MAR"))
                                    { month="03";}
                                    if(s.contains("Apr")|s.contains("APR"))
                                    { month="04";}
                                    if(s.contains("May")|s.contains("MAY"))
                                    { month="05";}
                                    if(s.contains("Jun")|s.contains("JUN"))
                                    { month="06";}
                                    if(s.contains("Jul")|s.contains("JUL"))
                                    { month="07";}
                                    if(s.contains("Aug")|s.contains("AUG"))
                                    { month="08";}
                                    if(s.contains("Sep")|s.contains("SEP"))
                                    { month="09";}
                                    if(s.contains("Oct")|s.contains("OCT"))
                                    { month="10";}
                                    if(s.contains("Nov")|s.contains("NOV"))
                                    { month="11";}
                                    if(s.contains("Dec")|s.contains("DEC"))
                                    { month="12";}
                                    // day=s.substring(0,2);
                                    space = "1234";
                                    if (s.contains("2020"))
                                    {    space = s.substring(s.indexOf("2020")-1, s.indexOf("2020"));
                                        if (space.equals(" "))
                                        {day=s.substring(s.indexOf("2020")-7,s.indexOf("2020")-5);
                                            // mTextView.setText(day+month+year);
                                        }
                                        else
                                        { day=s.substring(s.indexOf("2020")-5,s.indexOf("2020")-3);
                                            //mTextView.setText(day+month+year);
                                        }
                                    }
                                    if (s.contains("2021"))
                                    {    space = s.substring(s.indexOf("2021")-1, s.indexOf("2021"));
                                        if (space.equals(" "))
                                        {day=s.substring(s.indexOf("2021")-7,s.indexOf("2021")-5);}
                                        else
                                        { day=s.substring(s.indexOf("2021")-5,s.indexOf("2021")-3);}}
                                    mTextView.setText(month+"/"+day+"/"+year);
                                    final String datescan=day+month+year;
                                }catch(StringIndexOutOfBoundsException e){
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                    }
                }
            });
        }
    }
}
