package com.example.android.attendance;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.DatabaseHelper;
import com.example.android.attendance.data.StdAttendanceContract;
import com.example.android.attendance.data.StdAttendanceContract.StdAttendanceEntry;

public class TakeAttendanceAdapter extends CursorAdapter {

    public String newAttendanceDate = "thursday_15_08_2018";

    public TakeAttendanceAdapter(Activity context, Cursor cursor) {
        super(context, cursor, 0);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.take_attendance_list_item,
                parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        int nameIndex = cursor.getColumnIndexOrThrow(StdAttendanceEntry.S_NAME_COL);
        String name = cursor.getString(nameIndex);
        TextView nameTv = (TextView) view.findViewById(R.id.name_text_view);
        nameTv.setText(name);

        int rollNoIndex = cursor.getColumnIndexOrThrow(StdAttendanceEntry.S_ROLL_NO_COL);
        String rollNo = cursor.getString(rollNoIndex);
        TextView rollNoTv = (TextView) view.findViewById(R.id.roll_no_text);
        rollNoTv.setText(rollNo);

        int attendanceIndex = cursor.getColumnIndexOrThrow(newAttendanceDate);
        final int attendanceState = cursor.getInt(attendanceIndex);

        int idIndex = cursor.getColumnIndexOrThrow(StdAttendanceEntry._ID);
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
        values.put(newAttendanceDate, newAttendanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.openDatabaseForUpdate();

        String id = String.valueOf(button.getTag());
        String selection = StdAttendanceEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        int rowUpdated = db.update(StdAttendanceEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowUpdated <= 0) {
            Toast.makeText(context, "Error in Attendance", Toast.LENGTH_SHORT).show();
        }
    }
}
