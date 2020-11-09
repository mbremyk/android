package com.example.e6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Client.OnResponseListener {
    TextView txtOut;
    double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOut = findViewById(R.id.txtOut);
        new Server().start();
    }

    public void onBtnAddClick(View v) {
        TextView nrA = findViewById(R.id.nrA);
        TextView nrB = findViewById(R.id.nrB);
        double a = Double.parseDouble(nrA.getText().toString());
        double b = Double.parseDouble(nrB.getText().toString());
        Client c = new Client(this, a, b);
        c.start();
        try {
            c.join();
            this.setTxtOut(Double.toString(sum));
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setTxtOut(String s) {
        txtOut.setText(s);
    }

    @Override
    public void onResponse(Double d) {
        this.sum = d;
    }
}