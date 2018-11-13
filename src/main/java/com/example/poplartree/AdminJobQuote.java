package com.example.poplartree;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;

public class AdminJobQuote extends AppCompatActivity  implements View.OnClickListener{
    //Global Varibles
    public DatabaseReference databaseRefrence, databaseLandscaperRefrence, databaseRefrenceJobAssign, databaseRefrenceremove;
    private ListView lv_mainlist;
    private ArrayList<String> landscaperlist, arraylistUser2, arraylistLandscaperEmail,  arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private EditText etStartdate, etStarttime;
    private TextView selectionscreentextview_display;
    private static final String TAG = "AdminJobQuote";
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private Spinner spinner;
    private Button buttonAssignJob, buttonback;
    private String customer_old_date, customer_starttime, startdate,LandscaperEmail, LandscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget, customer_startdate, customer_userID;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set GUI
        setContentView(R.layout.activity_admin_job_quote);
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);
        etStartdate = (EditText) findViewById(R.id.etStartdate);
        etStarttime = (EditText)findViewById(R.id.etStarttime);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        buttonAssignJob = (Button) findViewById(R.id.buttonAssignJob);
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonAssignJob.setOnClickListener(this);
        buttonback.setOnClickListener(this);

        //Setting Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRefrence = FirebaseDatabase.getInstance().getReference();
        databaseRefrenceremove = FirebaseDatabase.getInstance().getReference("/JobRequest/Stack");

        //OnClickListener time picker dialog
        etStarttime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int minute = 0;
                int hour = 9;
                boolean is24Hour = true;
                TimePickerDialog timePickerDialog = new TimePickerDialog(AdminJobQuote.this, android.R.style.Theme_Holo_Light_Dialog, mOnTimeSetListener , hour, minute, is24Hour);
                timePickerDialog.show();
            }
        });

        //OnClickListener date picker dialog
        etStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AdminJobQuote.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnDateSetListener, year, month, day);
                dialog.show();
            }
        });

        //Creating date picker dialog
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //January counts as 0 so needed +1
                month = month + 1;

                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH)+1;
                int d = cal.get(Calendar.DAY_OF_MONTH);

                if(y>year ||y==year&&m>month||y==year&&month==m &&d>dayOfMonth ){
                    Toast.makeText(AdminJobQuote.this, "Not Valid Date! Please enter gain",
                            Toast.LENGTH_LONG).show();

                }else{
                    customer_startdate = year+"-"+month+"-"+dayOfMonth;
                    etStartdate.setText("Start date: "+dayOfMonth+" / "+month+" / "+ year);
                }
            }
        };

        //Creating time picker dialog
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                etStarttime.setText("Start time: "+hour + ":"+String.format("%02d", minute));
                customer_starttime = hour + ":"+String.format("%02d", minute);

            }
        };

        //Reading from Job Request from Firebase
        databaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot NewSnapshot = dataSnapshot.child("/JobRequest/Stack");
                showdata(NewSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Reading from Landscaper from Firebase
        databaseLandscaperRefrence = FirebaseDatabase.getInstance().getReference("/Staff/Landscaper");
        databaseLandscaperRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showLandscaper(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    //Show Data for Job Request
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting up Arraylist
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //hide Assignjob until it selected
        buttonAssignJob.setVisibility(View.GONE);

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("firstname").getValue(String.class) + " " + ds.child("jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("startdate").getValue(String.class) + " - " + ds.child("userID").getValue(String.class));
        }

        //Checking if arraylist empty and displaying to user and removing buttons
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("There are no outstanding Job Request");
            lv_mainlist.setVisibility(View.GONE);
            etStartdate.setVisibility(View.GONE);
            etStarttime.setVisibility(View.GONE);
            buttonAssignJob.setVisibility(View.GONE);
        }

        //Setting arraydatapter and displaying it to user
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {
                //Update the Screen with details
                selectionscreentextview_display.setText(
                                "First Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("firstname").getValue(String.class) + "\n" +
                                "Last Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("lastname").getValue(String.class) + "\n" +
                                "Address: " + dataSnapshot.child(arraylistUser.get(pos)).child("address").getValue(String.class) + "\n" +
                                "Email Address: " + dataSnapshot.child(arraylistUser.get(pos)).child("email").getValue(String.class) + "\n" +
                                "Phone: " + dataSnapshot.child(arraylistUser.get(pos)).child("phone_number").getValue(String.class) + "\n" +
                                "Job Describtion: " + dataSnapshot.child(arraylistUser.get(pos)).child("jobdescribtion").getValue(String.class) + "\n" +
                                "Max Budget: " + dataSnapshot.child(arraylistUser.get(pos)).child("maxbudget").getValue(String.class) + "\n" +
                                "Proposed Start Date: " + dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class)+ "\n" +
                                "Proposed Start Time: " + dataSnapshot.child(arraylistUser.get(pos)).child("starttime").getValue(String.class)
                );

                //Storing the data to variables
                customer_userID = dataSnapshot.child(arraylistUser.get(pos)).child("userID").getValue(String.class);
                customer_firstName = dataSnapshot.child(arraylistUser.get(pos)).child("firstname").getValue(String.class);
                customer_lastName = dataSnapshot.child(arraylistUser.get(pos)).child("lastname").getValue(String.class);
                customer_email = dataSnapshot.child(arraylistUser.get(pos)).child("email").getValue(String.class);
                customer_address = dataSnapshot.child(arraylistUser.get(pos)).child("address").getValue(String.class);
                customer_jobdescribtion = dataSnapshot.child(arraylistUser.get(pos)).child("jobdescribtion").getValue(String.class);
                customer_phone_number = dataSnapshot.child(arraylistUser.get(pos)).child("phone_number").getValue(String.class);
                customer_maxbudget = dataSnapshot.child(arraylistUser.get(pos)).child("maxbudget").getValue(String.class);
                customer_old_date = dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class);
                buttonAssignJob.setVisibility(View.VISIBLE);

            }


        });


    }

    public void showLandscaper(final DataSnapshot dataSnapshot) {

        //setting up spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        // Setting up Arraylist
        List<String> list = new ArrayList<String>();
        arraylistUser2 = new ArrayList<String>();
        arraylistLandscaperEmail = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            list.add(ds.child("firstname").getValue(String.class) + " " + ds.child("lastname").getValue(String.class));
            arraylistUser2.add(ds.child("userID").getValue(String.class));
            arraylistLandscaperEmail.add(ds.child("email").getValue(String.class));
        }

        //Setting arraydatapter and displaying it to user
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        // set a listener on the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // overridden method that will be called when an item has been
            // selected in the spinner
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // here we will query what item was selected and then set
                // its text on the textview
                LandscaperID = arraylistUser2.get(pos);
                LandscaperEmail = arraylistLandscaperEmail.get(pos);
            }
            // overridden method that will be called when no item has been
            // selected in the spinner
            public void onNothingSelected(AdapterView<?> parent) {
                // here is where you would react if no item was selected
            }
        });
    }

    public void assignJob(){
        if(customer_startdate==null){

            Toast.makeText(AdminJobQuote.this, "Please enter Start date!", Toast.LENGTH_LONG).show();
        }
        else if(customer_starttime==null){

            Toast.makeText(AdminJobQuote.this, "Please enter Start time!", Toast.LENGTH_LONG).show();
        } else {

            //Email Intent
            String[] TO = {LandscaperEmail};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Job Assignment: " + customer_firstName + " " + customer_lastName);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName + "\nLast Name: " + customer_lastName + "\n Phone Number:" + customer_phone_number + "\n Address:" + customer_address + "\n Job Describtion:" + customer_jobdescribtion + "\n Max Budget:" + customer_maxbudget + "\n Start Date:" + customer_startdate + "\n Start Time:" + customer_starttime + "\n\n\n");
            startActivity(Intent.createChooser(emailIntent, "Choose email client..."));

            //Update data to Firebase
            databaseRefrenceJobAssign = FirebaseDatabase.getInstance().getReference("/AssignedJob/Database/Landscaper/" + LandscaperID);
            CustomerRequestJobFormO requestedJob = new CustomerRequestJobFormO(customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget, customer_startdate, customer_starttime, customer_userID);
            databaseRefrenceJobAssign.child(customer_userID).setValue(requestedJob);
            databaseRefrenceJobAssign = FirebaseDatabase.getInstance().getReference("/AssignedJob/Stack/Landscaper/" + LandscaperID);
            databaseRefrenceJobAssign.child(customer_userID).setValue(requestedJob);
            //Remove job request from stack
            databaseRefrenceremove.child(customer_old_date + " - " + customer_userID).setValue(null);

        }

    }

    public void onClick(View view) {
        if(view == buttonAssignJob){
            assignJob();
        } else if(view == buttonback ){
            finishAffinity();
            Intent intent = new Intent(AdminJobQuote.this, AdminActivity.class);
            startActivity(intent);
        }
    }
}
