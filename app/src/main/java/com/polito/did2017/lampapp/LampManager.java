package com.polito.did2017.lampapp;

        import android.annotation.TargetApi;
        import android.content.Context;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by asus on 06/12/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LampManager extends AppCompatActivity {


    private static final LampManager ourInstance = new LampManager();
    private List<Lamp> lista = new ArrayList();


    Context applicationContext = MainActivity.getContextOfApplication();

    //private LampManager() {
    //    lista= new ArrayList<>();
    //}

    public static LampManager getInstance() {
        return ourInstance;
    }

    public void setLamps(){
        boolean b = true;
        for(int i=0;i<3;i++){
            Lamp l = new Lamp("LAMP_"+i);
            l.setName("LAMP_" + i);
            System.out.print("LAMP_"+i);
            for (int pos=0; pos < lista.size(); pos++){
                if(getLamp(pos).getName().equals("LAMP_"+i)){
                    b =false;
                    break;
                }else
                    b =true;
            }
            if(b)
                lista.add(l);
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


    public void discover(Runnable done) throws InterruptedException {
        ClientListen cl = new ClientListen();
        new Thread(cl).start();
        done.run();
        try {
            Thread.sleep(5000);
            Boolean b = true;
            System.out.print(getLamps());
            System.out.println(cl.text);
            for (int i=0; i < lista.size(); i++){
                if(cl.text.equals(lista.get(i).getName()))
                    b=false;
            }
            if (cl.text.contains("LAMP_") && b) {
                Lamp l = new Lamp(cl.URL.replace("/",""));
                l.setName(cl.text);
                this.lista.add(l);
                new TcpClient(l, applicationContext).execute();
            }else if(cl.text=="NULL") {
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d("ERROR","Nessuna lampada trovata");
        }



    }
}

