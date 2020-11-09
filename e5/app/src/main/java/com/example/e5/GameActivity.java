package com.example.e5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    HttpApi httpApi;
    public static final String baseUrl = "http://tomcat.stud.iie.ntnu.no/studtomas/tallspill.jsp";
    final String[] credKeys = {"navn", "kortnummer"};
    final String[] numberKeys = {"tall"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        httpApi = new HttpApi(this, baseUrl);

        Intent intent = getIntent();

        String[] values = {intent.getStringExtra("name"), intent.getStringExtra("card number")};
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < credKeys.length; ++i) {
            params.put(credKeys[i], values[i]);
        }
        httpApi.get(params);
    }

    public void setOutputText(String s) {
        TextView txtOut = findViewById(R.id.txtOut);
        txtOut.setText(s);
    }

    public void onBtnGuessClick(View v) {
        TextView txtGuess = findViewById(R.id.txtGuess);
        if(txtGuess.getText().toString().isEmpty()) return;
        Map<String, String> params = new HashMap<String, String>();
        params.put(numberKeys[0], txtGuess.getText().toString());
        httpApi.get(params);
    }
}