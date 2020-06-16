package com.example.karmathfinal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.annotation.SuppressLint;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.content.Context;



import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SongActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleSongAdapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager;
    private TextView mContent;

    private ImageButton copyButton;
    private ImageButton shareButton;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static String text = null;

    public static final String FILE_NAME="Song.txt";
    public static final String TempSongFile="SongTemp.txt";
    //    public static final String sourceURL = "https://drive.google.com/uc?export=download&id=1ZLUs31rvs5mXpmBy8YaxIA0AYLfzXstH";
    public static final String sourceURL = "https://drive.google.com/uc?export=download&id=1NNferlDkfEJ9Q1zBgkQpFwSVzBO4Y5lb";
    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }
    public static boolean lineBreak(char c){
        return (c == '।' || c == ',' || c == '|' || c == '.' || c == '?');
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
//        if(isInternetWorking()) {
//            // DoInBackground Thread
//            startAsyncTaskSong();
//            Toast.makeText(SongActivity.this, "Internet found!", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(SongActivity.this, "Please connect to Internet and try again!", Toast.LENGTH_SHORT).show();
//        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Trying to load data from the web wish me luck...
        final ArrayList<Integer> oldSongID = new ArrayList<>();
//        final ArrayList<Integer> songID = new ArrayList<>();

//        final ArrayList<String> loadedFromWeb = new ArrayList<>();
        final ArrayList<String> loadedFromStorage = new ArrayList<>();

//        final ArrayList<String> categoryWeb = new ArrayList<>();
        final ArrayList<String> categoryLocal = new ArrayList<>();

        final ArrayList<Boolean> switchStates = new ArrayList<>();

        // Load local songs
        FileInputStream fis = null;
        String text;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            text=sb.toString();
            StringBuilder line;
            int n= text.length(), i = 0;
            while(i<n){
                char c=text.charAt(i);
                if(isDigit(c)){
                    line = new StringBuilder();
                    int j=i;
                    while (isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        j++;
                    }
                    while (!isDigit(text.charAt(j))){
                        line.append(text.charAt(j));
                        if(lineBreak(text.charAt(j)))
                            line.append("\n");
                        j++;
                        if(j==n)break;
                    }
                    String tmp=line.toString();
                    String [] arr = tmp.split("_", 4);
                    oldSongID.add(Integer.parseInt(arr[0]));
                    loadedFromStorage.add(arr[3]);
                    categoryLocal.add(arr[2]);

                    if(arr[1].equals("false")){
                        switchStates.add(false);
                    }
                    else{
                        switchStates.add(true);
                    }
                    i=j;
                }
            }
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

        // Fetch New File Here
        fis = null;
        try {
            fis = openFileInput(TempSongFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            text=sb.toString();
            StringBuilder line;
            int n= text.length(), i=0;
            while(i<n){
                char c=text.charAt(i);
                if(isDigit(c)){
                    line = new StringBuilder();
                    int j=i;
                    while (isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        j++;
                    }
                    while (!isDigit(text.charAt(j))){
                        line.append(text.charAt(j));
                        if(lineBreak(text.charAt(j)))
                            line.append("\n");
                        j++;
                        if(j==n)break;
                    }
                    String tmp=line.toString();
                    String [] arr = tmp.split("_", 4);
                    int num=Integer.parseInt(arr[0]);
//                    Toast.makeText(SongActivity.this, tmp, Toast.LENGTH_SHORT).show();
                    if(!oldSongID.contains(num)) {
                        oldSongID.add(num);
//                        songID.add(num);

//                        loadedFromWeb.add(arr[3]);
                        loadedFromStorage.add(arr[3]);
//                        categoryWeb.add(arr[2]);
                        categoryLocal.add(arr[2]);

                        switchStates.add(false);
                    }
                    i=j;
                }
            }
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

//        int N=switchStates.size(), ContentLength=oldSongID.size();
//        for(int i=0; i<ContentLength-N; i++){
//            switchStates.add(false);
//        }
//        String s = switchStates.size() + " " + loadedFromStorage.size();
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, s, duration);
//        toast.show();

        final ArrayList<ExampleSong> mExampleSongList = new ArrayList<>();
        int n=oldSongID.size();
        for(int i=0; i<n; i++) {
            switch (categoryLocal.get(i)) {
                case "Agyat":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_agyat, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Bhagwan":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_bhagwan, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Dard":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_dard, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Dosti":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_dosti, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Guru":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_guru, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Lagan":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_lagan, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Pyaar":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_prema, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Prerna":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_prerna, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
                case "Tyag":
                    mExampleSongList.add(new ExampleSong(R.drawable.ic_tyag, loadedFromStorage.get(i), switchStates.get(i)));
                    break;
            }
        }
//        mExampleSongList.add(new ExampleSong(R.drawable.ic_agyat, "Junk Data one", switchStates.get(0)));
//        mExampleSongList.add(new ExampleSong(R.drawable.ic_prema, "Junk Data two", switchStates.get(1)));
//        mExampleSongList.add(new ExampleSong(R.drawable.ic_guru, "Junk Data three", switchStates.get(2)));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mAdapter = new ExampleSongAdapter(mExampleSongList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mContent = findViewById(R.id.SongView);

        mAdapter.setOnItemClickListener(new ExampleSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
            @Override
            public void onCopyClick(int position) {
                String text = mExampleSongList.get(position).getContent();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Context context = getApplicationContext();
                CharSequence msgText = "Content Copied!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, msgText, duration);
                toast.show();
            }

            @Override
            public void onShareClick(int position) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Karmath");
                    String shareMessage;//\nLet me recommend you this application:\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareMessage = mExampleSongList.get(position).getContent() +"\n\n";
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

            @Override
            public void onAddFavoriteClick(int position) {
                if (!switchStates.get(position)){
                    Context context = getApplicationContext();
                    CharSequence textEnabled = "Added to Favorites!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, textEnabled, duration);
                    toast.show();
                    switchStates.set(position, true);
                }
                load();
            }

            @Override
            public void onRemoveFavoriteClick(int position) {
                Context context = getApplicationContext();
                CharSequence textDisabled = "Removed from Favorites!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, textDisabled, duration);
                toast.show();
                switchStates.set(position, false);
                load();
            }
            // Write the current state in the Boolean Text File
            void load() {
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    String text;

                    int n = loadedFromStorage.size();
                    for (int i = 0; i < n; i++) {
                        if (switchStates.get(i))
                            text = oldSongID.get(i) + "_true_" + categoryLocal.get(i) + "_" + loadedFromStorage.get(i);
                        else
                            text = oldSongID.get(i) + "_false_" + categoryLocal.get(i) + "_" + loadedFromStorage.get(i);
                        fos.write(text.getBytes());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}