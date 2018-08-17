package com.example.android.attendance.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class StdAttendanceContract {
    public static abstract class StdAttendanceEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_STUDENTS_ATTENDANCE = "students_attendance";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_STUDENTS_ATTENDANCE);


        //to be used only when content provider is used
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS_ATTENDANCE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS_ATTENDANCE + "/#";

        //define constants for storing the table and attributes name
        public static final String TABLE_NAME = "students_attendance";

        public static final String _ID = BaseColumns._ID;
        public static final String S_NAME_COL = "std_name";
        public static final String S_ROLL_NO_COL = "std_roll_no";
        public static final String S_SEMESTER_COL = "std_semester";
        public static final String S_COLLEGE_COL = "std_college";
        public static final String S_BRANCH_COL = "std_branch";
        public static final String S_SECTION_COL = "std_section";
        public static final String S_FACULTY_TAKEN_COL = "std_faculty_taken";

        //constants representing college
        public static final int COLLEGE_GIT = 0;
        public static final int COLLEGE_GCT = 1;

        //constants representing branch
        public static final int BRANCH_CS = 0;
        public static final int BRANCH_ME = 1;
        public static final int BRANCH_IT = 2;
        public static final int BRANCH_EC = 3;
        public static final int BRANCH_CE = 4;
    }
}
