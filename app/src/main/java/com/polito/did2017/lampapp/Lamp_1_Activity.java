package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.dinuscxj.shootrefreshview.ShootRefreshView;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.squareup.picasso.Picasso;


public class Lamp_1_Activity extends AppCompatActivity {

    private static final int SHORT_DELAY = 500;

    private SeekBar bar;
    private int pos;
    private SeekBar mPullProgressBar;
    private Switch aSwitch;
    private ShootRefreshView shootRefreshView;
    private ColorPicker picker;
    private LampManager lm;
    private ImageView iv;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        final Intent i = getIntent();
        pos = i.getExtras().getInt("pos");
        lm = LampManager.getInstance();
        setTitle(lm.getLamp(pos).getName());

        setContentView(R.layout.activity_lamp_1_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aSwitch = findViewById(R.id.switchState);


        //https://github.com/LarsWerkman/HoloColorPicker
        picker = (ColorPicker) findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        picker.setNewCenterColor(R.color.trasparence);
        iv = (ImageView) findViewById(R.id.imageView8);


        bar = findViewById(R.id.seekBar);

        Button b1 = findViewById(R.id.Everyday_button);
        Button b2 = findViewById(R.id.Focus_button);
        Button b3 = findViewById(R.id.Relax_button);

        shootRefreshView = findViewById(R.id.shoot_refresh_view);
        mPullProgressBar = findViewById(R.id.pull_progress_bar);

        setInitial(lm, pos);

        aSwitch.setOnClickListener(view -> {
            boolean state = aSwitch.isChecked();
            lm.getLamp(pos).setState(state);
            RefreshLamp(lm, pos);
            stateWidgetChanged();
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int intensity;

            @Override
            public void onProgressChanged(SeekBar seekBar, int intensity, boolean b) {
                this.intensity = intensity;
                lm.getLamp(pos).setIntensity(intensity);
                System.out.println(intensity);
                RefreshLamp(lm, pos);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        b1.setOnClickListener(v -> {
            String name_button = "EveryDay";
            pressButton(getResources().getColor(R.color.EveryDay), name_button);
        });

        b2.setOnClickListener(v -> {
            String name_button = "Focus";
            pressButton(getResources().getColor(R.color.Focus), name_button);

        });

        b3.setOnClickListener(v -> {
            String name_button = "Relax";
            pressButton(getResources().getColor(R.color.Relax), name_button);

        });
            picker.setOnColorChangedListener(color -> {
                if (lm.getLamp(pos).getRgb() != color) {
                    lm.getLamp(pos).setColor(color);
                    Drawable drawable = getResources().getDrawable(R.drawable.shape_5);
                    drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                    Log.d("Sto cambiando colore", String.valueOf(color));
                }
                RefreshLamp(lm, pos);
            });

        mPullProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                shootRefreshView.pullProgress(0, ((float) progress) / ((float) seekBar.getMax()));
                lm.getLamp(pos).setWing(progress);
                RefreshLamp(lm, pos);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setInitial(LampManager lm, int pos) {
        aSwitch.setChecked(lm.getLamp(pos).getState());
        stateWidgetChanged();
        picker.setColor(lm.getLamp(pos).getRgb());
        Picasso.get()
                .load(lm.getLamp(pos).getPicture())
                .transform(new CircleTransform())
                .into(iv);
        int luminosity = lm.getLamp(pos).getIntensity();
        bar.setProgress(luminosity);
        int progress = lm.getLamp(pos).getWing();
        mPullProgressBar.setProgress(progress);
        shootRefreshView.pullProgress(0, ((float) progress) / ((float) mPullProgressBar.getMax()));
    }

    public void RefreshLamp(LampManager lm, int i) {
        new TcpClient(lm.getLamp(i)).execute();
    }

    public void pressButton(int color, String name_button) {
        lm = LampManager.getInstance();
        if (checkIsOn()) {
            if (lm.getLamp(pos).getRgb() != color) {
                lm.getLamp(pos).setColor(color);
                picker.setColor(color);
                @SuppressLint("WrongConstant")
                Toast toast = Toast.makeText(getApplicationContext(), name_button, SHORT_DELAY);
                toast.show();
                toastCancel(toast);
            }
        }
        RefreshLamp(lm, pos);
    }

    public boolean checkIsOn() {
        if (lm.getLamp(pos).getState() == true)
            return true;
        else {
            @SuppressLint("WrongConstant")
            Toast toast = Toast.makeText(getApplicationContext(), R.string.turn_on, SHORT_DELAY);
            toast.show();
            toastCancel(toast);
        }
        return false;
    }

    private void toastCancel(Toast toast) {
        Handler handler = new Handler();
        handler.postDelayed(() -> toast.cancel(), 850);
    }

    public void stateWidgetChanged() {
        bar.setEnabled(checkIsOn());
        picker.setEnabled(checkIsOn());
        if (!checkIsOn()) {
            picker.setAlpha(0.001f);
        } else {
            picker.setAlpha(1.0f);
        }
        mPullProgressBar.setEnabled(checkIsOn());
    }
}
