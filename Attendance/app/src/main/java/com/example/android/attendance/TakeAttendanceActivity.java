package com.example.android.attendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.attendance.data.DatabaseHelper;
import com.example.android.attendance.data.StdAttendanceContract;
import com.example.android.attendance.data.StdAttendanceContract.StdAttendanceEntry;

public class TakeAttendanceActivity extends AppCompatActivity {

    private TextView collegeTv;
    private TextView semesterTv;
    private TextView branchTv;
    private TextView sectionTv;
    private TextView subjectTv;
    private TextView dateTv;

    private ListView stdListView;
    private TakeAttendanceAdapter takeAttendanceAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        /**
         * initialize all text views
         */
        collegeTv = (TextView) findViewById(R.id.college_text_view);
        semesterTv = (TextView) findViewById(R.id.semester_text_view);
        branchTv = (TextView) findViewById(R.id.branch_text_view);
        sectionTv = (TextView) findViewById(R.id.section_text_view);
        subjectTv = (TextView) findViewById(R.id.subject_text_view);
        dateTv = (TextView) findViewById(R.id.date_text_view);

        /**
         * get data from intent i.e. the activity which called this..
         */
        Intent attendanceIntent = getIntent();
        int college = attendanceIntent.getIntExtra("EXTRA_COLLEGE",0);
        String date = attendanceIntent.getStringExtra("EXTRA_DATE");
        String semester = attendanceIntent.getStringExtra("EXTRA_SEMESTER");
        String branch = attendanceIntent.getStringExtra("EXTRA_BRANCH");
        String section = attendanceIntent.getStringExtra("EXTRA_SECTION");
        String subject = attendanceIntent.getStringExtra("EXTRA_SUBJECT");

        /**
         * populate the text views with the data from the intent
         */
        collegeTv.setText(( college == 1) ? getString(R.string.college_gct)
                : getString(R.string.college_git) );
        semesterTv.setText(semester);
        branchTv.setText(branch);
        sectionTv.setText(section);
        subjectTv.setText(subject);
        dateTv.setText(date);


        /**
         * open the database for populating the list view with list of students
         */
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db;
        try {
            db = databaseHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        stdListView = findViewById(R.id.students_list_view);

       String selection = StdAttendanceEntry.S_COLLEGE_COL + "=?" + " and "
                + StdAttendanceEntry.S_SEMESTER_COL + "=?" + " and "
                + StdAttendanceEntry.S_BRANCH_COL + "=?" + " and "
                + StdAttendanceEntry.S_SECTION_COL + "=?";

        String[] selectionArgs = {String.valueOf(StdAttendanceEntry.COLLEGE_GIT), "5",
                String.valueOf(StdAttendanceEntry.BRANCH_CS), "A"};

        Cursor cursor = db.query(StdAttendanceEntry.TABLE_NAME, null, selection,
                selectionArgs, null, null,null);

        takeAttendanceAdapter = new TakeAttendanceAdapter(this, cursor);
        stdListView.setAdapter(takeAttendanceAdapter);
    }
}
