package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class Lamp_1_Activity extends AppCompatActivity {

    public static Context contextOfApplication;



    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        contextOfApplication = getApplicationContext();


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lamp_1_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ScrollView sV = findViewById(R.id.scrollView);
        final ConstraintLayout cL = findViewById(R.id.Constraint_layout);
        Button b1 = findViewById(R.id.Everyday_button);
        Button b2 = findViewById(R.id.Focus_button);
        Button b3 = findViewById(R.id.Relax_button);
        Button b4 = findViewById(R.id.Color_Button);
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
        RefreshLamp(lm,pos);
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
                RefreshLamp(lm,pos);
            }
        });
        //Luminosità
        final int lum = lm.getLamp(pos).getIntensity();
        bar.setProgress(lum);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int intensity;
            @Override
            public void onProgressChanged(SeekBar seekBar, int intensity, boolean b) {
                this.intensity=intensity;
                lm.getLamp(pos).setIntensity(intensity);
                System.out.println(intensity);
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
                RefreshLamp(lm,pos);
            }

        });
        b4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (lm.getLamp(pos).getState() == true) {
                    findViewById(R.id.Color_Button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Context context = Lamp_1_Activity.this;

                            ColorPickerDialogBuilder
                                    .with(context)
                                    .setTitle(R.string.color_dialog_title)
                                    .initialColor(getResources().getColor(R.color.Normal))
                                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                    .density(12)
                                    .setOnColorChangedListener(new OnColorChangedListener() {
                                        @Override
                                        public void onColorChanged(int selectedColor) {
                                            // Handle on color change
                                            Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                                        }
                                    })
                                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                                        @Override
                                        public void onColorSelected(int selectedColor) {
                                            Toast.makeText(getApplicationContext(),Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setPositiveButton("ok", new ColorPickerClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                            lm.getLamp(pos).setColor(selectedColor);
                                            cL.setBackgroundColor(selectedColor);
                                            sV.setBackgroundColor(selectedColor);
                                            if (allColors != null) {
                                                StringBuilder sb = null;

                                                for (Integer color : allColors) {
                                                    if (color == null)
                                                        continue;
                                                    if (sb == null)
                                                        sb = new StringBuilder("Color List:");
                                                    sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                                }

                                                if (sb != null)
                                                    Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .showColorEdit(true)
                                    .setColorEditTextColor(ContextCompat.getColor(Lamp_1_Activity.this, android.R.color.holo_blue_bright))
                                    .build()
                                    .show();
                        }
                    });

                   /* int color = getResources().getColor(R.color.Relax);
                    if (lm.getLamp(pos).getRgb() != color) {
                        lm.getLamp(pos).setColor(color);
                        cL.setBackgroundColor(color);
                        sV.setBackgroundColor(color);
                        @SuppressLint("WrongConstant")
                        Toast toast = Toast.makeText(getApplicationContext(), "Relax", 2);
                        toast.show();*/
                }
                RefreshLamp(lm,pos);
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


    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    public void RefreshLamp(LampManager lm, int i){
        new TcpClient(lm.getLamp(i), contextOfApplication).execute();

    }

}
