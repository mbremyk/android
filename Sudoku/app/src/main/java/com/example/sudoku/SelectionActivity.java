package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        initSpinner();
    }

    public void initSpinner() {
        Spinner spinDiff = findViewById(R.id.spinDiff);
        ArrayAdapter<CharSequence> diffAdapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_dropdown_item);
        spinDiff.setAdapter(diffAdapter);
    }

    public void onBtnStartClick(View v) {
        Spinner spinDiff = findViewById(R.id.spinDiff);
        Log.i("diff", spinDiff.getSelectedItem().toString());
        Intent intent = new Intent(this, SudokuActivity.class);
        intent.putExtra("difficulty", Sudoku.Difficulty.values()[spinDiff.getSelectedItemPosition()]);
        startActivity(intent);
    }
}