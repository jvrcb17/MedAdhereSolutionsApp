package com.solutions.medadhere.medadheresolutionsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by lindsayherron on 1/20/17.
 */

public class BPCalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context ctx;
    private DatabaseReference mDatabase;
    public static final String PREFS_NAME = "MyPrefsFile";

    int curYear;
    int curMonth;
    int curDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bp_calendar);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();


        mDatabase.child("app").child("users").child(UID).child("bloodpressuregoal").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String goal = dataSnapshot.getValue().toString();
                        Log.e("bpgoal",goal);
                        initializeCalendar(goal);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }


    public void initializeCalendar(String goal) {

        setContentView(R.layout.activity_bp_calendar);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Blood Pressure Records");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tv = (TextView) findViewById(R.id.bp_goal);
        tv.setText("My Blood Pressure Goal: "+goal);

        CalendarView calendarBlood = (CalendarView) findViewById(R.id.calendarBP);


        calendarBlood.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                String currDate = data;
                curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));


                Date current = new Date(curYear,curMonth,curDay);// some Dat
                Date survey = new Date(year,month+1,dayOfMonth);// some Date
                Date oldDateTime = new Date(1,1,2010);
                //Log.e("surveyMonth",Integer.toString(month));
                int one = (int)getDifferenceDays(oldDateTime,current);
                int two = (int)getDifferenceDays(oldDateTime,survey);

                String mon;
                String d;

                if (month+1 <10){
                    mon = "0"+Integer.toString(month+1);
                }
                else{
                    mon = Integer.toString(month+1);
                }
                if (dayOfMonth <10){
                    d = "0"+Integer.toString(dayOfMonth);
                }
                else{
                    d = Integer.toString(dayOfMonth);
                }


                int daysSince = one-two;
                //Log.e("Days Since", Integer.toString(daysSince));

                if (daysSince==0) {
                    Intent i = new Intent(ctx, BloodPressureLogActivity.class);
                    //old.set(GregorianCalendar.MONTH, date.getMonth()-1);
                    i.putExtra("date", year+"-"+mon+"-"+d);//Log.e("nrp",String.format("%d-%d", date.getMonth(), date.getDay()));
                    startActivity(i);
                    //Snackbar.make(view, String.format("%d-%d", date.getMonth(), date.getDay()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(daysSince>0){
                    Intent i = new Intent(ctx, BloodPressureLogReadActivity.class);
                    //old.set(GregorianCalendar.MONTH, date.getMonth()-1);
                    i.putExtra("date", year+"-"+mon+"-"+d);//Log.e("nrp",String.format("%d-%d", date.getMonth(), date.getDay()));
                    startActivity(i);

                }
                else{
                    Snackbar.make(view,"Please only edit the current day.",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });

    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();
        int id = item.getItemId();
        if (id  == R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_bloodpressure) {
            //Intent i = new Intent(this, BPCalendarActivity.class);
            //startActivity(i);
        }else if(id == R.id.nav_weight){
            Intent i = new Intent(this, WeightCalendarActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, com.solutions.medadhere.medadheresolutionsapp.MedicationActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, com.solutions.medadhere.medadheresolutionsapp.SurveySelectionActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+tel));
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logout) {

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_study_contact) {
            Intent i = new Intent(this, StudyContactsActivity.class);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}