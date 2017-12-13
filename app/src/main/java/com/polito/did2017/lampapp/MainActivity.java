package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;


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
        final GridView list = findViewById(R.id.grid_3);

        mSwipeRefreshLayout = findViewById(R.id.swipe);
        contextOfApplication = getApplicationContext();





        // Esercitazione Sulla lista
        final LampManager lm = LampManager.getInstance();


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

                        View v = null;
                        if (view == null)
                            v = getLayoutInflater().inflate(R.layout.adapterlist, viewGroup, false);
                        else
                            v = view;
                        TextView tv = v.findViewById(R.id.textViewAD);
                        final Switch s = v.findViewById(R.id.switchAD);
                        ImageView iv = v.findViewById(R.id.imageViewAD);
                        //Imposta il testo
                        tv.setText(lm.getLamp(i).getName());
                        //setta lo swicht
                        s.setChecked(lm.getLamp(i).getState());
                        // imposta lo swict
                        s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean st = s.isChecked();
                                lm.getLamp(i).setState(st);
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
        list.setAdapter(baseAdapter);
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
            @Override public void run() {
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
                LampManager lm = LampManager.getInstance();;
                lm.discover(new Runnable() {
                                @Override
                                public void run() {
                                }
                });
                baseAdapter.notifyDataSetChanged();
                //Restart();
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

}