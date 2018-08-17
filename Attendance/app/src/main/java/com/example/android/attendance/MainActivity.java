package com.example.android.attendance;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView facNameTv;
    private TextView facUserIdTv;
    private TextView facDeptTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = getIntent();
        String facName = loginIntent.getStringExtra("EXTRA_FACULTY_NAME");
        String facUserId = loginIntent.getStringExtra("EXTRA_FACULTY_USER_ID");
        String facDept = loginIntent.getStringExtra("EXTRA_FACULTY_DEPARTMENT");

        facNameTv = (TextView) findViewById(R.id.fac_name_tv);
        facUserIdTv = (TextView) findViewById(R.id.fac_user_id_tv);
        facDeptTv = (TextView) findViewById(R.id.fac_dept_tv);

        facNameTv.setText(facName);
        facUserIdTv.setText(facUserId);
        facDeptTv.setText(facDept);

        setupFloatingActionButton(facUserId);
    }

    /**
     * initialise floatingActionButton and link it to newAttendanceActivity
     */
    private void setupFloatingActionButton(final String facUserId) {

        FloatingActionButton newAttendanceFab = findViewById(R.id.fab_main_activity);
        newAttendanceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAttendanceIntent = new Intent();
                newAttendanceIntent.setClass
                        (MainActivity.this,NewAttendanceActivity.class);
                newAttendanceIntent.putExtra("EXTRA_FACULTY_USER_ID", facUserId);
                startActivity(newAttendanceIntent);
            }
        });
    }
}
