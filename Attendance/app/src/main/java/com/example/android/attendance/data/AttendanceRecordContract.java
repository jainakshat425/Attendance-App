package com.example.android.attendance.data;

import android.provider.BaseColumns;

public class AttendanceRecordContract {

    public class AttendanceRecordEntry implements BaseColumns {

        public static final String TABLE_NAME = "attendance_records";

        public static final String _ID = BaseColumns._ID;
        public static final String FACULTY_ID_COL = "fac_user_id";
        public static final String ATTENDANCE_TABLE_COL = "col_sem_br_sec";
        public static final String ATTENDANCE_COL = "fac_sub_date";
        public static final String TOTAL_STUDENTS_COL = "total_students";
        public static final String STUDENTS_PRESENT_COL = "students_present";
    }
}
