package com.polito.did2017.lampapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Davide on 16/01/2018.
 */

public class PreAdapter extends BaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    LampManager lm;
    Context contextOfApplication;
    //BaseSwipeAdapter baseSwipeAdapter = this;
    boolean mIsOpen=true;
    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);
    private SwipeLayout lo;
    private View vC;
    private View vCan;

    public PreAdapter() {
        lm = LampManager.getInstance();
        //lm = new LampManager();
        //contextOfApplication = ctx.getApplicationContext();
    }


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = null;
        if (view == null)
            v = LayoutInflater.from(contextOfApplication).inflate(R.layout.adapterlist, viewGroup, false);
        else
            v = view;
        final TextView tv = v.findViewById(R.id.textViewAD);
        final Switch s = v.findViewById(R.id.switchAD);
        ImageView iv = v.findViewById(R.id.imageViewAD);
        final Lamp l = lm.getLamp(i);
        final String Name = lm.getLamp(i).getName();
        //Imposta il testo
        tv.setText(Name);
        //setta lo swicht
        boolean b = lm.getLamp(i).getState();
        s.setChecked(b);
        final int color = lm.getLamp(i).getRgb();
        // imposta lo swict

            s.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    final boolean st = s.isChecked();
                    lm.getLamp(i).setState(st);
                    new TcpClient(lm.getLamp(i)).execute();
                }
            });
        Picasso.with(contextOfApplication).load(lm.getLamp(i).getPicture()).into(iv);
        ////////////////////

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mIsOpen) {
                        openItem(i);
                    }
                }
            });

        SwipeLayout swipeLayout = null;
        swipeLayout = v.findViewById(R.id.swipe1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));

        final View finalV1 = v;


        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                mIsOpen = false;
                Log.d("SWIPE_POS","onStartOpen"+i);
            }

            @Override
            public void onOpen(final SwipeLayout layout) {
                Log.d("SWIPE_POS_op","onOpen"+i+lm.getLamp(i).getName());
                lo=layout;
                vCan = finalV1;
                mIsOpen = false;
                Log.d("onOpen_nameLM",lm.getLamp(i).getName());
                closeItem(i);
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lo.close();
                    }
                }, 50);
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                mIsOpen = false;
                Log.d("SWIPE_POS","onStartClose"+i);
            }

            @Override
            public void onClose(SwipeLayout layout){
                mIsOpen = true;
                Log.d("SWIPE_POS","onClose"+i);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.d("SWIPE_POS","onUpdate"+i);
            }

            @Override
            public void onHandRelease(final SwipeLayout layout, float xvel, float yvel) {
                if(layout.getOpenStatus() == SwipeLayout.Status.Open){
                    layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.close();
                        }
                    }, 50);
                }
                Log.d("SWIPE_POS","onHandRelease"+i);
            }
        });

        //lo.close();
        return v;
    }



    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public void openItem(int position) {
        Log.d("positionO",lm.getLamp(position).getName());
        Intent i = new Intent(contextOfApplication, Lamp_1_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("pos", position);
        contextOfApplication.startActivity(i);
    }

    @Override
    public void closeItem(final int position) {
        Log.d("closeItem_nameLM",lm.getLamp(position).getName());
        final Boolean state = lm.getLamp(position).getState();
        final String url = lm.getLamp(position).getURL();
        final int lum = lm.getLamp(position).getIntensity();
        final int col = lm.getLamp(position).getRgb();
        final int win = lm.getLamp(position).getWing();
        final String name = lm.getLamp(position).getName();
        final String img =  lm.getLamp(position).getPicture();
        lm.removeLamp(name);
        notifyDatasetChanged();
        Snackbar snackbar = Snackbar.make(vCan, "UNDO", Snackbar.LENGTH_LONG);
        snackbar.setAction("ANNULLA", new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //int mAdapterPosition = viewHolder.getAdapterPosition();
                try {
                    lm.addLamp(state, url, lum, col, win, name,img);
                    ViewGroup viewGroup=null;
                    getView(position,vCan,viewGroup);
                    notifyDatasetChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lo.close();
                    }
                }, 50);
                notifyDatasetChanged();
                return;
            }
        });
        snackbar.show();
        Log.d("Nome Swipe", name);
        notifyDatasetChanged();
    }
    @Override
    public void closeAllExcept(SwipeLayout layout) {

    }



    @Override
    public void closeAllItems() {
    }

    @Override
    public List<Integer> getOpenItems() {
        return null;
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return null;
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
    }

    @Override
    public boolean isOpen(int position) {
        return false;
    }

    @Override
    public Attributes.Mode getMode() {
        return null;
    }

    @Override
    public void setMode(Attributes.Mode mode) {

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.layout.adapterlist;
    }

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }
}
