package com.example.android.attendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.DatabaseHelper;
import com.example.android.attendance.data.FacultyContract.FacultyEntry;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView needHelpLink;

    private int attempts = 5;
    private  DatabaseHelper myDbHelper;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        needHelpLink = findViewById(R.id.need_help_link);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        needHelpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent needHelpIntent = new Intent(Intent.ACTION_SEND);
                needHelpIntent.setType("text/html");
                needHelpIntent.putExtra(Intent.EXTRA_SUBJECT,"Need Help");
                needHelpIntent.putExtra(Intent.EXTRA_TEXT,"Describe the problem");
                startActivity(Intent.createChooser(needHelpIntent,"Send Email..."));
            }
        });


        myDbHelper = new DatabaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }


    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        SQLiteDatabase db;
        try {
            db = myDbHelper.openDataBaseReadOnly();
        } catch (SQLException sqle) {
            throw sqle;
        }

        String selection = FacultyEntry.F_USER_ID_COL + "=?" + " and " +
                FacultyEntry.F_PASSWORD_COL + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(FacultyEntry.TABLE_NAME, null, selection, selectionArgs,
                null,null,null);

        if ( cursor == null || !cursor.moveToFirst()) {
            Toast.makeText(this,"Account doesn't Exist", Toast.LENGTH_SHORT ).show();
            cursor.close();
        }
        else {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FacultyEntry.F_NAME_COL));
            String dept = cursor.getString(cursor.getColumnIndexOrThrow
                    (FacultyEntry.F_DEPARTMENT_COL));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow
                    (FacultyEntry.F_USER_ID_COL));

            Intent mainIntent = new Intent();
            mainIntent.setClass(this,MainActivity.class);
            mainIntent.putExtra("EXTRA_FACULTY_NAME",name);
            mainIntent.putExtra("EXTRA_FACULTY_USER_ID",userId);
            mainIntent.putExtra("EXTRA_FACULTY_DEPARTMENT",dept);
            startActivity(mainIntent);
        }
    }
}
