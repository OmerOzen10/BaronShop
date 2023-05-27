package com.example.e_commercial_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent intent = new Intent(MainActivity.this,HomePage.class);
//                startActivity(intent);
//                finish();
//
//            }
//        },500);

        Timer timer = new Timer();

        try {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                    startActivity(intent);
                    finish();
                }
            };
            timer.schedule(timerTask,1000);

        }catch (Exception e){
            Log.d(TAG , "onCreate: error" + e.getMessage());
        }

    }
}