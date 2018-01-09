package com.polito.did2017.lampapp;

        import android.annotation.SuppressLint;
        import android.util.Log;
        import android.widget.TextView;

        import java.io.IOException;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;
        import java.net.InetSocketAddress;
        import java.net.SocketException;

/**
 * Created by Davide on 21/12/2017.
 */

public class ClientListen implements Runnable {

    int port = 4096;
    public String URL;
    private DatagramSocket udpSocket;
    private DatagramPacket packet;
    String text;

    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        boolean run = true;

        try {
            byte[] message = new byte[8000];
            InetAddress remote_address;
            udpSocket = new DatagramSocket(port);
            packet = new DatagramPacket(message, message.length);

            Log.i("UDP client: ", "about to wait to receive");
            udpSocket.receive(packet);
            String text = new String(message, 0, packet.getLength());
            this.text = text;
            Log.d("Received data", text);
            packet.getPort();
            URL = packet.getAddress().toString();
            Log.d("Address", String.valueOf(packet.getAddress()));
            Log.d("Port", String.valueOf(packet.getPort()));
            udpSocket.close();

        } catch (IOException e) {
            Log.e("UDP client has IOException", "error: ", e);
            run = false;
        }
    }
}


