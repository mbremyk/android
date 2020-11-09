package com.example.e3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static ArrayList<String> people = new ArrayList<String>();
    ArrayAdapter<String> peopleAdapter;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        people.add("Test\n31/10/2020");
        initList();
    }

    public void onBtnAddClick(View v) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, 1);
    }

    private void initList() {
        peopleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, people);
        ListView lstPeople = findViewById(R.id.lstPeople);
        lstPeople.setAdapter(peopleAdapter);

        lstPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                index = position;
                intent.putExtra("data", people.get(index));
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    people.add(data.getStringExtra("data"));
                    break;
                case 2:
                    people.set(index, data.getStringExtra("data"));
                    break;
                default:
                    break;
            }
        }
        peopleAdapter.notifyDataSetChanged();
    }
}