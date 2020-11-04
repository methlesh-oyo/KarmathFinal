package com.methleshkoshle.karmathfinal;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileIoHelper {

    private String GLOBAL_FAVORITE_NAME = "Favorite.txt";

    private FileInputStream fis = null;

    private ArrayList<Integer> oldSongID = new ArrayList<>();

    private ArrayList<String> loadedFromStorage = new ArrayList<>();

    private ArrayList<Boolean> switchStates = new ArrayList<>();

    Context context;

    String label, FILE_NAME, TempFileName;

    void init(Context currentContext, String currentID, String filename, String tempFileName){
        label=currentID;
        context=currentContext;
        FILE_NAME=filename;
        TempFileName=tempFileName;
    }

    // Load local songs
    void loadLocalContent() {
        String text;
        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            text = sb.toString();
            StringBuilder line;
            int n = text.length(), i = 0;
            while (i < n) {
                char c = text.charAt(i);
                if (Conditionals.isDigit(c)) {
                    line = new StringBuilder();
                    int j = i;
                    while (Conditionals.isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        j++;
                    }
                    while (!Conditionals.isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        if (Conditionals.lineBreak(text.charAt(j)))
                            line.append("\n");
                        j++;
                        if (j == n) break;
                    }
                    String tmp = line.toString();
                    String[] arr = tmp.split("_", 3);
                    oldSongID.add(Integer.parseInt(arr[0]));
                    loadedFromStorage.add(arr[2]);

                    if (arr[1].equals("false")) {
                        switchStates.add(false);
                    } else {
                        switchStates.add(true);
                    }
                    i = j;
                }
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
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

    // Fetch New File Here
    void fetchNewFile() {
        String text;
        fis = null;
        try {
            fis = context.openFileInput(TempFileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            text = sb.toString();
            StringBuilder line;
            int n = text.length(), i = 0;
            while (i < n) {
                char c = text.charAt(i);
                if (Conditionals.isDigit(c)) {
                    line = new StringBuilder();
                    int j = i;
                    while (Conditionals.isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        j++;
                    }
                    while (!Conditionals.isDigit(text.charAt(j))) {
                        line.append(text.charAt(j));
                        if (Conditionals.lineBreak(text.charAt(j)))
                            line.append("\n");
                        j++;
                        if (j == n) break;
                    }
                    String tmp = line.toString();
                    String[] arr = tmp.split("_", 3);
                    int num = Integer.parseInt(arr[0]);
                    if (!oldSongID.contains(num)) {
                        oldSongID.add(num);
                        loadedFromStorage.add(arr[2]);

                        switchStates.add(false);
                    }
                    i = j;
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
    }
    void handleAddClickEvents(int position){
        if (!switchStates.get(position)){
            FileOutputStream fos = null;
            try {
                String text = oldSongID.get(position) + "_" + label + "_" + loadedFromStorage.get(position) + "\n\n";
                fos = context.openFileOutput(GLOBAL_FAVORITE_NAME, context.MODE_APPEND);
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
            CharSequence textEnabled = "Added to Favorites!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, textEnabled, duration);
            toast.show();
            switchStates.set(position, true);
        }
        load();
    }
    void handleRemoveClickEvents(int position){
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;

        // exclude the current content
        try {
            fis = context.openFileInput(GLOBAL_FAVORITE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
//                    StringBuilder sb = new StringBuilder();
            String text;
            boolean toBeRemoved=false;
            while ((text = br.readLine()) != null) {
                String[] arr = text.split("_", 3);
                if(text.length()>=1){
                    if(Conditionals.isDigit(text.charAt(0))){
                        if (arr.length >= 3) {
                            if (arr[1].equals(label) && Integer.parseInt(arr[0]) == oldSongID.get(position)) {
                                toBeRemoved=true;
                            }
                            else{
                                toBeRemoved=false;
                            }
                        }
                    }
                }
                if(toBeRemoved)continue;
                sb.append(text);
                sb.append("\n");
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
            fos = context.openFileOutput(GLOBAL_FAVORITE_NAME, context.MODE_PRIVATE);
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
            fos = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
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
    ArrayList<String> getLoadedFromStorage(){
        return loadedFromStorage;
    }
    ArrayList<Boolean> getSwitchStates(){
        return switchStates;
    }
}
