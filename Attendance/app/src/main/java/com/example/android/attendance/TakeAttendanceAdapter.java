package com.example.android.attendance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.data.AttendanceContract.AttendanceEntry;
import com.example.android.attendance.data.DatabaseHelper;

import java.util.ArrayList;

public class TakeAttendanceAdapter extends CursorAdapter {

    private static String ATTENDANCE_TABLE;
    private static String NEW_COLUMN;
    private static ArrayList<Integer> attendanceStatesList;
    private int serialNo;

    public TakeAttendanceAdapter(Activity context, Cursor cursor, String attendanceTable,
                                 String newColumn, ArrayList<Integer> attendanceStates) {
        super(context, cursor, 0);
        ATTENDANCE_TABLE = attendanceTable;
        NEW_COLUMN = newColumn;
        attendanceStatesList = attendanceStates;

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

        int position = cursor.getPosition();

        serialNo = position;
        TextView serialTv = (TextView) view.findViewById(R.id.serial_no_tv);
        serialTv.setText(String.valueOf(serialNo+1));

        CheckBox presentCheckbox = view.findViewById(R.id.present_checkbox);

        presentCheckbox.setTag(position);

        presentCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(String.valueOf(v.getTag()));
                /*changeAttendanceState(context, buttonView, isChecked);*/
                if (attendanceStatesList.get(position) == 1) {
                    attendanceStatesList.set(position, 0);
                } else {
                    attendanceStatesList.set(position, 1);
                }

            }
        });
        if (attendanceStatesList.get(position) == 1) {
            presentCheckbox.setChecked(true);
        } else {
            presentCheckbox.setChecked(false);
        }
    }

    public static int getAttendanceState(int i) {
        return attendanceStatesList.get(i);
    }

    public static String getAttendanceColumn() {
        return NEW_COLUMN;
    }

    public static String getAttendanceTableName() {
        return ATTENDANCE_TABLE;
    }
}
