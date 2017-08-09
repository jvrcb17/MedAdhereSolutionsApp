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
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import static com.solutions.medadhere.medadheresolutionsapp.R.id.lv;

//import android.icu.text.DateFormat;
//import android.icu.text.SimpleDateFormat;


/**
 * Created by Javier 9/18/16.
 */
public class MedicationAdherenceSurvey extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseReference mDatabase ;

    public static final String PREFS_NAME = "MyPrefsFile";
    Button btnTag;
    int number;
    int qnum;
    int prevClick=-1;
    Context ctx;
    ArrayList<String> questionsArray = new ArrayList<>();
    final ArrayList<String> answers = new ArrayList<>(Collections.nCopies(8, "-1"));
    Toolbar toolbar;
    int selected =0;
    public int questCount = 0;
    int toAnswer = 5;
    HorizontalScrollView horScrollView;
    TextView questionsView;
    Button button; //Submit Button
    Button buttonNext;
    Button buttonPrev;
    LinearLayout llradio;
    LinearLayout layout;
    String medName;
    Boolean five=true;


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

        toolbar.setSubtitle("Medicaiton Adherence Survey: " + medName);

        horScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        questionsView = (TextView) findViewById(R.id.questionView);
        button = (Button) findViewById(R.id.submitButton);
        buttonNext = (Button) findViewById(R.id.nextButton);
        buttonPrev = (Button) findViewById(R.id.prevButton);
        llradio = (LinearLayout) findViewById(R.id.rblayout);
        layout = (LinearLayout) findViewById(lv);


        questionsArray.add("I find it easy to take "+medName+" every day at the correct times.");
        questionsArray.add("The frequency at which I am supposed to take it is the biggest barrier to taking "+medName+" correctly.");
        questionsArray.add("The shape or taste of the pill is the biggest barrier to taking "+medName+" correctly.");
        questionsArray.add("The price of "+medName+" is the biggest barrier to taking it correctly.");
        questionsArray.add("I do not take "+medName+" according to the prescribed schedule because it does not work for me.");
        questionsArray.add("I do not take "+medName+" according to the prescribed schedule because it has an unpleasant side effect.");
        questionsArray.add("I do not take "+medName+" according to the prescribed schedule because I have trouble remembering to do so.");
        questionsArray.add("What factors make "+medName+" easy or difficult to take according to the prescribed schedule?");


        createButtons();
    }


    public void createButtons(){
        final EditText editText = new EditText(ctx);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine(true);


        for (int i = 0; i < toAnswer; i++) {
            final Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btnTag.setText("Question " + (i + 1));
            btnTag.setId(Integer.parseInt(Integer.toString(i)));
            layout.addView(btnTag);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qnum = btnTag.getId();
                    horScrollView.scrollTo(qnum *5* horScrollView.getMaxScrollAmount()/8, 0);

                    //Button Visibility
                    if (qnum == 0) {
                        buttonPrev.setVisibility(View.INVISIBLE);
                    }
                    if (qnum == toAnswer-1) {
                        buttonNext.setVisibility(View.INVISIBLE);
                    }
                    button.setVisibility(view.VISIBLE);
                    if(qnum>0){
                        buttonPrev.setVisibility(View.VISIBLE);
                    }
                    if(qnum<toAnswer-1){
                        Log.e("Should be","Visible");
                        buttonNext.setVisibility(View.VISIBLE);
                    }

                    // Set Format for Questions
                    if(qnum==toAnswer-1){
                        if(qnum==4&five==true) {
                            questionsView.setText(Integer.toString(qnum + 1) + ".  " + questionsArray.get(qnum + 3));
                        }
                        else{
                            questionsView.setText(Integer.toString(qnum + 1) + ".  " + questionsArray.get(qnum));

                        }
                        llradio.removeAllViews();
                        llradio.addView(editText);
                    }
                    else{
                        questionsView.setText(Integer.toString(qnum + 1) + ".  " + questionsArray.get(qnum));
                        editText.clearFocus();
                        llradio.removeAllViews();
                        addRadioButtons(qnum);
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
                String UID = ((MyApplication) MedicationAdherenceSurvey.this.getApplication()).getUID();
                int nonselected;
                selected = 0;
                if(toAnswer==5) {
                    nonselected = -3;
                }
                else{
                    nonselected = 0;
                }
                for (int x = 0; x < answers.size(); x++) {
                    if (Integer.parseInt(answers.get(x)) != -1) {
                        selected++;
                    } else {
                        nonselected++;
                    }
                }
                if(editText.getText().length()>0){
                    selected++;
                    Log.e("Edit Text",editText.getText().toString());
                    answers.set(7,editText.getText().toString());
                    nonselected--;
                }
                Log.e("Selected",Integer.toString(selected));
                Log.e("toAnswer",Integer.toString(toAnswer));
                if (selected == toAnswer) {
                    Snackbar.make(v, "Medication Survey Successfully Completed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    System.out.println(dateFormat.format(cal.getTime()));
                    final String currentDate = dateFormat.format(cal.getTime());


                    mDatabase.child("app").child("users").child(UID).child(medName +"medadherenceanswers").child(currentDate).setValue(answers.toString());

                    Intent i = new Intent(MedicationAdherenceSurvey.this, SurveySelectionActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar.make(v, "Please complete all "+Integer.toString(toAnswer)+" questions. " + nonselected + " questions remain. Please scroll through the buttons above.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });



    }



    public void addRadioButtons(final int question) {

        final RadioGroup rg = new RadioGroup(this);
        llradio.addView(rg);

        String[] choices = {"Strongly agree", "Somewhat agree", "Do not agree or disagree", "Somewhat disagree", "Strongly disagree"};

        final int numberRB = 5;
        final int questionnumber = question;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(toAnswer==5&checkedId==4) {
                    answers.set(questionnumber, Integer.toString(checkedId - 8+3));
                }
                else{
                    answers.set(questionnumber, Integer.toString(checkedId - 8));
                }
                if (questionnumber == 0) {
                    if ((checkedId - 8) == 2 | (checkedId - 8 == 3) | (checkedId - 8) == 4) {
                        Log.e("checkedID",Integer.toString(checkedId - 8));
                        if(toAnswer!=8) {
                            toAnswer = 8;
                            layout.removeAllViews();
                            createButtons();
                        }
                    }
                    else{
                        if(toAnswer!=5) {
                            toAnswer = 5;
                            layout.removeAllViews();
                            createButtons();
                        }
                    }

                }
                rg.check(checkedId);
                ((Button) findViewById(questionnumber)).setBackgroundColor(Color.TRANSPARENT);
            }
        });
        // 5 radiobuttons for 1. Strongly agree 2. Somewhat agree 3. Do not agree or disagree 4. Somewhat disagree 5. Strongly disagree

        for (int i = 8; i < 13; i++) {
            final RadioButton rdbtn = new RadioButton(this);
            //rdbtn.setId((row * 2) + i);
            rdbtn.setText(choices[i - 8]);

            int textColor = Color.parseColor("#A9A9A9");
            rdbtn.setButtonTintList(ColorStateList.valueOf(textColor));
            rdbtn.setTextSize(20);
            rdbtn.setId(i);

            rg.addView(rdbtn);
            if (i - 8 != 5) {
                Space space = new Space(this);
                space.setMinimumHeight(50);
                rg.addView(space);
            }

            if (Integer.parseInt(answers.get(question)) == i - 8) {
                Log.e("Set", "Blue");
                rdbtn.setChecked(true);
                rdbtn.setBackgroundColor(Color.parseColor("#70FFFF"));
            }

            rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("RBID", Integer.toString(compoundButton.getId()));
                    if (b) {
                        for (int i = 8; i < 13; i++) {
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
