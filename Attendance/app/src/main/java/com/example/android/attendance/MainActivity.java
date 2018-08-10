package com.example.android.attendance;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialise floatingActionButton and link it to newAttendanceActivity
        FloatingActionButton newAttendanceFab = findViewById(R.id.fab_main_activity);
        newAttendanceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAttendanceIntent = new Intent();
                newAttendanceIntent.setClass
                        (MainActivity.this,NewAttendanceActivity.class);
                startActivity(newAttendanceIntent);
            }
        });
    }
}
