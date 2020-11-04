package com.methleshkoshle.karmathfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.methleshkoshle.karmathfinal.R;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GuruActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ExampleContentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mContent;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static final String FILE_NAME = "Guru.txt";
    public static final String TempGuruFile="GuruTemp.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();

        final FileIoHelper currentFileIoHelper = new FileIoHelper();

        currentFileIoHelper.init(context, "Guru", FILE_NAME, TempGuruFile);

        currentFileIoHelper.loadLocalContent();
        currentFileIoHelper.fetchNewFile();

        ArrayList<String> loadedFromStorage = currentFileIoHelper.getLoadedFromStorage();

        ArrayList<Boolean> switchStates = currentFileIoHelper.getSwitchStates();

        final ArrayList<ExampleContent> mExampleContentList = new ArrayList<>();

        int n=loadedFromStorage.size();

        for(int i=0; i<n; i++){
            mExampleContentList.add(new ExampleContent(R.drawable.ic_guru, loadedFromStorage.get(i), switchStates.get(i)));
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
                currentFileIoHelper.handleAddClickEvents(position);
            }

            @Override
            public void onRemoveFavoriteClick(int position) {
                currentFileIoHelper.handleRemoveClickEvents(position);
            }
        });
    }
}