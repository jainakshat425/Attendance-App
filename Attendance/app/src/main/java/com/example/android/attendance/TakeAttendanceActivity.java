package com.example.android.attendance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.attendance.data.DatabaseHelper;
import com.example.android.attendance.data.AttendanceContract.AttendanceEntry;
import com.example.android.attendance.data.StudentContract.StudentEntry;

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

    private String NEW_COLUMN;

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
        int college = attendanceIntent.getIntExtra("EXTRA_COLLEGE", 0);
        String date = attendanceIntent.getStringExtra("EXTRA_DATE");
        String semester = attendanceIntent.getStringExtra("EXTRA_SEMESTER");
        String branch = attendanceIntent.getStringExtra("EXTRA_BRANCH");
        String section = attendanceIntent.getStringExtra("EXTRA_SECTION");
        String subject = attendanceIntent.getStringExtra("EXTRA_SUBJECT");
        String facUserId = attendanceIntent.getStringExtra("EXTRA_FACULTY_USER_ID");

        String collegeString = (college == 1) ? getString(R.string.college_gct)
                : getString(R.string.college_git);

        /**
         * populate the text views with the data from the intent
         */
        collegeTv.setText(collegeString);
        semesterTv.setText(semester);
        branchTv.setText(branch);
        sectionTv.setText(section);
        subjectTv.setText(subject);
        dateTv.setText(date);


        /**
         * open the database for getting the table of Students for Attendance
         */
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db;
        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            throw sqle;
        }

        String ATTENDANCE_TABLE = collegeString + "_" + semester + "_" + branch + "_" + section;

        if (!tableAlreadyExists(db, ATTENDANCE_TABLE)) {
            /**
             * CREATE Table
             */
            String CREATE_NEW_TABLE = "CREATE TABLE " + ATTENDANCE_TABLE + "("
                    + AttendanceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + AttendanceEntry.NAME_COL + " TEXT NOT NULL, "
                    + AttendanceEntry.ROLL_NO_COL + " TEXT NOT NULL UNIQUE)";
            db.execSQL(CREATE_NEW_TABLE);

            /**
             * get data from students table
             */
            String[] projection = {StudentEntry.S_NAME_COL, StudentEntry.S_ROLL_NO_COL};
            String selection = StudentEntry.S_COLLEGE_COL + "=?" + " and "
                    + StudentEntry.S_SEMESTER_COL + "=?" + " and "
                    + StudentEntry.S_BRANCH_COL + "=?" + " and "
                    + StudentEntry.S_SECTION_COL + "=?";
            String[] selectionArgs = {String.valueOf(college), semester, branch, section};
            Cursor studentData = db.query(StudentEntry.TABLE_NAME, projection,
                    selection, selectionArgs, null, null, null);

            /**
             * insert the student data from students table into New Attendance Table
             */
            if (studentData.getCount() > 0 && studentData.moveToFirst()) {

                ContentValues newStudentValues;
                int nameIndex;
                String name;
                int rollNoIndex;
                String rollNo;

                for (studentData.moveToFirst(); !studentData.isAfterLast();
                     studentData.moveToNext()) {
                    nameIndex = studentData.getColumnIndex(StudentEntry.S_NAME_COL);
                    name = studentData.getString(nameIndex);
                    rollNoIndex = studentData.getColumnIndex(StudentEntry.S_ROLL_NO_COL);
                    rollNo = studentData.getString(rollNoIndex);

                    newStudentValues = new ContentValues();
                    newStudentValues.put(AttendanceEntry.NAME_COL, name);
                    newStudentValues.put(AttendanceEntry.ROLL_NO_COL, rollNo);

                    db.insert(ATTENDANCE_TABLE, null, newStudentValues);
                }
            }
        }

        /**
         * ALTER Table
         */
        addNewAttendanceColumn(db, ATTENDANCE_TABLE, facUserId, subject, date);


        Cursor cursor = db.query(ATTENDANCE_TABLE, null, null,
                null, null, null,null);

        stdListView = findViewById(R.id.students_list_view);
        takeAttendanceAdapter = new TakeAttendanceAdapter(this, cursor, ATTENDANCE_TABLE,
                NEW_COLUMN);
        stdListView.setAdapter(takeAttendanceAdapter);
    }

    private void addNewAttendanceColumn(SQLiteDatabase db, String newTableName,
                                        String facUserId, String subject, String date) {

        String formattedDate = date.replace("-", "_");
        NEW_COLUMN = facUserId + "_" + subject + "_" + formattedDate;

        if ( columnAlreadyExists(db, newTableName, NEW_COLUMN)) {
            return;
        }
        String ADD_ATTENDANCE_COLUMN = "ALTER TABLE " + newTableName + " ADD COLUMN "
                + NEW_COLUMN + " INTEGER DEFAULT 0;";
        db.execSQL(ADD_ATTENDANCE_COLUMN);
        db.needUpgrade(db.getVersion() + 1);
    }

    private boolean columnAlreadyExists(SQLiteDatabase db, String newTableName, String newColumn) {
        try {
            Cursor tempCursor = db.query(newTableName, new String[] {newColumn}, null,
                    null, null, null, null);
        } catch (android.database.sqlite.SQLiteException e) {
            return false;
        }
        return true;
    }

    private boolean tableAlreadyExists(SQLiteDatabase db, String newTableName) {
        try {
            Cursor tempCursor = db.query(newTableName, null, null, null,
                    null, null, null);
        } catch (android.database.sqlite.SQLiteException e) {
            return false;
        }
       return true;
    }
}
