package com.polito.did2017.lampapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.squareup.picasso.Picasso;

/**
 * Created by Davide on 11/02/2018.
 */

public class LampAdapter extends BaseSwipeAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface  {
    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);
    private LampManager lm;
    private Context contextOfApplication;
    private TextView tv;
    private Switch s;
    private ImageView iv;
    private SwipeLayout swipeLayout;
    private View vi;

    public LampAdapter(Context ctx) {
        lm = LampManager.getInstance();
        contextOfApplication = ctx.getApplicationContext();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.layout.adapterlist;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(contextOfApplication).inflate(R.layout.adapterlist,parent,false);
        swipeLayout = v.findViewById(R.id.swipe1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper));
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void fillValues(final int position, final View convertView) {

        vi=convertView;
        tv  = vi.findViewById(R.id.textViewAD);
        s   = vi.findViewById(R.id.switchAL);
        iv  = vi.findViewById(R.id.imageViewAD);
        tv.setText( lm.getLamp(position).getName());
        s.setChecked(lm.getLamp(position).getState());
        Load_image(position);


        final boolean mIsOpen[]=new boolean[lm.getLamps().size()];
        mIsOpen[position]=true;
        swipeLayout.removeAllSwipeListener();
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                for (int i=0;i<mIsOpen.length;i++)
                    mIsOpen[i]=false;
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                for (int i=0;i<mIsOpen.length;i++)
                    mIsOpen[i]=false;
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                for (int i=0;i<mIsOpen.length;i++)
                    mIsOpen[i]=false;
            }

            @Override
            public void onClose(SwipeLayout layout) {
                for (int i=0;i<mIsOpen.length;i++)
                    mIsOpen[i]=true;
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            }

            @Override
            public void onHandRelease(final SwipeLayout layout, float xvel, float yvel) {
                if (layout.getOpenStatus() == SwipeLayout.Status.Open) {
                    layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.close();
                        }
                    }, 50);
                }
            }
        });
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sto aprendo:", lm.getLamp(position).getName() +" nella posizione "+ position);
                if(mIsOpen[position])
                    openLamp(position);
            }
        });

        Button b = (Button) vi.findViewById(R.id.delete);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sto chiudendo:", lm.getLamp(position).getName() +" nella posizione "+ position);
                closeLamp(position);
            }
        });
        s.setOnCheckedChangeListener((v1,v2) -> {
            lm.getLamp(position).setState(v2);
            Log.d("DAvide_log"+position, String.valueOf(v2));
            RefreshLamp(lm,position);
        });
    }

    private void Load_image(int position) {
        Picasso.get()
                .load(lm.getLamp(position).getPicture())
                .transform(new CircleTransform())
                .into(iv);
    }


    public void closeLamp(final int position) {
        lm.removeLamp(lm.getLamp(position).getName());
        notifyDatasetChanged();
    }

    public void openLamp(int position) {
        Intent i = new Intent(contextOfApplication, Lamp_1_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("pos", position);
        contextOfApplication.startActivity(i);
    }

    @Override
    public int getCount() {
        return lm.getLamps().size();
    }

    @Override
    public Object getItem(int position) {
        return lm.getLamp(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }
    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    public void RefreshLamp(LampManager lm, int i){
        new TcpClient(lm.getLamp(i)).execute();
    }


}
