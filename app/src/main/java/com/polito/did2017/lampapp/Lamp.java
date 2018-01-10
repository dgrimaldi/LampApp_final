package com.polito.did2017.lampapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


public	class Lamp extends AppCompatActivity {
    private String name;
    private int rgb;
    private Bitmap image;;
    private int intensity;
    private Boolean state;
    public String	URL;

    //save the context recievied via constructor in a local variable
    Context applicationContext = MainActivity.getContextOfApplication();


    public Lamp( String	URL, String name){
        this.name=name;
        this.URL=URL;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sh.edit();
        setName(name);
        editor.putString(getName()+":URL", URL);
        editor.apply();
    }

    public String getURL(){
        this.URL=URL;
        return URL;
    }

    public	void setColor(int rgb){
        this.rgb=rgb;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor =sh.edit();
        editor.putInt(getName()+"_COLOR_DATA", rgb);
        editor.apply();
        getRgb();
    };
    public int getRgb(){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        rgb = mPrefs.getInt(getName()+"_COLOR_DATA", 0);
        return rgb;
    }
    public void setName(String	name){
        this.name=name;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Otteniamo il corrispondente Editor
        SharedPreferences.Editor editor = sh.edit();
        // Salviamo nelle preferences
        editor.putString("NAME", name);
        // Applichiamo l'editor
        editor.apply();
        //modifichiamo il valore dello switch
        getName();
    };
    public String getName(){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        name = mPrefs.getString("NAME", null);
        return name;
    };

    public void setState(boolean state){
        this.state=state;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Otteniamo il corrispondente Editor
        SharedPreferences.Editor editor = sh.edit();
        // Salviamo nelle preferences
        editor.putBoolean(getName()+"_STATE_DATA", state);
        // Applichiamo l'editor
        editor.apply();
        //modifichiamo il valore dello switch
        getState();
    }
    public Boolean getState() {
        // Leggiamo le preferenze salvate
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        // Leggiamo l'info legata alla memoria
        state = mPrefs.getBoolean(getName()+"_STATE_DATA",false);
        //cambiare colore
        //ChangeColor1(selectedState);
        return state;


    }

    public void setIntensity(int intensity){
        this.intensity=intensity;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sh.edit();
        editor.putInt(getName()+"_LUM_DATA", intensity);
        editor.apply();
        getIntensity();
    };

    public int getIntensity(){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        intensity = mPrefs.getInt(getName()+"_LUM_DATA", 0);
        return intensity;
    }

    public Bitmap getPicture(){
        return null;
    };




}
