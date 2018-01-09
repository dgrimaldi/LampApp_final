package com.polito.did2017.lampapp;



import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
    Context context;
    private Lamp lamp;


    public TcpClient(Lamp lamp, Context context) {
        this.lamp=lamp;
        this.context=context;
    }


    protected void onPostExecute(Boolean result){
        super.onPostExecute(result);
        Toast.makeText(context, "Changed: "+lamp.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Context... params) {
        try {
            send=false;
            while(send==false){
                Log.d(TAG,"Apertura connesione");
                socket = new Socket();
                socket.connect(new InetSocketAddress(lamp.URL,port));
                msgOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                msgIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                write(lamp.getState()+",");
                write(lamp.getIntensity()+"*");
                int colo = lamp.getRgb();
                String color = Integer.toHexString(colo).toUpperCase();
                write(color+"/");
                System.out.println(msgOut);
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