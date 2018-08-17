package com.example.android.attendance.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class FacultyContract {
    public static abstract class FacultyEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.android.attendance";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_FACULTIES = "faculties";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_FACULTIES);


        public static final String TABLE_NAME = "faculties";

        public static final String _ID = BaseColumns._ID;
        public static final String F_NAME_COL = "fac_name";
        public static final String F_USER_ID_COL = "fac_user_id";
        public static final String F_PASSWORD_COL = "fac_password";
        public static final String F_DEPARTMENT_COL = "fac_department";

    }
}
