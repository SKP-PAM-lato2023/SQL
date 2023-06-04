package com.example.sql;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    // klucz dla kazdego z rekordu
    private long id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("Kurs", MODE_PRIVATE, null);
        String sqlDB = "CREATE TABLE IF NOT EXISTS Sluchacze (Id INTEGER, Imie VARCHAR, Nazwisko VARCHAR)";
        db.execSQL(sqlDB);

        insertIntoDB();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> wyniki = new ArrayList<String>();
                Cursor c = db.rawQuery("SELECT Id, Imie, Nazwisko FROM Sluchacze", null);
                if(c.moveToFirst()){
                    do{
                        int id = c.getInt(c.getColumnIndexOrThrow("Id"));
                        String imie = c.getString(c.getColumnIndexOrThrow("Imie"));
                        String nazwisko = c.getString(c.getColumnIndexOrThrow("Nazwisko"));
                        wyniki.add(String.format("%d. %s %s", id, imie, nazwisko));
                    }while(c.moveToNext());
                }
                c.close();

                ListView listView = findViewById(R.id.listView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                        android.R.layout.simple_list_item_1, wyniki);
                listView.setAdapter(adapter);
            }
        });

        ActivityResultLauncher<Intent> getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // pobieram dane
                        Intent intent = result.getData();
                        if(intent != null){
                            CharSequence imie = intent.getCharSequenceExtra("Imie");
                            CharSequence nazwisko = intent.getCharSequenceExtra("Nazwisko");
                            String sqlStatment = "INSERT INTO Sluchacze VALUES (?, ?, ?)";
                            SQLiteStatement statement = db.compileStatement(sqlStatment);
                            statement.bindLong(1, id++);
                            statement.bindString(2, imie.toString());
                            statement.bindString(3, nazwisko.toString());
                            statement.executeInsert();
                            // klikam przycisk
                            button.callOnClick();

                        }
                    }
                }
        );

        Button dodawanie = findViewById(R.id.button2);
        dodawanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Dodawanie.class);
                getResult.launch(intent);
            }
        });

    }

    private void insertIntoDB(){
        String sqlCount = "SELECT COUNT(*) FROM Sluchacze";
        Cursor cursor = db.rawQuery(sqlCount, null);
        cursor.moveToFirst();
        int ilosc = cursor.getInt(0);
        cursor.close();

        if(ilosc == 0){
            String sqlStatment = "INSERT INTO Sluchacze VALUES (?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sqlStatment);

            statement.bindLong(1,id++);
            statement.bindString(2, "Jan");
            statement.bindString(3, "Kowalski");
            statement.executeInsert();

            statement.bindLong(1,id++);
            statement.bindString(2, "Anna");
            statement.bindString(3, "Nowak");
            statement.executeInsert();
        }else{
            id = ilosc+1;
        }
    }
}