package com.example.android.attendance.data;

import android.provider.BaseColumns;

public class AttendanceContract {
    public static abstract class AttendanceEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String NAME_COL = "name";
        public static final String ROLL_NO_COL = "roll_no";
        public static final String TOTAL_ATTENDANCE_COL = "total_attendance";
    }
}
