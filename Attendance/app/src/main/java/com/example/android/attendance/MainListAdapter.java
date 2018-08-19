package com.example.android.attendance;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<NewAttendance> {

    Activity mainActivity;

    public MainListAdapter(Activity context, ArrayList<NewAttendance> newAttendance) {
        super(context, 0, newAttendance);
        this.mainActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.main_attendance_list_item, parent, false);
        }

        NewAttendance currentNewAttendance = getItem(position);

        final Bundle attendanceBundle = currentNewAttendance.getmBundle();

        int college = attendanceBundle.getInt("EXTRA_COLLEGE");
        int semester = Integer.parseInt(attendanceBundle.getString("EXTRA_SEMESTER"));
        String branch = attendanceBundle.getString("EXTRA_BRANCH");
        String section = attendanceBundle.getString("EXTRA_SECTION");
        String subject = attendanceBundle.getString("EXTRA_SUBJECT");
        String date = attendanceBundle.getString("EXTRA_DATE");
        String faculty = attendanceBundle.getString("EXTRA_FACULTY");
        String tableName = attendanceBundle.getString("EXTRA_TABLE_NAME");
        String[] projection = attendanceBundle.getStringArray("EXTRA_ATTENDANCE_PROJECTION");

        Resources stringResources = getContext().getResources();
        String collegeString = (college == 1) ?
                stringResources.getString(R.string.college_gct) :
                stringResources.getString(R.string.college_git);

        TextView collegeTv = (TextView) listItemView.findViewById(R.id.college_tv);
        collegeTv.setText(collegeString);

        TextView semesterTv = (TextView) listItemView.findViewById(R.id.semester_tv);
        semesterTv.setText(String.valueOf(semester));

        TextView branchTv = (TextView) listItemView.findViewById(R.id.branch_tv);
        branchTv.setText(branch);

        TextView sectionTv = (TextView) listItemView.findViewById(R.id.section_tv);
        sectionTv.setText(section);

        TextView subjectTv = (TextView) listItemView.findViewById(R.id.subject_tv);
        subjectTv.setText(subject);

        TextView dateTv = (TextView) listItemView.findViewById(R.id.date_tv);
        dateTv.setText(date);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takeAttendanceIntent = new Intent();
                takeAttendanceIntent.setClass(mainActivity, TakeAttendanceActivity.class);
                takeAttendanceIntent.putExtras(attendanceBundle);
                mainActivity.startActivity(takeAttendanceIntent);
            }
        });

        return listItemView;
    }
}
