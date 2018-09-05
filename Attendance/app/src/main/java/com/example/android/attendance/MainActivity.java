package com.example.android.attendance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.attendance.data.AttendanceRecordContract.AttendanceRecordEntry;
import com.example.android.attendance.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private String SELECTION = AttendanceRecordEntry.FACULTY_ID_COL + "=?";

    private TextView facNameTv;
    private TextView facDeptTv;
    private TextView facIdTv;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private ListView mainListView;
    private MainListCursorAdapter cursorAdapter;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private static final int NEW_ATTENDANCE_REQUEST_CODE = 1;
    private static final int UPDATE_ATTENDANCE_REQ_CODE = 2;

    Bundle intentBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        intentBundle = getIntent().getExtras();

        String facName = intentBundle.getString("EXTRA_FACULTY_NAME");
        String facUserId = intentBundle.getString("EXTRA_FACULTY_USER_ID");
        String facDept = intentBundle.getString("EXTRA_FACULTY_DEPARTMENT");

        setupNavigationDrawer(facName, facUserId, facDept);

        mainListView = findViewById(R.id.main_list_view);

        RelativeLayout emptyView = findViewById(R.id.empty_view_main);
        mainListView.setEmptyView(emptyView);

        /**
         * open the database for getting the records of attendance
         */
        databaseHelper = new DatabaseHelper(this);

        try {
            db = databaseHelper.openDatabaseForReadWrite();
        } catch (SQLException sqle) {
            throw sqle;
        }


        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME, null, SELECTION,
                new String[]{facUserId}, null, null, null);

        cursorAdapter = new MainListCursorAdapter(this, cursor);
        mainListView.setAdapter(cursorAdapter);
        setupFloatingActionButton(facUserId);
    }

    private void setupNavigationDrawer(String facName, String facUserId, String facDept) {

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        View drawerFacDetails = navigationView.getHeaderView(0);

        facNameTv = (TextView) drawerFacDetails.findViewById(R.id.fac_name_tv);
        facDeptTv = (TextView) drawerFacDetails.findViewById(R.id.fac_dept_tv);
        facIdTv = (TextView) drawerFacDetails.findViewById(R.id.fac_id_tv);

        facNameTv.setText(facName);
        facDeptTv.setText(facDept);
        facIdTv.setText(facUserId);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * initialise floatingActionButton and link it to newAttendanceActivity
     */
    private void setupFloatingActionButton(final String facUserId) {

        FloatingActionButton newAttendanceFab = findViewById(R.id.fab_main_activity);
        newAttendanceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAttendanceIntent = new Intent();
                newAttendanceIntent.setClass
                        (MainActivity.this, NewAttendanceActivity.class);
                newAttendanceIntent.putExtra("EXTRA_FACULTY_USER_ID", facUserId);
                startActivityForResult(newAttendanceIntent, NEW_ATTENDANCE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Cursor cursor = db.query(AttendanceRecordEntry.TABLE_NAME, null, SELECTION,
                new String[]{intentBundle.getString("EXTRA_FACULTY_USER_ID")},
                null, null, null);
        cursorAdapter.swapCursor(cursor);
        cursorAdapter.notifyDataSetChanged();

    }

    public static int getUpdateAttendanceReqCode() {
        return UPDATE_ATTENDANCE_REQ_CODE;
    }


}
