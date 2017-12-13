package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


public class Lamp_1_Activity extends AppCompatActivity {

    public static Context contextOfApplication;


    public static final String COLOR = "color";
    public static final String COLOR_DATA = "colorData";
    private Context context;

    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lamp_1_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ScrollView sV = findViewById(R.id.scrollView);
        final ConstraintLayout cL = findViewById(R.id.Constraint_layout);
        Button b1 = findViewById(R.id.Everyday_button);
        Button b2 = findViewById(R.id.Focus_button);
        Button b3 = findViewById(R.id.Relax_button);
        final Switch aSwitch = findViewById(R.id.switchState);
        final SeekBar bar = findViewById(R.id.seekBar);


        final Intent i = getIntent();
        //Animation

        //
        final int pos = i.getExtras().getInt("pos");
        // Necessario in precedenza ora leggiamo i valori da Preferences
        //System.out.println(state);
        //S1.setChecked(state);

        final LampManager lm = LampManager.getInstance();

        getInitial(lm, i);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state = aSwitch.isChecked();
                lm.getLamp(pos).setState(state);
                int color = lm.getLamp(pos).getRgb();
                if(aSwitch.isChecked()){
                    cL.setBackgroundColor(color);
                    sV.setBackgroundColor(color);
                }else {
                    cL.setBackgroundColor(getResources().getColor(R.color.Normal));
                    sV.setBackgroundColor(getResources().getColor(R.color.Normal));
                }
            }
        });
        //Luminosità
        final int lum = lm.getLamp(pos).getIntensity();
        bar.setProgress(lum);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int intensity, boolean b) {
                lm.getLamp(pos).setIntensity(intensity);
                System.out.println(intensity);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


            b1.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    if (lm.getLamp(pos).getState() == true) {
                        int color = getResources().getColor(R.color.EveryDay);
                        if (lm.getLamp(pos).getRgb() != color) {
                            lm.getLamp(pos).setColor(color);
                            cL.setBackgroundColor(color);
                            sV.setBackgroundColor(color);
                            @SuppressLint("WrongConstant")
                            Toast toast = Toast.makeText(getApplicationContext(), "EvryDay", 2);
                            toast.show();
                        } else {
                            lm.getLamp(pos).setColor(getResources().getColor(R.color.Normal));
                            cL.setBackgroundColor(getResources().getColor(R.color.Normal));
                            sV.setBackgroundColor(getResources().getColor(R.color.Normal));
                        }
                    }
                }

            });
        b2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (lm.getLamp(pos).getState() == true) {
                    int color = getResources().getColor(R.color.Focus);
                    if (lm.getLamp(pos).getRgb() != color) {
                        lm.getLamp(pos).setColor(color);
                        cL.setBackgroundColor(color);
                        sV.setBackgroundColor(color);
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "Focus", 2);
                        toast.show();
                    } else {
                        lm.getLamp(pos).setColor(getResources().getColor(R.color.Normal));
                        cL.setBackgroundColor(getResources().getColor(R.color.Normal));
                        sV.setBackgroundColor(getResources().getColor(R.color.Normal));
                    }
                }
            }

        });
        b3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (lm.getLamp(pos).getState() == true) {
                    int color = getResources().getColor(R.color.Relax);
                    if (lm.getLamp(pos).getRgb() != color) {
                        lm.getLamp(pos).setColor(color);
                        cL.setBackgroundColor(color);
                        sV.setBackgroundColor(color);
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "Relax", 2);
                        toast.show();
                    } else {
                        lm.getLamp(pos).setColor(getResources().getColor(R.color.Normal));
                        cL.setBackgroundColor(getResources().getColor(R.color.Normal));
                        sV.setBackgroundColor(getResources().getColor(R.color.Normal));
                    }
                }
            }

        });
    }



    private void getInitial(LampManager lm, Intent i) {
        final int pos = i.getExtras().getInt("pos");

        boolean state = lm.getLamp(pos).getState();
        final Switch aSwitch = findViewById(R.id.switchState);
        aSwitch.setChecked(state);

        int luminosity = lm.getLamp(pos).getIntensity();
        final SeekBar aBar = findViewById(R.id.seekBar);
        aBar.setProgress(luminosity);

        int color = lm.getLamp(pos).getRgb();
        final ScrollView scrollView = findViewById(R.id.scrollView);
        final ConstraintLayout constraintLayout = findViewById(R.id.Constraint_layout);
        if(aSwitch.isChecked()){
            constraintLayout.setBackgroundColor(color);
            scrollView.setBackgroundColor(color);
        }else{
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.Normal));
            scrollView.setBackgroundColor(getResources().getColor(R.color.Normal));
        }
    }



    //

    //
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

}
