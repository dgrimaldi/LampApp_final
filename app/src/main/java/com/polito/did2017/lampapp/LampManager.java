package com.polito.did2017.lampapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 06/12/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LampManager extends AppCompatActivity {
    private static final LampManager ourInstance = new LampManager();
    private List<Lamp> lista = new ArrayList();
    private int i=0;
    private Boolean b = false;
    private Context context;


    private LampManager() {
        lista= new ArrayList<>();
    }
    public static LampManager getInstance() {
        return ourInstance;
    }


    public Lamp addLamp() {
        Lamp lamp = new Lamp("Qui andra URL della lampada"+i);
        lamp.setName("LAMP_"+i);
        i++;
        return lamp;
    }
    public List<Lamp> getLamps(){
        return lista;
    };
    public Lamp getLamp(int i){
        return lista.get(i);
    };


    public void discover(Runnable done){
        lista.add(addLamp());
        //lista.add(addLamp());
        done.run();
    }
}
