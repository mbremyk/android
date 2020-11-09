package com.example.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SudokuActivity extends AppCompatActivity implements NumberInputField.OnTextChangeListener {
	private Sudoku sudoku;
	private DatabaseManager db;
	private NumberInputField focused;
	private final int pad = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku);
		this.db = new DatabaseManager(this);
		init();
	}

	private void init() {
		Intent intent = getIntent();

		TableLayout tblSudoku = findViewById(R.id.tblSudoku);
		for (int i = 0; i < tblSudoku.getChildCount(); ++i) {
			TableRow tr = (TableRow) tblSudoku.getChildAt(i);
			tr.setPadding(pad, i % 3 == 0 ? pad * 2 : pad, pad, i % 3 == 2 ? pad * 2 : pad);
			for (int j = 0; j < 9; ++j) {
				NumberInputField nif = new NumberInputField(this, i, j, this);
				TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
				cellParams.setMargins(j % 3 == 0 ? pad * 2 : pad, i == 0 ? pad : 0, j % 3 == 2 ? pad : pad, i == 8 ? pad : 0);
				nif.setLayoutParams(cellParams);
				tr.addView(nif);
				nif.setBackgroundColor(Color.WHITE);

				nif.setOnFocusChangeListener((v, hasFocus) -> {
					if (hasFocus) focused = (NumberInputField) v;
					else focused = null;
				});
				tr.setBackgroundColor(0x80000000);
			}
		}

		Sudoku.Difficulty difficulty = (Sudoku.Difficulty) intent.getSerializableExtra("difficulty");
		if (difficulty != null) {
			String[] boards = db.getBoardsByDifficulty(difficulty);
			Random rng = new Random();
			this.sudoku = new Sudoku(boards[rng.nextInt(boards.length)]);
		} else {
			this.sudoku = new Sudoku();
		}

		boolean create = intent.getBooleanExtra("create", false);
		if (create) {
			initSpinner();
			initBtnSave();
			findViewById(R.id.btnCheck).setVisibility(View.GONE);
		} else {
			findViewById(R.id.spinCreateDiff).setVisibility(View.GONE);
			findViewById(R.id.btnSave).setVisibility(View.GONE);
			initBtnCheck();
		}

		fillSquares();
		initColours();
	}

	private void initColours() {
		LinearLayout lytColours = findViewById(R.id.lytColours);
		int[] colours = getResources().getIntArray(R.array.colours);
		for (int i : colours) {
			Button btn = new Button(this);
			lytColours.addView(btn);
			btn.setBackgroundColor(i);
			btn.setText("");
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
			btn.setLayoutParams(params);
			btn.setBackgroundTintList(null);
			btn.setOnClickListener(v -> {
				if (this.focused != null) {
					focused.setBackgroundColor(i);
				}
			});
		}
	}

	private void fillSquares() {
		TableLayout tblSudoku = findViewById(R.id.tblSudoku);
		for (int i = 0; i < tblSudoku.getChildCount(); i++) {
			TableRow tr = (TableRow) tblSudoku.getChildAt(i);
			for (int j = 0; j < tr.getChildCount(); j++) {
				NumberInputField nif = (NumberInputField) tr.getChildAt(j);
				nif.setValue(this.sudoku.getNumber(i, j));
				nif.setEnabled(this.sudoku.getNumber(i, j) == 0);
				nif.setTextColor(Color.parseColor("#000000"));
			}
		}
	}

	@Override
	public void onTextChange(String s, int x, int y) {
		int val = 0;
		try {
			val = Integer.parseInt(s);
		} catch (NumberFormatException e) {

		}
		this.sudoku.setNumber(x, y, val);
	}

	private void initSpinner() {
		Spinner spinDiff = findViewById(R.id.spinCreateDiff);
		spinDiff.setVisibility(View.VISIBLE);
		ArrayAdapter<CharSequence> diffAdapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_dropdown_item);
		spinDiff.setAdapter(diffAdapter);
	}

	private void initBtnSave() {
		Button btnSave = findViewById(R.id.btnSave);
		btnSave.setVisibility(View.VISIBLE);
		btnSave.setOnClickListener(v -> {
			db.addBoard(sudoku.getNumbers(), Sudoku.Difficulty.values()[((Spinner) findViewById(R.id.spinCreateDiff)).getSelectedItemPosition()]);
			finish();
		});
	}

	private void initBtnCheck() {
		Button btnCheck = findViewById(R.id.btnCheck);
		btnCheck.setVisibility(View.VISIBLE);
		btnCheck.setOnClickListener(v -> {
			boolean isSolved = this.sudoku.isSolved();
			Toast.makeText(this, getResources().getText(isSolved ? R.string.solved : R.string.unsolved), Toast.LENGTH_SHORT).show();
		});
	}
}