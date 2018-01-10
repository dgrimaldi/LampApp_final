package com.polito.did2017.lampapp;

        import android.annotation.TargetApi;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Build;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

/**
 * Created by asus on 06/12/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LampManager extends AppCompatActivity {


    private static final LampManager ourInstance = new LampManager();
    private List<Lamp> lista = new ArrayList();
    Context applicationContext = MainActivity.getContextOfApplication();

    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    Map<String,?> allEntries = mPrefs.getAll();



    //private LampManager() {
    //    lista= new ArrayList<>();
    //}

    public static LampManager getInstance() {
        return ourInstance;
    }



    public void setLamps(){

        boolean b =true;
        for (Map.Entry<String,?> entry: allEntries.entrySet()) {
            String url = null;
            Log.d("map_values", entry.getKey() + ": " + entry.getValue().toString());
            if (entry.getKey().contains("URL")) {
                url = (String) entry.getValue().toString();
                int i = entry.getKey().indexOf(":",2);
                Log.d("map_values", entry.getKey() + i +": " + entry.getValue().toString());
                String name = entry.getKey().substring(0,i);
                System.out.println(name);
                Lamp l = new Lamp(url,name);
                for (int pos=0; pos < lista.size(); pos++){
                    if(getLamp(pos).getName().equals(name)){
                        b =false;
                        break;
                    }else
                        b =true;
                }
                if(b)
                    lista.add(l);
            }
        }
        //if(l.getName().equals(lista.get(i).getName())){
            //    lista.remove(l);
            //}else
            //    lista.add(l);
        
    }

    public List<Lamp> getLamps(){
        return lista;
    };
    public Lamp getLamp(int i){
        return lista.get(i);
    };
    public void removeLamp(int i){
        SharedPreferences.Editor editor = mPrefs.edit();
        for (Map.Entry<String,?> entry: allEntries.entrySet()){
            if(entry.getKey().contains(lista.get(i).getName()) ||
                    entry.getValue().toString().contains((lista.get(i).getName()))){
                editor.remove(entry.getKey());
                editor.apply();
            }
        }
        lista.remove(i);
    }


    public void discover(Runnable done) throws InterruptedException {
        ClientListen cl = new ClientListen();
        new Thread(cl).start();
        done.run();
        if(cl==null)
            lista.clear();
        try {
            Boolean b = true;
            System.out.print(getLamps());
            System.out.println(cl.text);
            for (int i=0; i < lista.size(); i++){
                if(cl.text.equals(lista.get(i).getName()))
                    b=false;
            }
            if (b) {
                    Lamp l = new Lamp( cl.URL.replace("/", ""), cl.text);
                    this.lista.add(l);
                    new TcpClient(l, applicationContext).execute();

            }else if(cl.text=="NULL") {
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("ERROR","Nessuna lampada trovata");
            Toast.makeText(applicationContext, "Lamp not found", Toast.LENGTH_SHORT).show();
        }



    }
}

