package com.polito.did2017.lampapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity
implements UDPService.OnHeadlineSelectedListener{


    SwipeRefreshLayout mSwipeRefreshLayout;
    //BaseAdapter baseSwipeAdapter;
    LampAdapter lampAdapter;
    UDPService myService=null;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        final GridView gridView = findViewById(R.id.grid_3);




        mSwipeRefreshLayout = findViewById(R.id.swipe);


        //gridView.setAdapter(null);

        // Esercitazione Sulla lista
        final LampManager lm = LampManager.getInstance();
        //final LampManager lm = new LampManager();
        //lm.setLamps();
        //Per provare con due lampade

        try {
            lm.addLamp(true, "255.255.255.255", 100, 000000, 25, "LAMP_EXAMPLE_0",null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            lm.addLamp(false, "255.255.255.0", 250, 000000, 50, "LAMP_EXAMPLE_1","https://firebasestorage.googleapis.com/v0/b/lampapp-6688e.appspot.com/o/Flower_Lamp.png?alt=media&token=50681145-8f46-4cfe-8ea0-e2bebb78785a");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //
        lampAdapter = new LampAdapter(getApplicationContext());

        gridView.setAdapter(lampAdapter);
        System.out.println(lampAdapter);
        lampAdapter.notifyDataSetChanged();
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

    }


    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, UDPService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
        //startService(intent);
    }


    private ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            UDPService.MyLocalBinder binder = (UDPService.MyLocalBinder) service;
            myService = binder.getService();
            myService.startListenForUDPBroadcast(MainActivity.this);
        }
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            myService = null;
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aggiorna:
                Intent intent = new Intent(this, UDPService.class);
                bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
                lampAdapter.notifyDataSetChanged();
                try {
                    Thread.sleep(1000);
                    stopService(intent);
                } catch (InterruptedException e) {
                    Log.d("ERROR", e.getMessage());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onArticleSelected(int position) {
        //notification.setSmallIcon(R.id.image_preview);
        //notification.setTicker("New_LAMP");
        //notification.setWhen(System.currentTimeMillis());
        //notification.setContentTitle("N";

        Intent intent = new Intent(MainActivity.this, Lamp_1_Activity.class);
        intent.putExtra("pos",position);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.aggiorna)
                .setContentTitle("New Lamp")
                .setContentText("You have a new lamp!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).getNotification();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder);
        lampAdapter.notifyDataSetChanged();
    }
}