package com.example.android.attendance.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.attendance.data.FacultyContract.FacultyEntry;
import com.example.android.attendance.data.StudentContract.StudentEntry;

public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Attendance.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + StudentEntry.TABLE_NAME
            + "("
            + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + StudentEntry.S_NAME_COL + " TEXT NOT NULL,"
            + StudentEntry.S_ROLLNO_COL + " TEXT NOT NULL,"
            + StudentEntry.S_COLLEGE_COL + " INTEGER NOT NULL,"
            + StudentEntry.S_SEMESTER_COL + " INTEGER NOT NULL,"
            + StudentEntry.S_BRANCH_COL + " INTEGER NOT NULL,"
            + StudentEntry.S_SECTION_COL + " TEXT NOT NULL"
            + ");";

    public static final String CREATE_TABLE_FACULTY = "CREATE TABLE " + FacultyEntry.TABLE_NAME
            + "("
            + FacultyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FacultyEntry.F_NAME_COL + " TEXT NOT NULL,"
            + FacultyEntry.F_USERID_COL + " TEXT NOT NULL,"
            + FacultyEntry.F_PASSWORD_COL + " TEXT NOT NULL,"
            + FacultyEntry.F_DEPARTMENT_COL + " INTEGER"
            + ");";


    public DbOpenHelper (Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FACULTY);
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
