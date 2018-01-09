package com.polito.did2017.lampapp;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {


    public static Context contextOfApplication;
    SwipeRefreshLayout mSwipeRefreshLayout;
    BaseAdapter baseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final GridView gridView = findViewById(R.id.grid_3);

        mSwipeRefreshLayout = findViewById(R.id.swipe);
        contextOfApplication = getApplicationContext();


        gridView.setAdapter(null);


        // Esercitazione Sulla lista
        final LampManager lm = LampManager.getInstance();
        lm.setLamps();
        gridView.setAdapter(null);
        //getInitial(lm);

        baseAdapter = new BaseAdapter() {


            @Override
            public int getCount() {
                return lm.getLamps().size();
            }

            @Override
            public Object getItem(int i) {
                return lm.getLamp(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                //Lamp L = (Lamp) getItem(i);
                View v = null;
                if (view == null)
                    v = getLayoutInflater().inflate(R.layout.adapterlist, viewGroup, false);
                else
                    v = view;
                TextView tv = v.findViewById(R.id.textViewAD);
                final Switch s = v.findViewById(R.id.switchAD);
                ImageView iv = v.findViewById(R.id.imageViewAD);
                System.out.println(lm.getLamp(i).URL);
                //Imposta il testo
                tv.setText(lm.getLamp(i).getName());
                //setta lo swicht
                s.setChecked(lm.getLamp(i).getState());
                // imposta lo swict
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String URL = lm.getLamp(i).URL;
                        final boolean st = s.isChecked();
                        lm.getLamp(i).setState(st);
                        //char data = Boolean.toString(st).charAt(0);
                        new TcpClient(lm.getLamp(i), contextOfApplication).execute();

                    }
                });
                iv.setImageResource(R.drawable.lampada_1);

                ////////////////////
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, Lamp_1_Activity.class);
                        // Necessario in precedenza ora leggiamo i valori da Preferences
                        intent.putExtra("pos", i);
                        startActivity(intent);
                    }
                });
                return v;
            }
        };

        gridView.setAdapter(baseAdapter);
        System.out.println(baseAdapter);
        baseAdapter.notifyDataSetChanged();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myUpdateOperation();


            }
        });
    }

    private void myUpdateOperation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                baseAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aggiorna:
                LampManager lm = LampManager.getInstance();
                try {
                    lm.discover(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                baseAdapter.notifyDataSetChanged();
                //Restart();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //private void Restart() {
    //    finish();
    //    startActivity(getIntent());
    //}


    public static Context getContextOfApplication() {
        return contextOfApplication;
    }



    /*public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        Boolean st;
        String ip;

        public ConnectTask(Boolean st, String ip) {
            this.st = st;
            this.ip = ip;
        }

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(Boolean mState, String mSERVER_IP) {

                    mState=st;
                    mSERVER_IP=ip;
                    System.out.println(mState);
                    //this method calls the onProgressUpdate
                    publishProgress(String.valueOf(st),ip);
                }
            });
            mTcpClient.run();

            return null;
        }
    }*/

}