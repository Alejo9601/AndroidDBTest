package com.example.testbasedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etAccount, etUserEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAccount = (EditText) findViewById(R.id.etAccount);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    public void handleBtnCreate (View view) {

        String account = etAccount.getText().toString();
        String userEmail = etUserEmail.getText().toString();
        String pass = etPassword.getText().toString();

        if(account.isEmpty() || userEmail.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "storepass", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        ContentValues records = new ContentValues();

        records.put("account", account);
        records.put("username", userEmail);
        records.put("password", pass);

        database.insert("passwords", null, records);
        database.close();

        etAccount.setText("");
        etUserEmail.setText("");
        etPassword.setText("");

        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
    }

    public void handleSearch(View view) {

        String accName = etAccount.getText().toString();

        if(accName.isEmpty()) {
            Toast.makeText(this, "Please insert account name", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "storepass", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        Cursor rowRecord = database.rawQuery("select username, password from passwords where account = ?",new String[] {accName});

        if(!rowRecord.moveToFirst()) {
            Toast.makeText(this, "Sorry, that account does not exists", Toast.LENGTH_SHORT).show();
            database.close();
            return;
        }

        etUserEmail.setText(rowRecord.getString(0));
        etPassword.setText(rowRecord.getString(1));

        database.close();

    }

}