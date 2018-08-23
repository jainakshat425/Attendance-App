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
        return LayoutInflater.from(context).inflate(R.layout.main_attendance_list_item,
                parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        Resources resources = context.getResources();

        /**
         * get column index from attendanceRecord table
         */
        int tableNameIndex = cursor.getColumnIndex(AttendanceRecordEntry.ATTENDANCE_TABLE_COL);
        int columnNameIndex = cursor.getColumnIndex(AttendanceRecordEntry.ATTENDANCE_COL);
        int stdPresentIndex = cursor.getColumnIndex(AttendanceRecordEntry.STUDENTS_PRESENT_COL);
        int totalStdIndex = cursor.getColumnIndex(AttendanceRecordEntry.TOTAL_STUDENTS_COL);
        int collegeIndex = cursor.getColumnIndex(AttendanceRecordEntry.COLLEGE_COL);
        int semesterIndex = cursor.getColumnIndex(AttendanceRecordEntry.SEMESTER_COL);
        int branchIndex = cursor.getColumnIndex(AttendanceRecordEntry.BRANCH_COL);
        int sectionIndex = cursor.getColumnIndex(AttendanceRecordEntry.SECTION_COL);
        int facUserIdIndex = cursor.getColumnIndex(AttendanceRecordEntry.FACULTY_ID_COL);
        int subjectIndex = cursor.getColumnIndex(AttendanceRecordEntry.SUBJECT_COL);
        int dateIndex = cursor.getColumnIndex(AttendanceRecordEntry.DATE_COL);
        int dayIndex = cursor.getColumnIndex(AttendanceRecordEntry.DAY_COL);
        int lectureIndex = cursor.getColumnIndex(AttendanceRecordEntry.LECTURE_COL);


        String tableName = cursor.getString(tableNameIndex);
        String columnName = cursor.getString(columnNameIndex);
        int stdPresent = cursor.getInt(stdPresentIndex);
        int totalStudents = cursor.getInt(totalStdIndex);
        int college = cursor.getInt(collegeIndex);
        int semester = cursor.getInt(semesterIndex);
        String branch = cursor.getString(branchIndex);
        String section = cursor.getString(sectionIndex);
        String facUserId = cursor.getString(facUserIdIndex);
        String subject = cursor.getString(subjectIndex);
        String date = cursor.getString(dateIndex);
        String day = cursor.getString(dayIndex);
        int lecture = cursor.getInt(lectureIndex);

        String collegeString = (college == 1) ? resources.getString(R.string.college_gct)
                : resources.getString(R.string.college_git);

        TextView collegeTv = (TextView) view.findViewById(R.id.college_tv);
        collegeTv.setText(collegeString);

        GradientDrawable collegeCircle = (GradientDrawable) collegeTv.getBackground();

        if (collegeString.equals(resources.getString(R.string.college_git))) {
            collegeCircle.setColor(ContextCompat.getColor(context, R.color.colorGit));
        } else {
            collegeCircle.setColor(ContextCompat.getColor(context, R.color.colorGct));
        }

        TextView semesterTv = (TextView) view.findViewById(R.id.semester_tv);
        setSemesterTv(semester, semesterTv);

        TextView branchTv = (TextView) view.findViewById(R.id.branch_tv);
        branchTv.setText(branch);

        TextView sectionTv = (TextView) view.findViewById(R.id.section_tv);
        sectionTv.setText(section);

        TextView subjectTv = (TextView) view.findViewById(R.id.subject_tv);
        subjectTv.setText(subject);

        TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
        dateTv.setText(date);

        TextView dayTv = (TextView) view.findViewById(R.id.day_tv);

        dayTv.setText(day.substring(0,3) + ",");

        TextView lectureTv = (TextView) view.findViewById(R.id.lecture_tv);
        setLectureTv(String.valueOf(lecture), lectureTv);

        TextView studentsPresentTv = (TextView) view.findViewById(R.id.students_present_tv);
        studentsPresentTv.setText(String.valueOf(stdPresent));

        TextView totalStudentsTv = (TextView) view.findViewById(R.id.total_students_tv);
        totalStudentsTv.setText(String.valueOf(totalStudents));


        int collegeSelected = (collegeString.equals(resources.getString(R.string.college_git))) ? 0 : 1;
        String[] projection = {AttendanceEntry._ID, AttendanceEntry.NAME_COL,
                AttendanceEntry.ROLL_NO_COL, columnName};

        intentBundle = new Bundle();
        intentBundle.putString("EXTRA_DATE", date);
        intentBundle.putString("EXTRA_SEMESTER", String.valueOf(semester));
        intentBundle.putString("EXTRA_BRANCH", branch);
        intentBundle.putString("EXTRA_SECTION", section);
        intentBundle.putString("EXTRA_SUBJECT", subject);
        intentBundle.putString("EXTRA_COLLEGE", String.valueOf(college));
        intentBundle.putString("EXTRA_FACULTY_USER_ID", facUserId);
        intentBundle.putString("EXTRA_LECTURE",String.valueOf(lecture));
        intentBundle.putString("EXTRA_DAY",day);
        intentBundle.putString("EXTRA_TABLE_NAME", tableName);
        intentBundle.putString("EXTRA_ATTENDANCE_COLUMN", columnName);
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

    private void setSemesterTv(int semester, TextView semesterTv) {
        if (semester == 1) {
            semesterTv.setText(String.valueOf(semester) + "st Sem");
        } else if (semester == 2) {
            semesterTv.setText(String.valueOf(semester) + "nd Sem");
        } else if (semester == 3) {
            semesterTv.setText(String.valueOf(semester) + "rd Sem");
        } else {
            semesterTv.setText(String.valueOf(semester) + "th Sem");
        }
    }

    private void setLectureTv(String lecture, TextView lectureTv) {
        if (Integer.parseInt(lecture) == 1) {
            lectureTv.setText(lecture + "st Lecture");
        } else if (Integer.parseInt(lecture) == 2) {
            lectureTv.setText(lecture + "nd Lecture");
        } else if (Integer.parseInt(lecture) == 3) {
            lectureTv.setText(lecture + "rd Lecture");
        } else {
            lectureTv.setText(lecture + "th Lecture");
        }
    }


}
