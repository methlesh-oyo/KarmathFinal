package com.example.karmathfinal;

public class ExampleSong {
    private int mImageResource;
    private String mContent;
    private boolean mState;

    public  ExampleSong(int imageResource, String content, boolean state){
        mImageResource=imageResource;
        mContent=content;
        mState=state;
    }

    public  int getImageResource(){
        return  mImageResource;
    }

    public String getContent() {
        return mContent;
    }

    public boolean getState() {
        return mState;
    }
}
