package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.dinuscxj.shootrefreshview.ShootRefreshView;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.squareup.picasso.Picasso;

import java.io.File;


public class Lamp_1_Activity extends AppCompatActivity {

    ScrollView sV;
    ConstraintLayout cL;
    SeekBar bar;
    int pos;
    SeekBar mPullProgressBar;
    Switch aSwitch;
    int colorI;
    ShootRefreshView shootRefreshView;
    ColorPicker picker;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lamp_1_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //https://github.com/LarsWerkman/HoloColorPicker
        picker = (ColorPicker) findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        picker.setNewCenterColor(R.color.trasparence);
        sV= findViewById(R.id.scrollView);
        cL= findViewById(R.id.Constraint_layout);
        Button b1 = findViewById(R.id.Everyday_button);
        Button b2 = findViewById(R.id.Focus_button);
        Button b3 = findViewById(R.id.Relax_button);
        Button b4 = findViewById(R.id.Color_Button);
        aSwitch = findViewById(R.id.switchState);
        bar= findViewById(R.id.seekBar);

        shootRefreshView = findViewById(R.id.shoot_refresh_view);

        mPullProgressBar= findViewById(R.id.pull_progress_bar);




        final Intent i = getIntent();

        pos= i.getExtras().getInt("pos");


        final LampManager lm = LampManager.getInstance();
        aSwitch.setChecked(lm.getLamp(pos).getState());
        picker.setColor(lm.getLamp(pos).getRgb());
        ImageView iv = (ImageView) findViewById(R.id.imageView8);

        Picasso.get()
                .load(lm.getLamp(pos).getPicture())
                .transform(new CircleTransform())
                .into(iv);

        setTitle(lm.getLamp(pos).getName());
        colorI = lm.getLamp(pos).getRgb();
        RefreshLamp(lm,pos);
        getInitial(lm, pos);


        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state = aSwitch.isChecked();
                lm.getLamp(pos).setState(state);
                RefreshLamp(lm,pos);
            }
        });
        final int lum = lm.getLamp(pos).getIntensity();
        bar.setProgress(lum);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int intensity;
            @Override
            public void onProgressChanged(SeekBar seekBar, int intensity, boolean b) {
                this.intensity=intensity;
                lm.getLamp(pos).setIntensity(intensity);
                System.out.println(intensity);
                RefreshLamp(lm,pos);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lm.getLamp(pos).setIntensity(intensity);
                System.out.println(intensity);
                RefreshLamp(lm,pos);
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
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "EvryDay", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                RefreshLamp(lm,pos);
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
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "Focus", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                RefreshLamp(lm,pos);
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
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "Relax", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                RefreshLamp(lm,pos);
            }

        });
        // Aggiungere il bottone b4
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                if (lm.getLamp(pos).getState() == true) {
                    if (lm.getLamp(pos).getRgb() != color) {
                        lm.getLamp(pos).setColor(color);
                        Drawable drawable = getResources().getDrawable(R.drawable.shape_5);
                        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        Log.d("Sto cambiando colore", String.valueOf(color));
                    }
                }
                RefreshLamp(lm,pos);
            }
        });

        mPullProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress=progress;
                shootRefreshView.pullProgress(0, ((float) progress) / ((float) seekBar.getMax()));
                lm.getLamp(pos).setWing(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lm.getLamp(pos).setWing(progress);
                RefreshLamp(lm,pos);
            }
        });
    }

    private void getInitial(LampManager lm, int pos) {
        aSwitch.setChecked(lm.getLamp(pos).getState());
        int luminosity = lm.getLamp(pos).getIntensity();
        bar.setProgress(luminosity);
        int progress = lm.getLamp(pos).getWing();
        mPullProgressBar.setProgress(progress);
        shootRefreshView.pullProgress(0,((float) progress) / ((float) mPullProgressBar.getMax()));
    }
    public void RefreshLamp(LampManager lm, int i){
        new TcpClient(lm.getLamp(i)).execute();
    }
}
