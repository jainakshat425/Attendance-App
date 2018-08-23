package com.example.android.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.attendance.data.AttendanceContract.AttendanceEntry;
import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;

public class MainListCursorAdapter extends CursorAdapter {


    private Bundle intentBundle;

    public MainListCursorAdapter(Activity context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate( R.layout.main_attendance_list_item,
                parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        Resources resources = context.getResources();

        int tableNameIndex = cursor.getColumnIndexOrThrow(AttendanceRecordEntry.ATTENDANCE_TABLE_COL);
        int columnNameIndex = cursor.getColumnIndexOrThrow(AttendanceRecordEntry.ATTENDANCE_COL);
        int stdPresentIndex = cursor.getColumnIndexOrThrow(AttendanceRecordEntry.STUDENTS_PRESENT_COL);
        int totalStdIndex = cursor.getColumnIndexOrThrow(AttendanceRecordEntry.TOTAL_STUDENTS_COL);

        String tableName = cursor.getString(tableNameIndex);
        String columnName = cursor.getString(columnNameIndex);
        int stdPresent = cursor.getInt(stdPresentIndex);
        int totalStudents = cursor.getInt(totalStdIndex);

        String[] splittedTableName = tableName.split("_",4);
        String[] splittedColumnName = columnName.split("_",3);

        String college = splittedTableName[0];
        String semester = splittedTableName[1];
        String branch = splittedTableName[2];
        String section = splittedTableName[3];

        String facUserId = splittedColumnName[0];
        String subject = splittedColumnName[1];
        String date = splittedColumnName[2].replace("_","-");


        TextView collegeTv = (TextView) view.findViewById(R.id.college_tv);
        collegeTv.setText(college);

        GradientDrawable collegeCircle = (GradientDrawable) collegeTv.getBackground();

        if (college.equals(resources.getString(R.string.college_git))) {
            collegeCircle.setColor(ContextCompat.getColor(context, R.color.colorGit));
        } else {
            collegeCircle.setColor(ContextCompat.getColor(context, R.color.colorGct));
        }

        TextView semesterTv = (TextView) view.findViewById(R.id.semester_tv);
        semesterTv.setText(semester + "th Sem");

        TextView branchTv = (TextView) view.findViewById(R.id.branch_tv);
        branchTv.setText(branch);

        TextView sectionTv = (TextView) view.findViewById(R.id.section_tv);
        sectionTv.setText(section);

        TextView subjectTv = (TextView) view.findViewById(R.id.subject_tv);
        subjectTv.setText(subject);

        TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
        dateTv.setText(date);

        TextView studentsPresentTv = (TextView) view.findViewById(R.id.students_present_tv);
        studentsPresentTv.setText(String.valueOf(stdPresent));

        TextView totalStudentsTv = (TextView) view.findViewById(R.id.total_students_tv);
        totalStudentsTv.setText(String.valueOf(totalStudents));


        int collegeSelected = (college.equals(resources.getString(R.string.college_git))) ? 0 : 1;
        String[] projection = {AttendanceEntry._ID, AttendanceEntry.NAME_COL,
                AttendanceEntry.ROLL_NO_COL, columnName};

        intentBundle = new Bundle();
        intentBundle.putString("EXTRA_DATE", date);
        intentBundle.putString("EXTRA_SEMESTER", semester);
        intentBundle.putString("EXTRA_BRANCH", branch);
        intentBundle.putString("EXTRA_SECTION", section);
        intentBundle.putString("EXTRA_SUBJECT", subject);
        intentBundle.putString("EXTRA_COLLEGE", String.valueOf(collegeSelected));
        intentBundle.putString("EXTRA_FACULTY_USER_ID", facUserId);
        intentBundle.putString("EXTRA_TABLE_NAME",tableName);
        intentBundle.putString("EXTRA_ATTENDANCE_COLUMN",columnName);
        intentBundle.putStringArray("EXTRA_ATTENDANCE_PROJECTION", projection);

        view.setTag(intentBundle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeAttendanceIntent = new Intent();
                takeAttendanceIntent.setClass(context, TakeAttendanceActivity.class);
                takeAttendanceIntent.putExtras((Bundle) v.getTag());
                ((Activity) context).startActivityForResult(takeAttendanceIntent,
                        MainActivity.getUpdateAttendanceReqCode());
            }
        });

    }

}
