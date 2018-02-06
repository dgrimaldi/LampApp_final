package com.polito.did2017.lampapp;

        import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 06/12/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LampManager {


    private static LampManager ourInstance = null;
    private List<Lamp> lista = Collections.synchronizedList(new ArrayList());
    private Context ctx;
    SharedPreferences mPrefs;
    Map<String, ?> allEntries;
    SharedPreferences.Editor editor;

    private LampManager(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        allEntries = mPrefs.getAll();
        editor = mPrefs.edit();
    }


    public static synchronized LampManager getInstance(Context ctx) {
        if (ourInstance == null) ourInstance = new LampManager(ctx);
        return ourInstance;
    }

    public void setLamps() {
        boolean b = true;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String url = null;
            Log.d("map_values", entry.getKey() + ": " + entry.getValue().toString());
            if (entry.getKey().contains("URL")) {
                url = (String) entry.getValue().toString();
                int i = entry.getKey().indexOf(":", 2);
                Log.d("map_values", entry.getKey() + i + ": " + entry.getValue().toString());
                String name = entry.getKey().substring(0, i);
                System.out.println(name);
                Lamp l = new Lamp(url, name, ctx);
                synchronized (lista) {
                    for (int pos = 0; pos < lista.size(); pos++) {
                        if (getLamp(pos).getName().equals(name)) {
                            b = false;
                            break;
                        } else
                            b = true;
                    }
                    if (b)
                        lista.add(l);
                }
            }
        }
    }

    public List<Lamp> getLamps() {
        return lista;
    }

    ;

    public Lamp getLamp(int i) {
        return lista.get(i);
    }

    ;

    public void removeLamp(String name) {
        //Log.d("REMOVELAMP", lista.get(i).getName());
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue().toString().contains(name) || entry.getKey().contains(name)) {
                editor.remove(entry.getKey());
            }
        }
        int i;
        synchronized (lista) {
            for (i = 0; i < lista.size(); i++) {
                if (name.equals(lista.get(i).getName()))
                    break;
            }
        }
        lista.remove(i);
        editor.commit();
    }

    public boolean addLamp(Boolean s, String URL, int lum, int col, int win, String name, String img) throws InterruptedException {
        Log.i("UDP", "Messagio ricevuto!");
        boolean b = true;
        try {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println(lista.get(i).getName() + ".equals" + name);
                if (lista.get(i).getName().equals(name)) {
                    b = false;
                    if (!lista.get(i).getURL().equals(URL)) {
                        lista.get(i).setURL(URL);
                    }
                    break;
                } else
                    b = true;
            }
        } catch (Exception e) {
            Log.d("ERR", e.getMessage());
        }
        if (b) {
            System.out.println("Thread.sleep(5000)");
            Lamp l = new Lamp(URL, name, ctx);
            l.setState(s);
            l.setColor(col);
            l.setIntensity(lum);
            l.setWing(win);
            l.setPicture(img);
            new TcpClient(l, ctx).execute();
            lista.add(l);
        }
        //Ho dovuto inserire c>='0' && c<='9' && name.contains("LAMP_")
        // perché alle volte arrivavano caratteri unicode
        // che incasinavano la lista, il nome
        // delle lampade deve quindi essere nel formato
        // "LAMP_c" con c numero naturale

        return b;
    };
}


