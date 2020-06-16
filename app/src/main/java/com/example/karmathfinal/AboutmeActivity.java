package com.example.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class AboutmeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

    }

//    View imgview1 = findViewById(R.id.facebookView);
//    View imgview2 = findViewById(R.id.instagramView);
//    View imgview3 = findViewById(R.id.linkedinView);
//    View imgview4 = findViewById(R.id.githubView);
//
//    public void facebookWebOpen() {
//
//    }
//    public void instagramWebOpen() {
//
//    }
//    public void linkedinWebOpen() {
//
//    }
//    public void githubWebOpen() {
//
//    }

    public void showLinkedin(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/methlesh-koshle-208a64145/"));
        startActivity(browserIntent);
    }

    public void showGithub(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MethleshKoshle"));
        startActivity(browserIntent);
    }

    public void showInstagram(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/methlesh_koshle/"));
        startActivity(browserIntent);
    }

    public void showFacebook(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/methlesh.koshle"));
        startActivity(browserIntent);
    }
}