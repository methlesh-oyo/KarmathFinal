package com.example.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThoughtActivity extends AppCompatActivity {
    private ImageView thoughtOfTheDay;
    private ImageView copyContent;
    private ImageView shareContent;
    private CardView cardView1;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static final String thoughtUrl = "https://drive.google.com/uc?export=download&id=1cJL_HlUbLBjCkjfpnpXKR8yR1xrjBIuM";
    public static final String imageUrl = "https://drive.google.com/uc?export=download&id=1HARUj6OEK6IYDzgajPExUoj3h8vLvVr4";

    public static final String THOUGHT_FILE="thought.txt";
    public static String text = null;
    public static String colorToday="#483D8B";
    public boolean isInternetWorking() {
        //Check if connected to internet, output accordingly
        final ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    assert nc != null;
                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thought);
        //Download todays's content
        if(isInternetWorking()) {
            // DoInBackground Thread
            startAsyncTaskThought();
        }
        else{
            Toast.makeText(ThoughtActivity.this, "Please connect to Internet and try again!", Toast.LENGTH_SHORT).show();
        }
        thoughtOfTheDay = findViewById(R.id.imageView7);
        copyContent = findViewById(R.id.copyContent);
        shareContent = findViewById(R.id.shareContent);
        cardView1 = findViewById(R.id.cardName);
        cardView1.setCardBackgroundColor(Color.parseColor(colorToday));
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        copyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = text;
                myClip = ClipData.newPlainText("text", content);
                myClipboard.setPrimaryClip(myClip);

                Context context = getApplicationContext();
                CharSequence msgText = "Content Copied!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, msgText, duration);
                toast.show();
            }
        });
        shareContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Karmath");
                    String shareMessage;//\nLet me recommend you this application:\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareMessage = text;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                Context context = getApplicationContext();
                CharSequence text = "Share Content!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    public void startAsyncTaskThought() {
        ThoughtActivity.ExampleAsyncTask task = new ThoughtActivity.ExampleAsyncTask(this);
        task.execute(imageUrl);
    }
    @SuppressLint("StaticFieldLeak")
    private class ExampleAsyncTask extends AsyncTask<String, String, Bitmap> {
        private WeakReference<ThoughtActivity> activityWeakReference;
        ExampleAsyncTask(ThoughtActivity activity) {
            activityWeakReference = new WeakReference<ThoughtActivity>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ThoughtActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
        @Override
        protected Bitmap doInBackground(String... String) {
            FileOutputStream fileOS = null;
            try{
                BufferedInputStream inputStream = new BufferedInputStream(new URL(thoughtUrl).openStream());
//                FileOutputStream fileOS = new FileOutputStream(FILE_NAME));
                fileOS = openFileOutput(THOUGHT_FILE, MODE_PRIVATE);//, MODE_PRIVATE);
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (IOException e) {
                // handles IO exceptions
            }finally {
                try {
                    fileOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                URL urlConnection = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

//            return "आज का विचार";
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            ThoughtActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
//            activity.progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Bitmap img) {
//            super.onPostExecute(img);
//            ThoughtActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing()) {
//                return;
//            }
//            thoughtOfTheDay.setImageBitmap(img);
//            String s="आज का विचार";
            try {
                super.onPostExecute(img);
                ThoughtActivity activity = activityWeakReference.get();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                thoughtOfTheDay.setImageBitmap(img);
                cardView1.setCardBackgroundColor(Color.parseColor(colorToday));
            }catch (Exception ex){
                Toast.makeText(ThoughtActivity.this, "Please connect to Internet and try again!", Toast.LENGTH_SHORT).show();
            }
            load();
            Toast.makeText(ThoughtActivity.this, "आज का विचार", Toast.LENGTH_SHORT).show();
        }
    }
    public void load() {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        //        String text;
        try {
            fis = openFileInput(THOUGHT_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((text = br.readLine()) != null) {
                sb.append(text);
                sb.append("\n");
            }
            text = sb.toString();
            String[] arr = text.split("_", 2);
            text = arr[0];
//
            arr = arr[1].split("\n", 2);
            colorToday = arr[0];
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, colorToday, duration);
//            toast.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}