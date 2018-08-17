package com.example.android.attendance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.AttendanceContract.AttendanceEntry;
import com.example.android.attendance.data.DatabaseHelper;

public class TakeAttendanceAdapter extends CursorAdapter {

    private String ATTENDANCE_TABLE;
    private String NEW_COLUMN;

    public TakeAttendanceAdapter(Activity context, Cursor cursor, String attendanceTable,
                                 String newColumn) {
        super(context, cursor, 0);
        ATTENDANCE_TABLE = attendanceTable;
        NEW_COLUMN = newColumn;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.take_attendance_list_item,
                parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        int nameIndex = cursor.getColumnIndexOrThrow(AttendanceEntry.NAME_COL);
        String name = cursor.getString(nameIndex);
        TextView nameTv = (TextView) view.findViewById(R.id.name_text_view);
        nameTv.setText(name);

        int rollNoIndex = cursor.getColumnIndexOrThrow(AttendanceEntry.ROLL_NO_COL);
        String rollNo = cursor.getString(rollNoIndex);
        TextView rollNoTv = (TextView) view.findViewById(R.id.roll_no_text);
        rollNoTv.setText(rollNo);

        int attendanceIndex = cursor.getColumnIndexOrThrow(NEW_COLUMN);
        final int attendanceState = cursor.getInt(attendanceIndex);

        int idIndex = cursor.getColumnIndexOrThrow(AttendanceEntry._ID);
        final int id = cursor.getInt(idIndex);

        Switch present_switch = (Switch) view.findViewById(R.id.present_switch);

        if (attendanceState == 1) {
            present_switch.setChecked(true);
        }
        present_switch.setTag(id);
        present_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                changeAttendanceState(context, buttonView, isChecked);
            }
        });

    }

    private void changeAttendanceState(Context context, CompoundButton button, boolean isChecked) {

        int newAttendanceState = isChecked ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(NEW_COLUMN, newAttendanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.openDatabaseForReadWrite();

        String id = String.valueOf(button.getTag());
        String selection = AttendanceEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        int rowUpdated = db.update(ATTENDANCE_TABLE, values, selection, selectionArgs);
        if (rowUpdated <= 0) {
            Toast.makeText(context, "Error in Attendance", Toast.LENGTH_SHORT).show();
        }
    }
}
