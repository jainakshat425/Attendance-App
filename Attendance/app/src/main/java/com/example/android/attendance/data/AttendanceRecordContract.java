package com.example.android.attendance.data;

import android.provider.BaseColumns;

public class AttendanceRecordContract {

    public class AttendanceRecordEntry implements BaseColumns {

        public static final String TABLE_NAME = "attendance_records";

        public static final String _ID = BaseColumns._ID;
        public static final String FACULTY_ID_COL = "fac_user_id";
        public static final String ATTENDANCE_TABLE_COL = "attendance_table_name";
        public static final String ATTENDANCE_COL = "attendance_col_name";
        public static final String TOTAL_STUDENTS_COL = "total_students";
        public static final String STUDENTS_PRESENT_COL = "students_present";
        public static final String COLLEGE_COL = "college";
        public static final String SEMESTER_COL = "semester";
        public static final String BRANCH_COL = "branch";
        public static final String SECTION_COL = "section";
        public static final String DATE_COL = "date";
        public static final String SUBJECT_COL = "subject";
        public static final String LECTURE_COL = "lecture";
        public static final String DAY_COL = "day";
    }
}
