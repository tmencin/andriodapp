package com.example.poplartree;

/**
 * Created by TMENCIN on 4/10/2018.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class CustomerRequestedJobForm extends AppCompatActivity {

    //global variables
    private EditText customer_firstName,customer_lastName, task_describtion,Address, etMaxBudget,customer_Email,phone, etStartdate, etStarttime;
    private Button requestjobbutton, backbutton;
    private static final String TAG = "CustomerRequestedJobForm";
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefrence,databaseRefrenceJobRequest, databaseRefrenceJobRequest2;
    private String firstname,lastname,email,phone_number, address, jobdescribtion, maxbudget, startdate,starttime, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_requestedjob);

        //Database Refrences to upload to firebase
        databaseRefrenceJobRequest = FirebaseDatabase.getInstance().getReference("/JobRequest/Database");
        databaseRefrenceJobRequest2 = FirebaseDatabase.getInstance().getReference("/JobRequest/Stack");

        //Setting up UI
        requestjobbutton = (Button) findViewById(R.id.requestjobbutton);
        customer_firstName = (EditText) findViewById(R.id.customer_firstName);
        customer_lastName = (EditText) findViewById(R.id.customer_lastName);
        task_describtion = (EditText) findViewById(R.id.task_describtion);
        Address = (EditText) findViewById(R.id.address);
        customer_Email = (EditText) findViewById(R.id.customer_Email);
        phone = (EditText) findViewById(R.id.phone);
        etMaxBudget = (EditText) findViewById(R.id.editTextMaxBudget);
        etStartdate = (EditText)findViewById(R.id.etStartdate);
        etStarttime = (EditText)findViewById(R.id.etStarttime);

        //Setting up firebase user and database refrence
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Customer");
        //Storing userID to variable
        userID = user.getUid();

        //onClickListener to time picker dialog
        etStarttime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //setting up start time at 9:00 and 24 hour time
                int minute = 0;
                int hour = 9;
                boolean is24Hour = true;

                //Creating  TimePickerDialog and showing it
                TimePickerDialog timePickerDialog = new TimePickerDialog(CustomerRequestedJobForm.this, android.R.style.Theme_Holo_Light_Dialog, mOnTimeSetListener , hour, minute, is24Hour);
                timePickerDialog.show();
            }
        });

        //onClickListener to DatePickerDialog
        etStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting it up to be the current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //Creating DatePickerDialog and showing it
                DatePickerDialog dialog = new DatePickerDialog(
                        CustomerRequestedJobForm.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnDateSetListener, year, month, day);
                dialog.show();

            }
        });

        //Setting OnDateListner
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //January counts as 0 so needed +1
                month = month + 1;

                //Variables for checking user input user input
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH)+1;
                int d = cal.get(Calendar.DAY_OF_MONTH);

                //Checking user input
                if(y>year ||y==year&&m>month||y==year&&month==m &&d>dayOfMonth ){
                    Toast.makeText(CustomerRequestedJobForm.this, "Not Valid Date! Please enter gain",
                            Toast.LENGTH_LONG).show();

                }else{
                    //displaying it and storing the start date
                    startdate = year+"-"+month+"-"+dayOfMonth;
                    etStartdate.setText("Start date: "+dayOfMonth+" / "+month+" / "+ year);
                }
            }
        };

        //Setting OnTimeListner
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    //displaying time and storeing it also setting it to two decimals
                    etStarttime.setText("Start time: "+hour + ":"+String.format("%02d", minute));
                    starttime = hour + ":"+String.format("%02d", minute);
            }
        };


        //Reading Data From database
        databaseRefrence.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showdata(dataSnapshot);

            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });

        //Reguest Job Button
        requestjobbutton.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {

                //Storing User inputs
                firstname = customer_firstName.getText().toString().trim();
                lastname = customer_lastName.getText().toString().trim();
                phone_number = phone.getText().toString().trim();
                address = Address.getText().toString().trim();
                jobdescribtion = task_describtion.getText().toString().trim();
                email = customer_Email.getText().toString().trim();
                maxbudget = etMaxBudget.getText().toString().trim();

                //Setting Firebase
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Checking user input if empty
                if(jobdescribtion.matches("")) {
                    Toast.makeText(CustomerRequestedJobForm.this, "Please enter Describtion!", Toast.LENGTH_LONG).show();
                }
                else if(maxbudget.matches("")){

                    Toast.makeText(CustomerRequestedJobForm.this, "Please enter Max Budget!", Toast.LENGTH_LONG).show();
                }
                else if(startdate==null){

                    Toast.makeText(CustomerRequestedJobForm.this, "Please enter Start date!", Toast.LENGTH_LONG).show();
                }
                else if(starttime==null){

                    Toast.makeText(CustomerRequestedJobForm.this, "Please enter Start time!", Toast.LENGTH_LONG).show();
                } else {

                    //Displaying info to user
                    Toast.makeText(CustomerRequestedJobForm.this, "Job Requested, Please Send Us an Email", Toast.LENGTH_LONG).show();

                    //Uploading to firebase
                    CustomerRequestJobFormO requestedJob = new CustomerRequestJobFormO(firstname, lastname, email, phone_number, address, jobdescribtion, maxbudget, startdate, starttime, userID);
                    databaseRefrenceJobRequest.child(startdate + " - " + user.getUid()).setValue(requestedJob);
                    databaseRefrenceJobRequest2.child(startdate + " - " + user.getUid()).setValue(requestedJob);

                    //Setting Email Intent
                    String[] TO = {"info@poplarservices.com"}; //Poplar system
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("message/rfc822");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Job Request");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + firstname + "\nLast Name: " + lastname + "\n Phone Number:" + phone_number + "\n Address:" + address + "\n Job Describtion:" + jobdescribtion + "\n Max Budget:" + maxbudget + "\nProposed Start Date:" + startdate + "\nProposed Start Time:" + starttime + "\n\n\n");
                    startActivity(Intent.createChooser(emailIntent, "Send Email..."));

                }
            }
        });

        //Setting Up the BackButton
        backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRequestedJobForm.this, CustomerActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }

    //Showing data method
    public void showdata(DataSnapshot dataSnapshot) {

        //Displaying data from Firebase
        customer_firstName.setText((String) dataSnapshot.child(userID).child("firstname").getValue(String.class));
        customer_lastName.setText((String) dataSnapshot.child(userID).child("lastname").getValue(String.class));
        customer_Email.setText((String) dataSnapshot.child(userID).child("email").getValue(String.class));
        Address.setText((String) dataSnapshot.child(userID).child("address").getValue(String.class));
        phone.setText((String) dataSnapshot.child(userID).child("phone_number").getValue(String.class));
    }

}
