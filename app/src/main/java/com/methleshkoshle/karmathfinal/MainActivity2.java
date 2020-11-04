package com.methleshkoshle.karmathfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.methleshkoshle.karmathfinal.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Context nowContext;

    public static String TempFile=null;
    public static String nowUrl=null;
    public static String nowActivity=null;
    public static final String TempAgyatFile="AgyatTemp.txt";
    public static final String TempBhagwanFile="BhagwanTemp.txt";
    public static final String TempDardFile="DardTemp.txt";
    public static final String TempDostiFile="DostiTemp.txt";
    public static final String TempGuruFile="GuruTemp.txt";
    public static final String TempLaganFile="LaganTemp.txt";
    public static final String TempPrernaFile="PrernaTemp.txt";
    public static final String TempPyaarFile="PyaarTemp.txt";
    public static final String TempTyagFile="TyagTemp.txt";

    public static final String TempSongFile="SongTemp.txt";

    public static final String sourceSongURL = "https://drive.google.com/uc?export=download&id=1NNferlDkfEJ9Q1zBgkQpFwSVzBO4Y5lb";
    public static final String sourceAgyatURL = "https://drive.google.com/uc?export=download&id=1ZLUs31rvs5mXpmBy8YaxIA0AYLfzXstH";
    public static final String sourceBhagwanURL = "https://drive.google.com/uc?export=download&id=1PTktNCb6RNB93oStv-vxSDVCkfCNw86v";
    public static final String sourceDardURL = "https://drive.google.com/uc?export=download&id=1balIvuyQmBmxifybTpDm5fmK0tq46jGb";
    public static final String sourceDostiURL = "https://drive.google.com/uc?export=download&id=19_cYlNdXjjbMnK0WPvMDssYAToHrmdue";
    public static final String sourceGuruURL = "https://drive.google.com/uc?export=download&id=1BOgE_livzQX85QteF5NgowIJ8cBxVy3_";
    public static final String sourceLaganURL = "https://drive.google.com/uc?export=download&id=1ctnu8bqHzgM0RSRnPWpmtPAqxA9ZA1Si";
    public static final String sourcePrernaURL = "https://drive.google.com/uc?export=download&id=1k5Ot5xhQT6IbJ3blPZAbONAtnIq48Z_W";
    public static final String sourcePyaarURL = "https://drive.google.com/uc?export=download&id=1O47PhC-ZjFD3gTimwan3Sa0siCrf_PX-";
    public static final String sourceTyagURL = "https://drive.google.com/uc?export=download&id=1K5bW-KmuSg_WzUUW6vz7O0Kj1zW-J-c7";

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item ) {
        switch (item.getItemId()){
            case R.id.nav_song:
                if(isInternetWorking()) {
                    // DoInBackground Thread
                    startAsyncTask(sourceSongURL, TempSongFile, "गीत");
                }
                else{
                    Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                }
                Intent intent1 = new Intent(MainActivity2.this, SongActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_favorites:
                Intent intent2 = new Intent(MainActivity2.this, FavoriteActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_share:
//                Intent intent3 = new Intent(Intent.ACTION_VIEW);
//                intent3.setData(Uri.parse("https://drive.google.com/uc?export=download&id=1lNpZUJX0ZpcklbKLexrUJYiZNVuoOaMV"));
//                startActivity(intent3);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Karmath");
                    String shareMessage= "\nLet me recommend you this beautiful qoute App *Karmath*:\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.methleshkoshle.myapplication" + "\n\n";
//                    https://drive.google.com/file/d/1bwR-bK3bIvAoSKkD54j_tBgOzN80tspb/view?usp=sharing
//                    shareMessage = shareMessage + "https://drive.google.com/uc?export=download&id=1bwR-bK3bIvAoSKkD54j_tBgOzN80tspb" +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
//            case R.id.nav_oneliner:
//                Intent intent4 = new Intent(MainActivity2.this, TwolinerActivity.class);
//                startActivity(intent4);
//                break;
            case R.id.nav_abt_me:
                Intent intent5 = new Intent(MainActivity2.this, AboutmeActivity.class);
                startActivity(intent5);
                break;
            case R.id.nav_write_a_review:
                Intent intent6 = new Intent(MainActivity2.this, WriteActivity.class);
                startActivity(intent6);
                break;
            case R.id.nav_thought:
                Intent intent7 = new Intent(MainActivity2.this, ThoughtActivity.class);
                startActivity(intent7);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Side navigation Activity
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        final ArrayList<ExampleItem> mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem(R.drawable.ic_agyat, "अज्ञात", "अज्ञात कवितायेँ"));
        mExampleList.add(new ExampleItem(R.drawable.ic_bhagwan, "भगवान", "परमेश्वर एक कल्पना"));
        mExampleList.add(new ExampleItem(R.drawable.ic_dard, "दर्द", "जीवन में बहुत दर्द है"));
        mExampleList.add(new ExampleItem(R.drawable.ic_dosti, "दोस्ती", "दोस्त जीवन सरल कर देते है"));
        mExampleList.add(new ExampleItem(R.drawable.ic_guru, "गुरु", "जीवन के मार्गदर्शक"));
        mExampleList.add(new ExampleItem(R.drawable.ic_lagan, "लगन", "विद्यार्थी की ततपरता"));
        mExampleList.add(new ExampleItem(R.drawable.ic_prema, "प्रेम", "इस संसार में प्रेम से बढ़कर कुछ नहीं"));
        mExampleList.add(new ExampleItem(R.drawable.ic_prerna, "प्रेरणा", "निराशा से मुक्त रहिए"));
        mExampleList.add(new ExampleItem(R.drawable.ic_tyag, "त्याग", "कुछ पाने के लिए कुछ खोना पड़ता है"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            Intent intent1 = new Intent(MainActivity2.this, AgyatActivity.class);
            Intent intent2 = new Intent(MainActivity2.this, BhagwanActivity.class);
            Intent intent3 = new Intent(MainActivity2.this, DardActivity.class);
            Intent intent4 = new Intent(MainActivity2.this, DostiActivity.class);
            Intent intent5 = new Intent(MainActivity2.this, GuruActivity.class);
            Intent intent6 = new Intent(MainActivity2.this, LaganActivity.class);
            Intent intent7 = new Intent(MainActivity2.this, PyaarActivity.class);
            Intent intent8 = new Intent(MainActivity2.this, PrernaActivity.class);
            Intent intent9 = new Intent(MainActivity2.this, TyagActivity.class);
            @Override
            public void onItemClick(int position) {
                if(position==0) { // AgyatActivity
                    if(isInternetWorking()) {
                        startAsyncTask(sourceAgyatURL, TempAgyatFile, "अज्ञात");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    // Life Saver
                    startActivity(intent1);
                }
                else if(position==1){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceBhagwanURL, TempBhagwanFile, "भगवान");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    // Life Saver
                    startActivity(intent2);
                }
                else if(position==2){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceDardURL, TempDardFile, "दर्द");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    // Life Saver
                    startActivity(intent3);
                }
                else if(position==3){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceDostiURL, TempDostiFile, "दोस्ती");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    // Life Saver
                    startActivity(intent4);
                }
                else if(position==4){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceGuruURL, TempGuruFile, "गुरु");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    // Life Saver
                    startActivity(intent5);
                }
                else if(position==5){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceLaganURL, TempLaganFile, "लगन");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent6);
                }
                else if(position==6){
                    if(isInternetWorking()) {
                        startAsyncTask(sourcePyaarURL, TempPyaarFile, "प्रेम");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent7);
                }
                else if(position==7){
                    if(isInternetWorking()) {
                        startAsyncTask(sourcePrernaURL, TempPrernaFile, "प्रेरणा");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent8);
                }
                else if(position==8){
                    if(isInternetWorking()) {
                        startAsyncTask(sourceTyagURL, TempTyagFile, "त्याग");
                    }
                    else{
                        Toast.makeText(MainActivity2.this, "Connect to Internet to load new content!", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent9);
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    public void startAsyncTask(String urlToExecute, String currentTempFile, String currentActivity){
        TempFile=currentTempFile;
        nowActivity=currentActivity;
        nowUrl=urlToExecute;
        ExampleAsyncTask task = new ExampleAsyncTask(MainActivity2.this);
        task.execute(urlToExecute);
    }
    @SuppressLint("StaticFieldLeak")
    class ExampleAsyncTask extends AsyncTask<String, String, String> {
        private WeakReference<MainActivity2> activityWeakReference;
        ExampleAsyncTask(MainActivity2 activity) {
            activityWeakReference = new WeakReference<MainActivity2>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
        @Override
        /* access modifiers changed from: protected */
        public String doInBackground(String... Strings) {
            FileOutputStream fileOS = null;
            try {
                BufferedInputStream inputStream = new BufferedInputStream(new URL(Strings[0]).openStream());
                fileOS = MainActivity2.this.openFileOutput(TempFile, 0);
                byte[] data = new byte[1024];
                while (true) {
                    int read = inputStream.read(data, 0, 1024);
                    int byteContent = read;
                    if (read != -1) {
                        fileOS.write(data, 0, byteContent);
                    } else {
                        break;
                    }
                }
            } catch (IOException e2) {
            } finally {
                try {
                    fileOS.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            MainActivity2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(MainActivity2.this, nowActivity, Toast.LENGTH_SHORT).show();
        }
    }
}
/* AsyncTask ka purana code
//                BufferedInputStream inputStream = new BufferedInputStream(new URL(Strings[0]).openStream());
// old                FileOutputStream fileOS = new FileOutputStream(FILE_NAME));
                Toast.makeText(MainActivity2.this, Strings[0], Toast.LENGTH_SHORT).show();
                        fileOS = openFileOutput(TempFile, MODE_PRIVATE);//, MODE_PRIVATE);
//                byte[] data = new byte[5*1024];
//                int byteContent;
//                while ((byteContent = inputStream.read(data, 0, 5*1024)) != -1) {
//                    fileOS.write(data, 0, byteContent);
//                }
//                String line;
//                URL url = new URL(Strings[0]);
//                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//                StringBuilder sb = new StringBuilder();
//                while ((line = in.readLine()) != null) {
//                    // do something with line
//                    sb.append(line);
//                    sb.append("\n");
//
//                }
//                line=sb.toString();
//                fileOS.write(line.getBytes());
//                in.close();
 */