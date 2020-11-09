package com.example.e7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager db;
    private ListView lstOut;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            db = new DatabaseManager(this);
            db.readDbFromFile(this, R.raw.books);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lstOut = findViewById(R.id.lstOut);
        setListColourToPreferred();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.getAllBooksAndAuthors());
        lstOut.setAdapter(listAdapter);
    }

    private void setListColourToPreferred() {
        SharedPreferences appPrefs = getDefaultSharedPreferences(this);
        String color = appPrefs.getString("listBackgroundPrefs", "#ffffff");
        lstOut.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferences) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setListColourToPreferred();
    }

    public void onBtnBooksClick(View v) {
        listAdapter.clear();
        listAdapter.addAll(db.getAllBooks());
        listAdapter.notifyDataSetChanged();
    }

    public void onBtnAuthorsClick(View v) {
        listAdapter.clear();
        listAdapter.addAll(db.getAllAuthors());
        listAdapter.notifyDataSetChanged();
    }
}