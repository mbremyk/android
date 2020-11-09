package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnPlayClick(View v) {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }

    public void onBtnNewClick(View v) {
        Intent intent = new Intent(this, SudokuActivity.class);
        intent.putExtra("create", true);
        startActivity(intent);
    }
}