package com.example.sudoku;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "SudokuDB";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_SUDOKU = "sudoku";
    static final String KEY_ID = "_id";
    static final String KEY_NUMBERS = "numbers";
    static final String KEY_DIFFICULTY = "difficulty";

    static final String CREATE_DB = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT UNIQUE NOT NULL, %s INTEGER NOT NULL);", TABLE_SUDOKU, KEY_ID, KEY_NUMBERS, KEY_DIFFICULTY);

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUDOKU);
        onCreate(db);
    }

    public void clean() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUDOKU);
        onCreate(db);
    }

    public void addBoard(int[][] numbers, Sudoku.Difficulty difficulty) {
        StringBuilder raw = new StringBuilder();
        for (int[] number : numbers) {
            for (int i : number) {
                raw.append(i);
            }
        }
        this.addBoard(raw.toString(), difficulty);
    }

    public void addBoard(String raw, Sudoku.Difficulty difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBERS, raw);
        values.put(KEY_DIFFICULTY, String.valueOf(difficulty));

        db.insert(TABLE_SUDOKU, null, values);
    }

    public String[] getBoardsByDifficulty(Sudoku.Difficulty difficulty) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUDOKU, new String[]{KEY_NUMBERS}, KEY_DIFFICULTY + "=?", new String[]{String.valueOf(difficulty)},null, null, null);
        String[] boards = new String[cursor.getCount()];
        for (int i = 0; i < boards.length; i++) {
            cursor.moveToNext();
            boards[i] = cursor.getString(0);
        }
        cursor.close();
        return boards;
    }

    public void insertSudokus(String[] raw, Sudoku.Difficulty[] difficulties) {
        for(int i = 0; i < raw.length; i++) {
            this.addBoard(raw[i], i < difficulties.length ? difficulties[i] : Sudoku.Difficulty.EASY);
        }
    }
}
