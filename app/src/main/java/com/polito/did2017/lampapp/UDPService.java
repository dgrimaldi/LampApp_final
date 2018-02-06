package com.polito.did2017.lampapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Davide on 12/01/2018.
 */

public class UDPService extends Service  {


    private final IBinder myBinder = new MyLocalBinder();
    LampManager lm = LampManager.getInstance(this);
    int port = 4096;
    public String URL;
    private DatagramSocket udpSocket;
    private DatagramPacket packet;
    String text;
    static boolean bol =false;
    OnHeadlineSelectedListener mCallback;
    private String name;
    private int lum;
    private String col;
    private int win;
    private String state;
    private String img;
    private int color;

    /////





    private void listenAndWaitAndThrowIntent() throws Exception {
        byte[] message = new byte[5120];
        if(udpSocket == null || udpSocket.isClosed()){
            udpSocket = new DatagramSocket(port);
        }
        packet = new DatagramPacket(message, message.length);
        //Thread.sleep(5000);
        Log.i("UDP client: ", "about to wait to receive");
        udpSocket.receive(packet);
        String text = new String(message, 0, packet.getLength());
        text.trim();
        this.text = text;
        Log.d("Received data", text);
        packet.getPort();

        name = text.substring(0,text.indexOf("*"));
        state = text.substring(text.indexOf("*")+1,text.indexOf("-"));
        lum = Integer.parseInt((text.substring(text.indexOf("-")+1,text.indexOf("+"))));
        col = text.substring(text.indexOf("+")+1,text.indexOf(","));
        color = (int) Long.parseLong(col, 16);
        win = Integer.parseInt(text.substring(text.indexOf(",")+1,text.indexOf("<")));
        img =  text.substring(text.indexOf("<")+1,text.length());
        URL = packet.getAddress().toString().replace("/", "");
        Log.d("UDP_PACKET",name+", "+state+", "+lum+", "+col+", "+win+", "+img);
        Log.d("Address", String.valueOf(packet.getAddress()));
        Log.d("Port", String.valueOf(packet.getPort()));
        udpSocket.close();
    }

    Thread UDPBroadcastThread;

    void startListenForUDPBroadcast(final OnHeadlineSelectedListener a){
        UDPBroadcastThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                while (true) {
                    try {
                        Thread.sleep(5000);
                        listenAndWaitAndThrowIntent();
                         bol = lm.addLamp(Boolean.parseBoolean(state),URL,lum,color,win,name,img);
                         if (bol){
                        //mCallback = (OnHeadlineSelectedListener) MainActivity.getContextOfApplication();
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    a.onArticleSelected(2);
                                }
                            });
                         }

                    } catch (Exception e) {
                        Log.d("UDP", e.getMessage());
                    }
                }
            }
        });
        UDPBroadcastThread.start();
    }

    //private void stopListen() {
    // super.onDestroy();
    // }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*public int onStartCommand(Intent intent, int flags, int startId){
        startListenForUDPBroadcast();
        Log.i("UDP", "Service Started");
        return START_STICKY;
    }*/
    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        Log.i("UDP", "Service Started");
        return myBinder;
    }

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
        void runOnUiThread(Runnable r);
    }

    public class MyLocalBinder extends Binder {
        UDPService getService() {
            return UDPService.this;
        }
        }
}
