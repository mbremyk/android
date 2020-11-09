package com.example.e3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Intent intent;
    EditText txtName;
    CalendarView cal;
    String strDay, strMonth, strYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        intent = getIntent();
        txtName = findViewById(R.id.txtName);
        cal = findViewById(R.id.cal);
        String data = intent.getStringExtra("data");
        if (data != null) {
            txtName.setText(data.split("\n")[0]);
            String[] date = data.split("\n")[1].split("/");
            strDay = date[0];
            strMonth = date[1];
            strYear = date[2];
        }
        findViewById(R.id.btnSave).setClickable(intent.getStringExtra("data") != null);
        try {
            cal.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(data.split("\n")[1]).getTime());
        } catch (Exception e) {
        }
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findViewById(R.id.btnSave).setClickable(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                strDay = Integer.toString(dayOfMonth);
                strMonth = Integer.toString(month + 1);
                strYear = Integer.toString(year);
                findViewById(R.id.btnSave).setClickable(true);
            }
        });
    }

    public void onBtnSaveClick(View v) {
        String name = txtName.getText().toString();
        strDay = strDay.length() < 2 ? "0" + strDay : strDay;
        strMonth = strMonth.length() < 2 ? "0" + strMonth : strMonth;
        String date = strDay + "/" + strMonth + "/" + strYear;
        Intent data = new Intent();
        data.putExtra("data", name + "\n" + date);
        setResult(RESULT_OK, data);
        finish();
    }
}