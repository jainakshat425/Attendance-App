package com.example.android.attendance.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BranchContract {
    public static abstract class BranchEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_BRANCHES = "branches";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_BRANCHES);



        public static final String TABLE_NAME = "branches";

        public static final String _ID = BaseColumns._ID;
        public static final String B_NAME_COL = "b_name";

    }
}
