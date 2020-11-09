package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager db = new DatabaseManager(this);
        String[] samples = getResources().getStringArray(R.array.sample_sudoku);
        String[] diffs = getResources().getStringArray(R.array.sample_difficulties);
        Sudoku.Difficulty[] difficulties = new Sudoku.Difficulty[diffs.length];
        for (int i = 0; i < diffs.length; i++) {
            difficulties[i] = Sudoku.Difficulty.getDifficultyFromStr(diffs[i]);
        }
        db.insertSudokus(samples, difficulties);
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

    public void onBtnInstructionsClick(View v) {
        startActivity(new Intent(this, InstructionsActivity.class));
    }
}