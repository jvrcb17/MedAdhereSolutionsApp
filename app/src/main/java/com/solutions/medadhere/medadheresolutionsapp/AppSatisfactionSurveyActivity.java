package com.solutions.medadhere.medadheresolutionsapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static com.solutions.medadhere.medadheresolutionsapp.R.id.answer;
import static com.solutions.medadhere.medadheresolutionsapp.R.id.lv;

/**
 * Created by lindsayherron on 1/17/17.
 */

public class AppSatisfactionSurveyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseReference mDatabase ;

    public static final String PREFS_NAME = "MyPrefsFile";
    Button btnTag;
    int qnum;
    int prevClick=-1;
    Context ctx;
    ArrayList<String> questionsArray = new ArrayList<>();
    final ArrayList<String> answers = new ArrayList<>(Collections.nCopies(21, "-1"));
    Toolbar toolbar;
    int selected =0;
    int toAnswer = 5;
    HorizontalScrollView horScrollView;
    TextView questionsView;
    Button button; //Submit Button
    Button buttonNext;
    Button buttonPrev;
    LinearLayout llradio;
    LinearLayout layout;
    String medName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_survey);
        //Button testButton = (Button) findViewById(R.id.TESTBUTTON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ctx=this;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        medName = intent.getStringExtra("medName");

        toolbar.setSubtitle("Mobile App Satisfaction Survey");

        horScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        questionsView = (TextView) findViewById(R.id.questionView);
        button = (Button) findViewById(R.id.submitButton);
        buttonNext = (Button) findViewById(R.id.nextButton);
        buttonPrev = (Button) findViewById(R.id.prevButton);
        llradio = (LinearLayout) findViewById(R.id.rblayout);
        layout = (LinearLayout) findViewById(lv);


        questionsArray.add("What is your Android device make, model, and version? ");
        questionsArray.add("Overall, I find this mobile application useful.");
        questionsArray.add("I find the medication reminders useful.");
        questionsArray.add("I find the blood pressure log useful.");
        questionsArray.add("I find the body weight log useful.");
        questionsArray.add("I find the medication utilization log useful.");
        questionsArray.add("I find the tips on how to stay healthy and adhere to my medication useful.");
        questionsArray.add("I find the call your pharmacist button useful.");
        questionsArray.add("How often do you use the feature: Blood Pressure Log");
        questionsArray.add("How often do you use the feature: Body Weight Log");
        questionsArray.add("How often do you use the feature: Medication Reminders");
        questionsArray.add("How often do you use the feature: Medication Utilization Log");
        questionsArray.add("How often do you use the feature: Call Your Pharmacist Feature");

        questionsArray.add("How would you rate the ease of the feature use: Blood Pressure Log");
        questionsArray.add("How would you rate the ease of the feature use: Body Weight Log");
        questionsArray.add("How would you rate the ease of the feature use: Medication Reminders");
        questionsArray.add("How would you rate the ease of the feature use: Medication Utilization Log");
        questionsArray.add("How would you rate the ease of the feature use: Call Your Pharmacist Feature");

        questionsArray.add("How likely are you to recommend this app to someone else?");
        questionsArray.add("What is your favorite feature of this mobile application and why is it your favorite feature?");
        questionsArray.add("What is your least favorite feature of this mobile application and why is it your least favorite feature?");

        createButtons();
    }


    public void createButtons(){
        final EditText editText = new EditText(ctx);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine(true);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);

        for (int i = 0; i < questionsArray.size(); i++) {
            final Button btnTag = new Button(this);
            btnTag.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText("Question " + (i + 1));
            btnTag.setId(Integer.parseInt(Integer.toString(i)));
            layout.addView(btnTag);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qnum = btnTag.getId();
                    horScrollView.scrollTo(qnum *12* horScrollView.getMaxScrollAmount()/22, 0);

                    if(prevClick!=-1&(prevClick==0|prevClick==19|prevClick==20)) {
                        Log.e("prev Click", Integer.toString(prevClick));
                        if(editText.getText().length()>0) {
                            answers.set(prevClick, editText.getText().toString());
                            Button btn = (Button) findViewById(prevClick);
                            btn.setBackgroundColor(Color.TRANSPARENT);
                            editText.setText("");
                        }
                    }

                    // Set Format for Questions
                    if(qnum==0|qnum==19|qnum==20){
                        try{
                            if(Integer.parseInt(answers.get(qnum))==-1){editText.setText("");}
                        }
                        catch(Exception e){
                            Log.e("Answer"+Integer.toString(qnum),answers.get(qnum));
                            editText.setText(answers.get(qnum));
                        }
                        //if(Integer.parseInt(answers.get(qnum))!=-1)editText.setText("");
                        llradio.removeAllViews();
                        llradio.addView(editText);
                    }
                    else{

                        editText.clearFocus();
                        llradio.removeAllViews();
                        addRadioButtons(qnum);
                    }
                    questionsView.setText(Integer.toString(qnum + 1) + ".  " + questionsArray.get(qnum));



                    ///Button Visibility
                    if (qnum == 0) {
                        buttonPrev.setVisibility(View.INVISIBLE);
                    }
                    if (qnum == questionsArray.size()-1) {
                        buttonNext.setVisibility(View.INVISIBLE);
                    }
                    button.setVisibility(view.VISIBLE);
                    if(qnum>0){
                        buttonPrev.setVisibility(View.VISIBLE);
                    }
                    if(qnum<questionsArray.size()-1){
                        Log.e("Should be","Visible");
                        buttonNext.setVisibility(View.VISIBLE);
                    }

                    // Check Previous Click
                    prevClick=qnum;
                }
            });
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnTag = (Button) findViewById(qnum+1);
                btnTag.performClick();

            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnTag = (Button) findViewById(qnum-1);
                btnTag.performClick();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String UID = ((MyApplication) AppSatisfactionSurveyActivity.this.getApplication()).getUID();
                int nonselected;
                selected = 0;
                Log.e("ANSWERS",answers.toString());
                nonselected = 0;
                if(qnum==0|qnum==19|qnum==20) {
                    if (editText.getText().length() > 0) {
                        answers.set(qnum, editText.getText().toString());
                    }
                }
                for (int x = 0; x < answers.size(); x++) {
                    try {
                        if (Integer.parseInt(answers.get(x)) != -1) {
                            selected++;
                        } else {
                            nonselected++;
                        }
                    }
                    catch (Exception e){
                        selected++;
                    }
                }
                Log.e("Selected",Integer.toString(selected));
                Log.e("toAnswer",Integer.toString(questionsArray.size()));
                if (selected == questionsArray.size()) {
                    Snackbar.make(v, "Medication Survey Successfully Completed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    System.out.println(dateFormat.format(cal.getTime()));
                    final String currentDate = dateFormat.format(cal.getTime());


                    mDatabase.child("app").child("users").child(UID).child("appsatisfactionanswers").child(currentDate).setValue(answers.toString());

                    Intent i = new Intent(AppSatisfactionSurveyActivity.this, SurveySelectionActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar.make(v, "Please complete all "+Integer.toString(toAnswer)+" questions. " + nonselected + " questions remain. Please scroll through the buttons above.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });



    }



    public void addRadioButtons(int question) {

        final RadioGroup rg = new RadioGroup(this);
        llradio.addView(rg);
        String [] choice = new String[5];

        String[] choices1 = {"Strongly agree", "Somewhat agree", "Do not agree or disagree", "Somewhat disagree", "Strongly disagree"};
        String[] choices2 = {"Not At All Likely", "Slightly Likely", "Moderately Likely", "Very Likely", "Completely"};
        String[] choices3 = {"Never", "Rarely", "Sometimes", "Often", "Always"};
        String[] choices4 = {"Very Easy", "Easy", "Moderate", "Difficult", "Very Difficult"};
        question++;
        if(question==2|question==3|question==4|question==5|question==6|question==7|question==8){
            for(int x=0;x<choices1.length;x++){
                choice[x]=choices1[x];
            }
        }
        else if(question==9|question==10|question==11|question==12|question==13){
            for(int x=0;x<choices3.length;x++){
                choice[x]=choices3[x];
            }
        }
        else if(question==14|question==15|question==16|question==17|question==18){
            for(int x=0;x<choices4.length;x++){
                choice[x]=choices4[x];
            }
        }
        else if(question==19){
            for(int x=0;x<choices2.length;x++){
                choice[x]=choices2[x];
            }
        }
        question--;
        final int numberRB = 5;
        final int questionnumber = question;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answers.set(questionnumber,Integer.toString(checkedId-22));
                rg.check(checkedId);
                ((Button) findViewById(questionnumber)).setBackgroundColor(Color.TRANSPARENT);
            }
        });
        // 5 radiobuttons for 1. Strongly agree 2. Somewhat agree 3. Do not agree or disagree 4. Somewhat disagree 5. Strongly disagree

        for (int i = 22; i < 27; i++) {
            final RadioButton rdbtn = new RadioButton(this);
            //rdbtn.setId((row * 2) + i);
            rdbtn.setText(choice[i - 22]);

            int textColor = Color.parseColor("#A9A9A9");
            rdbtn.setButtonTintList(ColorStateList.valueOf(textColor));
            rdbtn.setTextSize(20);
            rdbtn.setId(i);

            rg.addView(rdbtn);
            if (i - 22 != 5) {
                Space space = new Space(this);
                space.setMinimumHeight(50);
                rg.addView(space);
            }

            Log.e("QUESTION",Integer.toString(question));
            if (Integer.parseInt(answers.get(question)) == i - 22) {
                Log.e("Set", "Blue");
                rdbtn.setChecked(true);
                rdbtn.setBackgroundColor(Color.parseColor("#70FFFF"));
            }

            rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("RBID", Integer.toString(compoundButton.getId()));
                    if (b) {
                        for (int i = 22; i < 27; i++) {
                            if (i != (int) compoundButton.getId()) {
                                Log.e("Color" + i, "TRANS" + numberRB);
                                ((RadioButton) findViewById(i)).setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                        Log.e("Color" + compoundButton.getId(), "BLUE");
                        compoundButton.setBackgroundColor(Color.parseColor("#70FFFF"));
                    }
                }
            });
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {

        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id  == R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BPCalendarActivity.class);
            startActivity(i);
            finish();
        }else if(id == R.id.nav_weight){
            Intent i = new Intent(this, WeightCalendarActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, SurveySelectionActivity.class);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
