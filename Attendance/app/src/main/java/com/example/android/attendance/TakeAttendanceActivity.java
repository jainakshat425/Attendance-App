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

import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;
import com.example.android.attendance.data.DatabaseHelper;
import com.example.android.attendance.data.AttendanceContract.AttendanceEntry;
import com.example.android.attendance.data.StudentContract.StudentEntry;

import java.util.ArrayList;


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
    private SQLiteDatabase db;
    private Cursor currentTableCursor;

    private Bundle bundle;

    private ArrayList<Integer> attendanceStatesList;


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

        //values which only exist when we activity is launched for updating attendance
        String existingTable = bundle.getString("EXTRA_TABLE_NAME");
        String[] existingProjection = bundle.getStringArray("EXTRA_ATTENDANCE_PROJECTION");
        String existingAttendanceColumn = bundle.getString("EXTRA_ATTENDANCE_COLUMN");


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
        dateTv.setText(date);

        /**
         * open the database for getting the table of Students for Attendance
         */
        databaseHelper = new DatabaseHelper(this);

        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            Log.e("TakeAttendanceActivity", "error opening database ", sqle);
            throw sqle;
        }

        /**
         * this condition implies that this is update mode of attendance
         */
        if (existingTable != null && existingAttendanceColumn != null &&
                existingProjection != null) {


            setTitle(getString(R.string.update_attendance_title));

            String orderBy = AttendanceEntry.ROLL_NO_COL + " ASC";

            currentTableCursor = db.query(existingTable, existingProjection, null,
                    null, null, null, orderBy);


            if (currentTableCursor.moveToFirst()) {

                //get attendance state of all the students
                int attendanceIndex = currentTableCursor.getColumnIndexOrThrow
                        (existingAttendanceColumn);
                int attendanceState;
                attendanceStatesList = new ArrayList<Integer>();
                currentTableCursor.moveToFirst();
                for (int i = 0; i < currentTableCursor.getCount(); i++) {
                    attendanceState = currentTableCursor.getInt(attendanceIndex);
                    // initializes all items value with their existing attendanceState
                    attendanceStatesList.add(i, attendanceState);
                    currentTableCursor.moveToNext();
                }

                takeAttendanceAdapter = new TakeAttendanceAdapter(this, currentTableCursor,
                        existingTable, existingAttendanceColumn, attendanceStatesList);
                stdListView.setAdapter(takeAttendanceAdapter);
            }


        } else if (semester != null && branch != null && section != null) {

            //build name for table
            String ATTENDANCE_TABLE = collegeString + "_" + semester + "_" + branch + "_" + section;
            bundle.putString("EXTRA_TABLE_NAME", ATTENDANCE_TABLE);

            /**
             *  check table if exist or not
             *  if exist alter table
             *  else create table and then alter table
             */

            if (!tableAlreadyExists(ATTENDANCE_TABLE)) {

                createTableWithData(ATTENDANCE_TABLE, college, semester, branch, section);
            }

            /**
             * ALTER Table
             */
            if (subject != null || date != null || facUserId != null) {

                String attendanceColumn = addNewAttendanceColumn(ATTENDANCE_TABLE, subject,
                        date, facUserId);

                bundle.putString("EXTRA_ATTENDANCE_COLUMN", attendanceColumn);

                String[] attendanceProjection = new String[]{AttendanceEntry._ID,
                        AttendanceEntry.NAME_COL, AttendanceEntry.ROLL_NO_COL, attendanceColumn};

                String orderBy = AttendanceEntry.ROLL_NO_COL + " ASC";

                currentTableCursor = db.query(ATTENDANCE_TABLE, attendanceProjection, null,
                        null, null, null, orderBy);


                attendanceStatesList = new ArrayList<Integer>();
                for (int i = 0; i < currentTableCursor.getCount(); i++) {
                    // initializes all items value with their existing attendanceState
                    attendanceStatesList.add(i, 0);
                }
                takeAttendanceAdapter = new TakeAttendanceAdapter(this, currentTableCursor,
                        ATTENDANCE_TABLE, attendanceColumn, attendanceStatesList);
                stdListView.setAdapter(takeAttendanceAdapter);
            } else {
                Toast.makeText(this, "Something went wrong.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            Log.e("TakeAttendanceActivity", "Column name issue");
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

    private void createTableWithData(String newTableName, String college,
                                     String semester, String branch, String section) {
        //CREATE Table
        String CREATE_NEW_TABLE = "CREATE TABLE " + newTableName + "("
                + AttendanceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AttendanceEntry.NAME_COL + " TEXT NOT NULL, "
                + AttendanceEntry.ROLL_NO_COL + " TEXT NOT NULL UNIQUE, "
                + AttendanceEntry.TOTAL_ATTENDANCE_COL + " INTEGER)";

        try {
            db.execSQL(CREATE_NEW_TABLE);
        } catch(android.database.sqlite.SQLiteException e) {

            Log.e("TakeAttendanceActivity","Table Already Exists",e);
        }


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

    private String addNewAttendanceColumn(String newTableName, String subject, String date,
                                          String facUserId) {

        String formattedDate = date.replace("-", "_");
        String NEW_COLUMN = facUserId + "_" + subject + "_" + formattedDate;

        if (!columnAlreadyExists(newTableName, NEW_COLUMN)) {

            String ADD_ATTENDANCE_COLUMN = "ALTER TABLE " + newTableName + " ADD COLUMN "
                    + NEW_COLUMN + " INTEGER DEFAULT 0;";

            try {
                db.execSQL(ADD_ATTENDANCE_COLUMN);
                db.needUpgrade(db.getVersion() + 1);
                insertAttendanceRecord(newTableName, NEW_COLUMN);
            } catch (android.database.sqlite.SQLiteException e) {
                Log.e("TakeAttendanceActivity", "Column Already Exists", e);
            }

        } else {
            Toast.makeText(this, "Attendance already Exists", Toast.LENGTH_SHORT).show();
        }
        return NEW_COLUMN;
    }

    private boolean columnAlreadyExists(String newTableName, String newColumn) {
        String selection = AttendanceRecordEntry.ATTENDANCE_TABLE_COL + "=?" + " and " +
                AttendanceRecordEntry.ATTENDANCE_COL + "=?";
        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME,
                new String[]{AttendanceRecordEntry._ID},
                selection,
                new String[]{newTableName, newColumn}, null, null, null);

        return (cursor.getCount() > 0);
    }


    private boolean tableAlreadyExists(String newTableName) {


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
        switch (item.getItemId()) {
            case R.id.done_take_attendance:

                updateAttendanceStates();

                updateAttendanceRecord();

                Intent intent = new Intent();
                intent.putExtras(bundle);

                setResult(Activity.RESULT_OK, intent);
                finish();

            case android.R.id.home :
                updateAttendanceRecord();
                setResult(Activity.RESULT_CANCELED);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAttendanceStates() {
        ContentValues attendanceStateValues;
        int attendanceState;
        String attendanceColumn = TakeAttendanceAdapter.getAttendanceColumn();
        String attendanceTable = TakeAttendanceAdapter.getAttendanceTableName();
        if (currentTableCursor.moveToFirst()) {
            currentTableCursor.moveToFirst();
            int idIndex = currentTableCursor.getColumnIndex(AttendanceEntry._ID);
            String currentId;
            for (int i = 0; i < currentTableCursor.getCount(); i++) {

                currentId = String.valueOf(currentTableCursor.getInt(idIndex));
                attendanceState = TakeAttendanceAdapter.getAttendanceState(i);

                attendanceStateValues = new ContentValues();
                attendanceStateValues.put(attendanceColumn, attendanceState);

                db.update(attendanceTable, attendanceStateValues,
                        AttendanceEntry._ID + "=?", new String[]{currentId});
                currentTableCursor.moveToNext();
            }
        }
    }

    @Override
    public void onBackPressed() {
        updateAttendanceRecord();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void updateAttendanceRecord() {

        String attendanceColumn = TakeAttendanceAdapter.getAttendanceColumn();
        String attendanceTable = TakeAttendanceAdapter.getAttendanceTableName();

        ContentValues record = new ContentValues();

        record.put(AttendanceRecordEntry.TOTAL_STUDENTS_COL, totalStudents(attendanceTable));
        record.put(AttendanceRecordEntry.STUDENTS_PRESENT_COL,
                studentsPresent(attendanceTable, attendanceColumn));

        String where = AttendanceRecordEntry.ATTENDANCE_TABLE_COL + "=?" + " and "
                + AttendanceRecordEntry.ATTENDANCE_COL + "=?";
        String[] whereArgs = new String[]{attendanceTable, attendanceColumn};

        int rowUpdated = db.update(AttendanceRecordEntry.TABLE_NAME, record, where, whereArgs);
        Toast.makeText(this, "Rows Updated: " + rowUpdated, Toast.LENGTH_SHORT).show();

    }

    private int totalStudents(String newTableName) {
        String[] projection = {AttendanceEntry._ID};
        Cursor totalStudents = db.query(newTableName, projection, null, null,
                null, null, null);
        return totalStudents.getCount();
    }

    private void insertAttendanceRecord(String newTableName,
                                        String attendanceColumn) {

        ContentValues record = new ContentValues();
        record.put(AttendanceRecordEntry.FACULTY_ID_COL,
                bundle.getString("EXTRA_FACULTY_USER_ID"));
        record.put(AttendanceRecordEntry.ATTENDANCE_TABLE_COL, newTableName);
        record.put(AttendanceRecordEntry.ATTENDANCE_COL, attendanceColumn);

        long rowId = db.insert(AttendanceRecordEntry.TABLE_NAME, null, record);
    }

    private int studentsPresent(String newTableName, String attendanceColumn) {
        String[] projection = {AttendanceEntry._ID};
        String selection = attendanceColumn + "=?";
        String[] selectionArgs = {String.valueOf(1)};
        Cursor studentsPresent = db.query(newTableName, projection, selection, selectionArgs,
                null, null, null);
        return studentsPresent.getCount();
    }
}
