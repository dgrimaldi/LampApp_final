package com.polito.did2017.lampapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;


public	class Lamp {
    private String name;
    private int rgb;
    private Bitmap image;;
    private int intensity;
    private Boolean state;
    public String	URL;
    public String sName;
    private int wing;

    //save the context recievied via constructor in a local variable
    SharedPreferences sh ;
    SharedPreferences.Editor editor;
    private String picture;


    public Lamp(String	URL, String name,Context ctx){
        sh=PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sh.edit();
        this.name=name;
        this.URL=URL;
        setName(name);
        setURL(URL);
    }
    public void setURL(String URL){
        this.URL=URL;
        editor.putString(getName()+":URL", URL);
        editor.apply();
        getURL();
    }
    public String getURL(){
        URL = sh.getString(getName()+":URL","");
        return URL;
    }
    public	void setColor(int rgb){
        this.rgb=rgb;
        editor.putInt(getName()+"_COLOR_DATA", rgb);
        editor.apply();
        getRgb();
    };
    public int getRgb(){
        rgb = sh.getInt(getName()+"_COLOR_DATA", 0);
        return rgb;
    }
    public void setName(String	sName){
        name=sName;
        this.sName=sName;
        editor.putString(sName+"_NAME_DATA", name);
        editor.apply();
        getName();
    };
    public String getName(){
        name = sh.getString(sName+"_NAME_DATA", null);
        return name;
    };

    public void setState(boolean state){
        this.state=state;
        editor.putBoolean(getName()+"_STATE_DATA", state);
        editor.apply();
        getState();
    }
    public Boolean getState() {
        state = sh.getBoolean(getName()+"_STATE_DATA",false);
        return state;


    }
    public void setIntensity(int intensity){
        this.intensity=intensity;
        editor.putInt(getName()+"_LUM_DATA", intensity);
        editor.apply();
        getIntensity();
    };
    public int getIntensity(){
        intensity = sh.getInt(getName()+"_LUM_DATA", 0);
        return intensity;
    }

    public String getPicture(){
        picture = sh.getString(getName()+"_IMG_DATA",null);
        return picture;
    }

    public void setPicture(String picture){
        this.picture=picture;
        editor.putString(getName()+"_IMG_DATA",picture);
        editor.apply();
        getPicture();
    }



    public void setWing(int wing) {
        this.wing = wing;
        editor.putInt(getName()+"_WING_DATA", wing);
        editor.apply();
        getWing();
    }

    public int getWing() {
        wing= sh.getInt(getName()+"_WING_DATA",0);
        return wing;
    }
}
