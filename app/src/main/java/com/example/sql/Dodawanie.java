package com.example.sql;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Dodawanie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodawanie);

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                EditText imie = findViewById(R.id.imie);
                EditText nazwisko = findViewById(R.id.nazwisko);
                intent.putExtra("Imie", imie.getText());
                intent.putExtra("Nazwisko", nazwisko.getText());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}