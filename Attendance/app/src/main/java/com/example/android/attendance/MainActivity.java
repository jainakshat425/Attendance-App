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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;
import com.example.android.attendance.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private String SELECTION = AttendanceRecordEntry.FACULTY_ID_COL + "=?";

    private TextView facNameTv;
    private TextView facDeptTv;

    private ListView mainListView;
    private MainListCursorAdapter cursorAdapter;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private static final int NEW_ATTENDANCE_REQUEST_CODE = 1;
    private static final int UPDATE_ATTENDANCE_REQ_CODE = 2;

    Bundle intentBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentBundle = getIntent().getExtras();

        String facName = intentBundle.getString("EXTRA_FACULTY_NAME");
        String facUserId = intentBundle.getString("EXTRA_FACULTY_USER_ID");
        String facDept = intentBundle.getString("EXTRA_FACULTY_DEPARTMENT");

        facNameTv = (TextView) findViewById(R.id.fac_name_tv);
        facDeptTv = (TextView) findViewById(R.id.fac_dept_tv);

        facNameTv.setText(facName);
        facDeptTv.setText(facDept);

        mainListView = findViewById(R.id.main_list_view);

        RelativeLayout emptyView = findViewById(R.id.empty_view_main);
        mainListView.setEmptyView(emptyView);

        /**
         * open the database for getting the records of attendance
         */
        databaseHelper = new DatabaseHelper(this);

        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            throw sqle;
        }


        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME, null, SELECTION,
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
                startActivityForResult(newAttendanceIntent, NEW_ATTENDANCE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME, null, SELECTION,
                new String[]{intentBundle.getString("EXTRA_FACULTY_USER_ID")},
                null, null, null);
        cursorAdapter.swapCursor(cursor);
        cursorAdapter.notifyDataSetChanged();

    }

    public static int getUpdateAttendanceReqCode() {
        return UPDATE_ATTENDANCE_REQ_CODE;
    }
}
