package com.example.android.miwok;

/**
 * Created by lenovo on 2/11/2018.
 */

public class Word {
    private String mDefaultTranslation;
    private String mSpanishTranslation;
    private int mresourceId;
    private int maudioId;
    public Word(String mdefault,String mspanish,int id,int aid){
        this.mDefaultTranslation=mdefault;
        this.mSpanishTranslation=mspanish;
        this.mresourceId=id;
        this.maudioId=aid;
    }
    public Word(String mdefault,String mspanish,int aid){
        this.mDefaultTranslation=mdefault;
        this.mSpanishTranslation=mspanish;
        this.maudioId=aid;
    }
    public String getDefaultTranslation(){
        return this.mDefaultTranslation;
    }
    public String getSpanishTranslation(){
        return this.mSpanishTranslation;
    }
    public int getResourceId(){return this.mresourceId;}
    public int getAudioId(){return this.maudioId;}
}
