package com.example.android.attendance.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SubjectContract {
    public static abstract class SubjectEntry implements BaseColumns {


    public static final String TABLE_NAME = "subjects";

    public static final String _ID = BaseColumns._ID;
    public static final String SUB_NAME_COL = "sub_name";
    public static final String SUB_SEMESTER_COL = "sub_semester";
    public static final String SUB_BRANCH_COL = "sub_branch";
    public static final String SUB_FULL_NAME_COL = "sub_full_name";

    }
}