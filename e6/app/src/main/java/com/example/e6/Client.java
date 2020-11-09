package com.example.e6;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private final static String TAG = "Client";
    private final static String IP = "127.0.0.1";
    private final static int PORT = 12345;
    private final double a, b;
    OnResponseListener mListener;

    public Client(Activity activity, double a, double b) {
        this.mListener = (OnResponseListener) activity;
        this.a = a;
        this.b = b;
    }

    public void run() {
        Socket s = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            s = new Socket(IP, PORT);
            Log.v(TAG, "C: Connected to server" + s.toString());
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(a);
            out.println(b);
            mListener.onResponse(Double.parseDouble(in.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnResponseListener{
        public void onResponse(Double d);
    }
}
