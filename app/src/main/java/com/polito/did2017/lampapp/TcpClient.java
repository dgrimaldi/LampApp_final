package com.polito.did2017.lampapp;



import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;


/**
 * Created by Davide on 05/01/2018.
 */

public class TcpClient extends AsyncTask<Context, Void,Boolean> {


    private int port=2048;
    private Socket socket;
    private static BufferedWriter msgOut;
    private static BufferedReader msgIn;
    private String incomingMessage;
    boolean send;
    private Lamp lamp;


    public TcpClient(Lamp lamp) {
        this.lamp=lamp;
    }


    protected void onPostExecute(Boolean result){
        super.onPostExecute(result);
        //Toast.makeText(context, "Changed: "+lamp.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Context... params) {
        try {
            send=false;
            while(send==false){
                Log.d(TAG,"Apertura connesione");
                socket = new Socket();
                System.out.println("++++++++++++++++++!"+lamp.getURL());
 //               int i = lamp.getURL().indexOf("[");
 //               String URL = lamp.getURL().substring(0,i);
                socket.connect(new InetSocketAddress(lamp.getURL(),port));
                msgOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                msgIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int colo = lamp.getRgb();
                String color = Integer.toHexString(colo).toUpperCase();
                write(lamp.getState()+","+lamp.getIntensity()+"*"+color+"/"+lamp.getWing());
                Log.d("TCP",lamp.getState()+","+lamp.getIntensity()+"*"+color+"/"+lamp.getWing());
                //write(pos)
                incomingMessage = msgIn.readLine();

                try {
                if (incomingMessage.equals("OK!")) {
                    System.out.println("ricevuto");
                    send = true;
                }else
                    send = false;
                }catch (NullPointerException e){
                    e.printStackTrace();
                    send = false;
                }
                //Thread.sleep(1200);
                socket.close();
                msgOut.close();
                msgIn.close();
            }
        } catch (ConnectException e) {
            Log.e("Error", "No device conect");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(final String data) {
        try {
            Log.d(TAG, "write(): data = " + data);
            System.out.println(data);
            msgOut.write(data);
            msgOut.flush();
        } catch (IOException ex) {
            Log.e(TAG, "write(): " + ex.toString());
        } catch (NullPointerException ex) {
            Log.e(TAG, "write(): " + ex.toString());
        }
    }
}