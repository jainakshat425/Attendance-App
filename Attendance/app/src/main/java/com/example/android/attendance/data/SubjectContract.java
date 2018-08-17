package com.example.android.attendance.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SubjectContract {
    public static abstract class SubjectEntry implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SUBJECTS = "subjects";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SUBJECTS);



    public static final String TABLE_NAME = "subjects";

    public static final String _ID = BaseColumns._ID;
    public static final String SUB_NAME_COL = "sub_name";
    public static final String SUB_SEMESTER_COL = "sub_semester";
    public static final String SUB_BRANCH_COL = "sub_branch";

    }
}