package com.methleshkoshle.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//import com.methleshkoshle.karmathfinal.R;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_NAME = "Favorite.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                // the live saver
                finish();
            }
        }, 2500);
    }
    // trying hard to save data
}