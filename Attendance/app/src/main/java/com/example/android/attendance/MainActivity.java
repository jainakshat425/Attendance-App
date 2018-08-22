package com.example.android.attendance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;
import com.example.android.attendance.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private String[] PROJECTION = new String[]{AttendanceRecordEntry._ID,
            AttendanceRecordEntry.ATTENDANCE_TABLE_COL,
            AttendanceRecordEntry.ATTENDANCE_COL,
            AttendanceRecordEntry.STUDENTS_PRESENT_COL,
            AttendanceRecordEntry.TOTAL_STUDENTS_COL};
    private String SELECTION = AttendanceRecordEntry.FACULTY_ID_COL + "=?";

    private TextView facNameTv;
    private TextView facDeptTv;

    private ListView mainListView;
    private MainListCursorAdapter cursorAdapter;
    private Cursor cursor;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = getIntent();
        String facName = loginIntent.getStringExtra("EXTRA_FACULTY_NAME");
        String facUserId = loginIntent.getStringExtra("EXTRA_FACULTY_USER_ID");
        String facDept = loginIntent.getStringExtra("EXTRA_FACULTY_DEPARTMENT");

        facNameTv = (TextView) findViewById(R.id.fac_name_tv);
        facDeptTv = (TextView) findViewById(R.id.fac_dept_tv);

        facNameTv.setText(facName);
        facDeptTv.setText(facDept);

        mainListView = findViewById(R.id.main_list_view);

        /**
         * open the database for getting the records of attendance
         */
        databaseHelper = new DatabaseHelper(this);

        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            throw sqle;
        }


        cursor = db.query(AttendanceRecordEntry.TABLE_NAME, PROJECTION, SELECTION,
                new String[]{facUserId}, null, null, null);

        cursorAdapter = new MainListCursorAdapter(this, cursor);
        mainListView.setAdapter(cursorAdapter);
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
                        (MainActivity.this, NewAttendanceActivity.class);
                newAttendanceIntent.putExtra("EXTRA_FACULTY_USER_ID", facUserId);
                startActivityForResult(newAttendanceIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getLongExtra("EXTRA_FACULTY_USER_ID", 0) > 0) {
                Toast.makeText(this, "New Record Added.", Toast.LENGTH_SHORT).show();
            }
            cursor = db.query(AttendanceRecordEntry.TABLE_NAME, PROJECTION, SELECTION,
                    new String[]{data.getStringExtra("EXTRA_FACULTY_USER_ID")},
                    null, null, null);
            cursorAdapter.swapCursor(cursor);
        } else if(resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this,"No changes made!",Toast.LENGTH_SHORT).show();
        }
    }
}
