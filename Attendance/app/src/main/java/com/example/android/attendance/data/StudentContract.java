package com.example.android.attendance.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class StudentContract {
    public static abstract class StudentEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_STUDENTS = "students";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_STUDENTS);

        //define constants for storing the table and attributes name
        public static final String TABLE_NAME = "students";

        public static final String _ID = BaseColumns._ID;
        public static final String S_NAME_COL = "std_name";
        public static final String S_ROLL_NO_COL = "std_roll_no";
        public static final String S_SEMESTER_COL = "std_semester";
        public static final String S_COLLEGE_COL = "std_college";
        public static final String S_BRANCH_COL = "std_branch";
        public static final String S_SECTION_COL = "std_section";

        //constants representing college
        public static final int COLLEGE_GIT = 0;
        public static final int COLLEGE_GCT = 1;

    }
}
