package com.example.android.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewAttendanceActivity extends AppCompatActivity {

    /**
     * Declare all spinners, there adapters and variable for storing selected item
     */

    /** semester */
    private Spinner semesterSpinner;
    private SpinnerArrayAdapter semesterAdapter;
    private String semesterSelected;

    /** branch */
    private Spinner branchSpinner;
    private SpinnerArrayAdapter branchAdapter;
    private String branchSelected;

    /** section */
    private Spinner sectionSpinner;
    private SpinnerArrayAdapter sectionAdapter;
    private String sectionSelected;

    /** subject */
    private Spinner subjectSpinner;
    private SpinnerArrayAdapter subjectAdapter;
    private String subjectSelected;

    //declare switch
    private Switch collegeSwitch;

    //declare date edit text
    private EditText dateEditText;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_attendance);

       setupSemesterSpinner();
       setupBranchSpinner();
       setupSectionSpinner();
       setupSubjectSpinner();

       setupFabButton();

       setupDatePickerDialog();

       collegeSwitch = findViewById(R.id.college_switch);

    }


    /**
     * opens date picker dialog when editText is clicked
     */
    private void setupDatePickerDialog() {

        myCalendar = Calendar.getInstance();

        dateEditText = findViewById(R.id.edit_date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(NewAttendanceActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * updates the editText with selected date
     */
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * setup fab button which LINKs to TakeAttendanceActivity
     */
    private void setupFabButton() {
        //initialise floatingActionButton and link it to takeAttendance
        FloatingActionButton takeAttendanceFab = findViewById(R.id.fab_new_attendance);
        takeAttendanceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeAttendanceIntent  = new Intent();
                takeAttendanceIntent.setClass(NewAttendanceActivity.this,
                        TakeAttendanceActivity.class);
                if (collegeSwitch.isChecked()) {
                    Toast.makeText(NewAttendanceActivity.this,"College: "
                                    +collegeSwitch.getTextOn().toString(),
                                     Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAttendanceActivity.this,"College: "
                            +collegeSwitch.getTextOff().toString(),
                            Toast.LENGTH_SHORT).show();
                }
                startActivity(takeAttendanceIntent);
            }
        });
    }

    /**
     * setup subject spinner
     */
    private void setupSubjectSpinner() {

       String[] subjectArray = getResources().getStringArray(R.array.subjects_array);
        subjectSpinner = (Spinner)findViewById(R.id.subject_spinner);
        subjectAdapter = new SpinnerArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, subjectArray);
        subjectSpinner.setAdapter(subjectAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ( position != 0 ) {
                    subjectSelected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewAttendanceActivity.this,
                            "Subject: "+subjectSelected,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAttendanceActivity.this,
                            "Invalid Subject",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * setup semester spinner
     */
    private void setupSemesterSpinner() {
        String[] semesterArray = getResources().getStringArray(R.array.semester_array);
        semesterSpinner = findViewById(R.id.semester_spinner);
        semesterAdapter = new SpinnerArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, semesterArray);
        semesterSpinner.setAdapter(semesterAdapter);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ( position != 0 ) {
                    semesterSelected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewAttendanceActivity.this,
                            "Semester: "+semesterSelected,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAttendanceActivity.this,
                            "Invalid semester",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * setup section spinner
     */
    private void setupSectionSpinner() {

        String[] sectionArray = getResources().getStringArray(R.array.section_array);
        sectionSpinner = findViewById(R.id.section_spinner);
        sectionAdapter = new SpinnerArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, sectionArray);
        sectionSpinner.setAdapter(sectionAdapter);
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ( position != 0 ) {
                    sectionSelected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewAttendanceActivity.this,
                            "Section: "+sectionSelected,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAttendanceActivity.this,
                            "Invalid section",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * setup branch spinner
     */
    private void setupBranchSpinner() {

        String[] branchArray = getResources().getStringArray(R.array.branch_array);
        branchSpinner = findViewById(R.id.branch_spinner);
        branchAdapter = new SpinnerArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, branchArray);
        branchSpinner.setAdapter(branchAdapter);
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ( position != 0 ) {
                    branchSelected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewAttendanceActivity.this,
                            "Branch: "+branchSelected,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAttendanceActivity.this,
                            "Invalid Branch",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



}
