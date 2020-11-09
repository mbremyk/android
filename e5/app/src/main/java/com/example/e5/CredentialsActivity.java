package com.example.e5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CredentialsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
    }

    public void onBtnPlayClick(View v){
        TextView txtName = findViewById(R.id.txtName);
        TextView txtNumber = findViewById(R.id.txtNumber);
        if(txtName.getText().toString().isEmpty() || txtNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Du må fylle inn navn og kortnummer", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", txtName.getText().toString());
        intent.putExtra("card number", txtNumber.getText().toString());
        startActivity(intent);
    }
}