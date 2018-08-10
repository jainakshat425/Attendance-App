package com.example.android.attendance;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class FacultyContract {
    public static abstract class FacultyEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_FACULTY = "faculty";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_FACULTY);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_FACULTY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_FACULTY + "/#";

        public static final String TABLE_NAME = "faculty";

        public static final String _ID = BaseColumns._ID;
        public static final String F_NAME_COL = "fac_name";
        public static final String F_USERID_COL = "fac_user_id";
        public static final String F_PASSWORD_COL = "fac_password";
        public static final String F_DEPARTMENT_COL = "fac_department";

    }
}
