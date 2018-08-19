package com.example.android.attendance;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView facNameTv;
    private TextView facUserIdTv;
    private TextView facDeptTv;

    private ListView mainListView;
    private ArrayList<NewAttendance> attendanceArrayList;
    private MainListAdapter mainListAdapter;
    private Bundle attendanceData = null;

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

        mainListView = findViewById(R.id.main_list_view);
        attendanceArrayList = new ArrayList<>();



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
                startActivityForResult(newAttendanceIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == Activity.RESULT_OK ) {
            Bundle extras = data.getExtras();
            updateAttendanceListMain(extras);
        }
    }

    private void updateAttendanceListMain(Bundle extras) {

        attendanceArrayList.add(new NewAttendance(extras));

        mainListAdapter = new MainListAdapter(this, attendanceArrayList);

        mainListView.setAdapter(mainListAdapter);

    }
}
