package com.example.android.attendance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.AttendanceContract;
import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;
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

    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        /**
         * initialize all text views
         */
        initializeAllViews();
        /**
         * get data from intent i.e. the activity which called this..
         */
        bundle = getIntent().getExtras();
        String college = bundle.getString("EXTRA_COLLEGE");
        String date = bundle.getString("EXTRA_DATE");
        String day = bundle.getString("EXTRA_DAY");
        String semester = bundle.getString("EXTRA_SEMESTER");
        String branch = bundle.getString("EXTRA_BRANCH");
        String section = bundle.getString("EXTRA_SECTION");
        String subject = bundle.getString("EXTRA_SUBJECT");
        String facUserId = bundle.getString("EXTRA_FACULTY_USER_ID");

        String collegeString = (Integer.parseInt(college) == 1) ? getString(R.string.college_gct)
                : getString(R.string.college_git);

        /**
         * populate the text views with the data from the intent
         */
        collegeTv.setText(collegeString);
        semesterTv.setText(semester);
        branchTv.setText(branch);
        sectionTv.setText(section);
        subjectTv.setText(subject);
        dateTv.setText(day + ", " + date);

        /**
         * open the database for getting the table of Students for Attendance
         */
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db;
        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            Log.e("TakeAttendanceActivity","error opening database " ,sqle);
            throw sqle;
        }

        if (bundle.containsKey("EXTRA_TABLE_NAME") &&
                bundle.containsKey("EXTRA_ATTENDANCE_PROJECTION") &&
                bundle.containsKey("EXTRA_ATTENDANCE_COLUMN")) {

            String existingTable = bundle.getString("EXTRA_TABLE_NAME");
            String[] existingProjection = bundle.getStringArray("EXTRA_ATTENDANCE_PROJECTION");
            String existingAttendanceColumn = bundle.getString("EXTRA_ATTENDANCE_COLUMN");
            String orderBy = AttendanceEntry.ROLL_NO_COL + " ASC";
            Cursor cursor = db.query(existingTable, existingProjection, null,
                    null, null, null, orderBy);

            takeAttendanceAdapter = new TakeAttendanceAdapter(this, cursor,
                    existingTable, existingAttendanceColumn);
            stdListView.setAdapter(takeAttendanceAdapter);

        } else if(collegeString != null && semester != null && branch != null && section != null){

            //build name for table
            String ATTENDANCE_TABLE = collegeString + "_" + semester + "_" + branch + "_" + section;
            bundle.putString("EXTRA_TABLE_NAME", ATTENDANCE_TABLE);

            /**
             *  check table if exist or not
             *  if exist alter table
             *  else create table and then alter table
             */

            if (!tableAlreadyExists(db, ATTENDANCE_TABLE)) {

                createTableWithData(db, ATTENDANCE_TABLE, college, semester, branch, section);
            }

            /**
             * ALTER Table
             */
            if (subject != null || date != null || facUserId != null) {

                String attendanceColumn = addNewAttendanceColumn(db,
                        ATTENDANCE_TABLE, subject, date, facUserId);

                bundle.putString("EXTRA_ATTENDANCE_COLUMN", attendanceColumn);

                String[] attendanceProjection = new String[]{AttendanceEntry._ID, AttendanceEntry.NAME_COL,
                        AttendanceEntry.ROLL_NO_COL, attendanceColumn};
                String orderBy = AttendanceEntry.ROLL_NO_COL + " ASC";
                Cursor cursor = db.query(ATTENDANCE_TABLE, attendanceProjection, null,
                        null, null, null, orderBy);

                bundle.putInt("EXTRA_TOTAL_STUDENTS", cursor.getCount());

                if (cursor.getColumnIndex(attendanceColumn) != -1) {
                    takeAttendanceAdapter = new TakeAttendanceAdapter(this, cursor,
                            ATTENDANCE_TABLE, attendanceColumn);
                    stdListView.setAdapter(takeAttendanceAdapter);
                } else {
                    Toast.makeText(this, "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.e("TakeAttendanceActivity", "Column name issue");
            }
        }
    }

    private void initializeAllViews() {
        collegeTv = (TextView) findViewById(R.id.college_text_view);
        semesterTv = (TextView) findViewById(R.id.semester_text_view);
        branchTv = (TextView) findViewById(R.id.branch_text_view);
        sectionTv = (TextView) findViewById(R.id.section_text_view);
        subjectTv = (TextView) findViewById(R.id.subject_text_view);
        dateTv = (TextView) findViewById(R.id.date_text_view);

        stdListView = findViewById(R.id.students_list_view);
    }

    private void createTableWithData(SQLiteDatabase db, String newTableName, String college,
                                     String semester, String branch, String section) {
        //CREATE Table
        String CREATE_NEW_TABLE = "CREATE TABLE " + newTableName + "("
                + AttendanceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AttendanceEntry.NAME_COL + " TEXT NOT NULL, "
                + AttendanceEntry.ROLL_NO_COL + " TEXT NOT NULL UNIQUE, "
                + AttendanceEntry.TOTAL_ATTENDANCE_COL + " INTEGER)";
        db.execSQL(CREATE_NEW_TABLE);

        /**
         * get data from students table
         */
        String[] stdProjection = {StudentEntry.S_NAME_COL, StudentEntry.S_ROLL_NO_COL};
        String selection = StudentEntry.S_COLLEGE_COL + "=?" + " and "
                + StudentEntry.S_SEMESTER_COL + "=?" + " and "
                + StudentEntry.S_BRANCH_COL + "=?" + " and "
                + StudentEntry.S_SECTION_COL + "=?";
        String[] selectionArgs = {college, semester, branch, section};

        Cursor studentData = db.query(StudentEntry.TABLE_NAME, stdProjection,
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
                db.insert(newTableName, null, newStudentValues);
            }
        }
    }

    private String addNewAttendanceColumn(SQLiteDatabase db, String newTableName,
                                          String subject, String date, String facUserId) {

        String formattedDate = date.replace("-", "_");
        String NEW_COLUMN = facUserId + "_" + subject + "_" + formattedDate;

        if (!columnAlreadyExists(db, newTableName, NEW_COLUMN)) {

            String ADD_ATTENDANCE_COLUMN = "ALTER TABLE " + newTableName + " ADD COLUMN "
                    + NEW_COLUMN + " INTEGER DEFAULT 0;";
            db.execSQL(ADD_ATTENDANCE_COLUMN);
            db.needUpgrade(db.getVersion() + 1);
            insertAttendanceRecord(db, newTableName, NEW_COLUMN);
        } else {
            Toast.makeText(this,"Attendance already Exists", Toast.LENGTH_SHORT).show();
        }
        return NEW_COLUMN;
    }

    private boolean columnAlreadyExists(SQLiteDatabase db, String newTableName, String newColumn) {
        String selection = AttendanceRecordEntry.ATTENDANCE_TABLE_COL + "=?" + " and " +
                AttendanceRecordEntry.ATTENDANCE_COL + "=?";
        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME,
                new String[]{AttendanceRecordEntry._ID},
                selection,
                new String[]{newTableName,newColumn}, null, null, null);

        return (cursor.getCount() > 0);
    }


    private boolean tableAlreadyExists(SQLiteDatabase db, String newTableName) {
        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME,
                new String[]{AttendanceRecordEntry._ID},
                AttendanceRecordEntry.ATTENDANCE_TABLE_COL + "=?",
                new String[]{newTableName}, null, null, null);

        return (cursor.getCount() > 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_take_attendance_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done_take_attendance) {
            updateAttendanceRecord();
            Intent intent = new Intent();
            intent.putExtra("EXTRA_FACULTY_USER_ID",
                    bundle.getString("EXTRA_FACULTY_USER_ID"));
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAttendanceRecord() {
        SQLiteDatabase db;
        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            Log.e("TakeAttendanceActivity","error opening database " ,sqle);
            throw sqle;
        }

        String tableName = bundle.getString("EXTRA_TABLE_NAME");
        String attendanceColumn = bundle.getString("EXTRA_ATTENDANCE_COLUMN");
        int totalStudents = bundle.getInt("EXTRA_TOTAL_STUDENTS");
        ContentValues record = new ContentValues();
        record.put(AttendanceRecordEntry.STUDENTS_PRESENT_COL,
                studentsPresent(db, tableName, attendanceColumn));
        record.put(AttendanceRecordEntry.TOTAL_STUDENTS_COL, totalStudents);

        String where = AttendanceRecordEntry.ATTENDANCE_TABLE_COL + "=?" + " and "
                + AttendanceRecordEntry.ATTENDANCE_COL + "=?";
        String[] whereArgs = new String[] {tableName, attendanceColumn};

        int rowUpdated = db.update(AttendanceRecordEntry.TABLE_NAME, record, where, whereArgs);
        Toast.makeText(this,"Rows Updated: "+rowUpdated,Toast.LENGTH_SHORT).show();

    }

    private void insertAttendanceRecord(SQLiteDatabase db, String newTableName,
                                        String attendanceColumn) {

        ContentValues record = new ContentValues();
        record.put(AttendanceRecordEntry.FACULTY_ID_COL,
                bundle.getString("EXTRA_FACULTY_USER_ID"));
        record.put(AttendanceRecordEntry.ATTENDANCE_TABLE_COL, newTableName);
        record.put(AttendanceRecordEntry.ATTENDANCE_COL, attendanceColumn);

        long rowId = db.insert(AttendanceRecordEntry.TABLE_NAME, null, record);
    }

    private int studentsPresent(SQLiteDatabase db, String newTableName, String attendanceColumn) {
        String[] projection = {AttendanceEntry._ID, AttendanceEntry.NAME_COL,
                AttendanceEntry.ROLL_NO_COL, attendanceColumn};
        String selection = attendanceColumn + "=?";
        String[] selectionArgs = {String.valueOf(1)};
        Cursor studentsPresent = db.query(newTableName, projection, selection, selectionArgs,
                null, null, null);
        return studentsPresent.getCount();
    }
}
