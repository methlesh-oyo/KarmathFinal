package com.example.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleContentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private String text;
    public static final String FILE_NAME = "Favorite.txt";

    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Boolean isEmpty=true;
        final ArrayList<ExampleContent> mExampleContentList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
                sb.append("\n");
            }
            text=sb.toString();

            StringBuilder line;
            int n=text.length();
            for(int i=0; i<n; i++){
                char c= text.charAt(i);
                // Check for starting of content
                if(isDigit(c)){
                    line = new StringBuilder();
                    line.append(c);
                    int j=i+1;
                    if(j==n)break;
                    while (!isDigit(text.charAt(j))){
                        line.append(text.charAt(j));
                        j++;
                        if(j==n)break;
                    }
                    String tmp=line.toString();
                    String [] arr=tmp.split("_", 3);
                    // Insert
                    if(arr.length>=3) {
                        switch (arr[1]) {
                            case "Agyat":
                                isEmpty=false;
                                ExampleContent temperoryContent = new ExampleContent(R.drawable.ic_agyat, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Bhagwan":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_bhagwan, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Dard":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_dard, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Dosti":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_dosti, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Guru":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_guru, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Lagan":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_lagan, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Prerna":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_prerna, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Pyaar":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_prema, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                            case "Tyag":
                                isEmpty=false;
                                temperoryContent = new ExampleContent(R.drawable.ic_tyag, arr[2], true);
                                if (!mExampleContentList.contains(temperoryContent))
                                    mExampleContentList.add(temperoryContent);
                                break;
                        }
                    }
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
        if(isEmpty){
            Context context = getApplicationContext();
            CharSequence text = "No Favorites Yet!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ExampleContentAdapter(mExampleContentList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        mAdapter.setOnItemClickListener(new ExampleContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
            @Override
            public void onCopyClick(int position) {
                text = mExampleContentList.get(position).getContent();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Context context = getApplicationContext();
                CharSequence text = "Content Copied!";
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
                    shareMessage = mExampleContentList.get(position).getContent() +"\n\n";
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
                Context context = getApplicationContext();
                CharSequence text = "Already present!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            @Override
            public void onRemoveFavoriteClick(int position) {
                Context context = getApplicationContext();
                CharSequence textDisabled = "Remove it from category.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, textDisabled, duration);
                toast.show();
            }
        });
    }
}