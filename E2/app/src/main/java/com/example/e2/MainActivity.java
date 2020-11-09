package com.example.e2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView txtA;
    private TextView txtB;
    private TextView nrAnswer;
    private TextView nrBound;
    private int a = 3, b = 5;
    int answer;
    int rngBound = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        nrAnswer = findViewById(R.id.nrAnswer);
        nrBound = findViewById(R.id.nrBound);
        txtA.setText("3");
        txtB.setText("5");
    }

    public void onBtnRandomClick(View v)
    {
        Intent intent = new Intent(this, RandomActivity.class);
        String bound = nrBound.getText().toString();
        rngBound = !bound.isEmpty() ? Integer.parseInt(bound) : rngBound;
        intent.putExtra("rngBound", rngBound);
        intent.putExtra("requestCode", Constants.GET_RANDOM_NUMBER);
        startActivityForResult(intent, Constants.GET_RANDOM_NUMBER);
    }

    public void onBtnAddClick(View v)
    {
        String ans = nrAnswer.getText().toString();
        answer = !ans.isEmpty() ? Integer.parseInt( ans) : 0;
        if (answer == a + b)
        {
            Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, getString(R.string.wrong) + (a + b), Toast.LENGTH_SHORT).show();
        }
        getAndSetRandomNumbers();
    }

    public void onBtnMultiplyClick(View v)
    {
        String ans = nrAnswer.getText().toString();
        answer = !ans.isEmpty() ? Integer.parseInt( ans) : 0;
        if (answer == a * b)
        {
            Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, getString(R.string.wrong) + (a * b), Toast.LENGTH_SHORT).show();
        }
        getAndSetRandomNumbers();
    }

    private void getAndSetRandomNumbers()
    {
        rngBound = Integer.parseInt(nrBound.getText().toString());
        Intent intent = new Intent(this, RandomActivity.class);
        intent.putExtra("rngBound", rngBound);
        intent.putExtra("requestCode", Constants.GET_A | Constants.GET_B);
        startActivityForResult(intent, Constants.GET_A | Constants.GET_B);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode & Constants.GET_A) > 0)
        {
            //Log.i("GET_A", Integer.toBinaryString(requestCode) + " " + Integer.toBinaryString(Constants.GET_A) + " " + (requestCode & Constants.GET_A));
            txtA.setText(String.format(Locale.UK, "%d", a = data.getIntExtra("numberA", 1)));
        }
        if ((requestCode & Constants.GET_B) > 0)
        {
            //Log.i("GET_B", Integer.toBinaryString(requestCode) + " " + Integer.toBinaryString(Constants.GET_B) + " " + (requestCode & Constants.GET_B));
            txtB.setText(String.format(Locale.UK, "%d", b = data.getIntExtra("numberB", 1)));
        }
        if ((requestCode & Constants.GET_RANDOM_NUMBER) > 0) {
            int n = data.getIntExtra("number", -1);
            TextView txtRandom = findViewById(R.id.txtRandom);
            txtRandom.setText(String.format(Locale.UK, "%d", n));
        }
    }
}