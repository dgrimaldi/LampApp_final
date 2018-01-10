package com.polito.did2017.lampapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import static android.widget.AdapterView.*;


public class MainActivity extends AppCompatActivity{


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
                v.setLongClickable(true);
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
                /*v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lm.removeLamp(i);
                        baseAdapter.notifyDataSetChanged();
                    }
                });*/
                gridView.setLongClickable(true);
                v.setOnLongClickListener(new GridView.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("ATTENTION!!");
                        alert.setMessage("Are you sure to delete "+lm.getLamp(i).getName()+"?");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int s) {
                                lm.removeLamp(i);
                                baseAdapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                        return true;
                    }
                });




                    /*public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                        //Do your tasks here


                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                MainActivity.this);
                        alert.setTitle("Alert!!");
                        alert.setMessage("Are you sure to delete record");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do your work here
                                dialog.dismiss();


                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        alert.show();

                        return true;
                    }
                });*/
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