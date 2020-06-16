package com.example.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleContentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mContent;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static final String GLOBAL_FAVORITE_NAME = "Favorite.txt";
    public static final String FILE_NAME = "Dard.txt";
    public static final String TempDardFile="DardTemp.txt";


    private String text;
    private boolean Start=false;
    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }
    public static boolean lineBreak(char c){
        return (c == '।' || c == ',' || c == '|' || c == '.' || c == '?');
    }
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
        setContentView(R.layout.activity_dard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Trying to load data from the web wish me luck...
        final ArrayList<Integer> oldSongID = new ArrayList<>();
//        final ArrayList<Integer> songID = new ArrayList<>();

//        final ArrayList<String> loadedFromWeb = new ArrayList<>();
        final ArrayList<String> loadedFromStorage = new ArrayList<>();


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
            int n= text.length(), i=0;
            while (i<n){
                char c=text.charAt(i);
                if(isDigit(c)){
                    line = new StringBuilder();
                    int j=i;
                    while (isDigit(text.charAt(j))){
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
                    String [] arr = tmp.split("_", 3);
                    oldSongID.add(Integer.parseInt(arr[0]));
                    loadedFromStorage.add(arr[2]);

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
            fis = openFileInput(TempDardFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            text=sb.toString();
            StringBuilder line;
            int n= text.length(), i=0;
            while (i<n){
                char c=text.charAt(i);
                if(isDigit(c)){
                    line = new StringBuilder();
                    int j=i;
                    while (isDigit(text.charAt(j))){
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
                    String [] arr = tmp.split("_", 3);
                    int num=Integer.parseInt(arr[0]);
                    if(!oldSongID.contains(num)) {
                        oldSongID.add(num);
                        loadedFromStorage.add(arr[2]);

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

        final ArrayList<ExampleContent> mExampleContentList = new ArrayList<>();
        int n=loadedFromStorage.size();
        for(int i=0; i<n; i++){
            mExampleContentList.add(new ExampleContent(R.drawable.ic_dard, loadedFromStorage.get(i), switchStates.get(i)));
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ExampleContentAdapter(mExampleContentList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mContent = findViewById(R.id.textView11);

        mAdapter.setOnItemClickListener(new ExampleContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCopyClick(int position) {
                String text = mExampleContentList.get(position).getContent();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Context context = getApplicationContext();
                text = "Content Copied!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
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
                    shareMessage = mExampleContentList.get(position).getContent() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
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
                    FileOutputStream fos = null;
                    try {
                        String text = oldSongID.get(position) + "_Dard_" + loadedFromStorage.get(position) + "\n\n";
                        fos = openFileOutput(GLOBAL_FAVORITE_NAME, MODE_APPEND);
                        fos.write(text.getBytes());

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
                StringBuilder sb = new StringBuilder();
                FileInputStream fis = null;

                // exclude the current content
                try {
                    fis = openFileInput(GLOBAL_FAVORITE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
//                    StringBuilder sb = new StringBuilder();
                    String text;
                    while ((text = br.readLine()) != null) {
                        String[] arr = text.split("_", 3);
                        if(arr.length>=3){
                            if(arr[1].equals("Dard") && Integer.parseInt(arr[0])==oldSongID.get(position)){
                                continue;
                            }
                            sb.append(text);
                            sb.append("\n");
                        }
                    }
                }catch (FileNotFoundException e) {
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
                // Update it to the text file
                String text;
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(GLOBAL_FAVORITE_NAME, MODE_PRIVATE);
                    text=sb.toString();
                    fos.write(text.getBytes());
                    //                    mEditText.getText().clear();
                    //                    Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    //                            Toast.LENGTH_LONG).show();
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
                            text = oldSongID.get(i) + "_true_" + loadedFromStorage.get(i);
                        else
                            text = oldSongID.get(i) + "_false_" + loadedFromStorage.get(i);
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
//    public void startAsyncTaskDard() {
//        DardActivity.ExampleAsyncTask task = new DardActivity.ExampleAsyncTask(this);
//        task.execute(sourceURL);
//    }
//    @SuppressLint("StaticFieldLeak")
//    class ExampleAsyncTask extends AsyncTask<String, String, String> {
//        private WeakReference<DardActivity> activityWeakReference;
//        ExampleAsyncTask(DardActivity activity) {
//            activityWeakReference = new WeakReference<DardActivity>(activity);
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            DardActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing()) {
//                return;
//            }
//        }
//        @Override
//        protected String doInBackground(String... String) {
//            FileOutputStream fileOS = null;
//            try{
//                BufferedInputStream inputStream = new BufferedInputStream(new URL(sourceURL).openStream());
////                FileOutputStream fileOS = new FileOutputStream(FILE_NAME));
//                fileOS = openFileOutput(TempDardFile, MODE_PRIVATE);//, MODE_PRIVATE);
//                byte data[] = new byte[1024];
//                int byteContent;
//                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
//                    fileOS.write(data, 0, byteContent);
//                }
//            } catch (IOException e) {
//                // handles IO exceptions
//            }finally {
//                try {
//                    fileOS.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return "Finished!";
//        }
//        @Override
//        protected void onProgressUpdate(String... values) {
//            try {
//                super.onProgressUpdate(values);
//                DardActivity activity = activityWeakReference.get();
//                if (activity == null || activity.isFinishing()) {
//                    return;
//                }
//            }catch (Exception ex){
//                Toast.makeText(DardActivity.this, "Please connect to Internet and try again!", Toast.LENGTH_SHORT).show();
//            }
////            activity.progressBar.setProgress(values[0]);
//        }
//        @Override
//        protected void onPostExecute(String s) {
////            super.onPostExecute(s);
//            try {
//                super.onPostExecute(s);
//                DardActivity activity = activityWeakReference.get();
//                if (activity == null || activity.isFinishing()) {
//                    return;
//                }
//            }catch (Exception ex){
//                Toast.makeText(DardActivity.this, "Please connect to Internet and try again!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}