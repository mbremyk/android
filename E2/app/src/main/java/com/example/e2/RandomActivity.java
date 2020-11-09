package com.example.e2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {
    static Random random = new Random();
    private int rngBound = 100;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_random);
        intent = getIntent();
        rngBound = intent.getIntExtra("rngBound", 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(this, Integer.toString(random.nextInt(rngBound)), Toast.LENGTH_SHORT).show();
        Intent data = new Intent();
        int requestCode = intent.getIntExtra("requestCode", 0);
        if ((requestCode & Constants.GET_A) > 0) {
            data.putExtra("numberA", random.nextInt(rngBound));
        }
        if ((requestCode & Constants.GET_B) > 0) {
            data.putExtra("numberB", random.nextInt(rngBound));
        }
        if ((requestCode & Constants.GET_RANDOM_NUMBER) > 0) {
            data.putExtra("number", random.nextInt(rngBound));
        }
        setResult(RESULT_OK, data);
        finish();
    }
}