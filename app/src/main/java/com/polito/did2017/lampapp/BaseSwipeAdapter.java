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
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Davide on 16/01/2018.
 */

public class BaseSwipeAdapter extends BaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    LampManager lm;
    Context contextOfApplication;
    BaseSwipeAdapter baseSwipeAdapter = this;

    public BaseSwipeAdapter(Context ctx) {
        lm=LampManager.getInstance(ctx);
        contextOfApplication=ctx.getApplicationContext();
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
                new TcpClient(lm.getLamp(i), contextOfApplication).execute();
            }
        });
        Picasso.with(contextOfApplication).load(lm.getLamp(i).getPicture()).into(iv);
        ////////////////////

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItem(i);
            }
        });

        SwipeLayout swipeLayout = null;
        swipeLayout = v.findViewById(R.id.swipe1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));

        final View finalV = v;

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            public boolean mIsOpen=false;

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.d("SWIPE_POS","onStartOpen"+i);
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.d("SWIPE_POS_op","onOpen"+i);
                mIsOpen = true;
                closeItem(finalV,l);

            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.d("SWIPE_POS","onStartClose"+i);
            }

            @Override
            public void onClose(SwipeLayout layout){
                mIsOpen = false;
                Log.d("SWIPE_POS","onClose"+i);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.d("SWIPE_POS","onUpdate"+i);
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                if(mIsOpen)
                Log.d("SWIPE_POS","onHandRelease"+i);
            }
        });
        return v;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public void openItem(int position) {
        Intent i = new Intent(contextOfApplication, Lamp_1_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("pos", position);
        contextOfApplication.startActivity(i);
    }

    @Override
    public void closeItem(int position) {

    }

    public void closeItem(View v, final Lamp l) {
        //final int adapterPosition = viewHolder.getAdapterPosition();
        final Boolean state = l.getState();
        final String url = l.getURL();
        final int lum = l.getIntensity();
        final int col = l.getRgb();
        final int win = l.getWing();
        final String name = l.getName();
        final String img =  l.getPicture();
        //snackbar = Snackbar;
        Snackbar snackbar = Snackbar.make(v, "UNDO", Snackbar.LENGTH_LONG);
        snackbar.setAction("ANNULLA", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int mAdapterPosition = viewHolder.getAdapterPosition();
                try {
                    lm.addLamp(state, url, lum, col, win, name,img);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyDatasetChanged();
                return;
            }
        });
        snackbar.show();
        lm.removeLamp(name);
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
        return R.layout.swipelayout;
    }

    @Override
    public void notifyDatasetChanged() {
        baseSwipeAdapter.notifyDataSetChanged();
    }
}
