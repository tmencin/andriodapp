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

public class LandscaperViewJobs extends AppCompatActivity {

    //Global Varibles
    public DatabaseReference databaseRefrence, databaseRefrenceJobAccept,databaseRefrenceDelete;
    private ListView lv_mainlist;
    private ArrayList<String> arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private TextView selectionscreentextview_display;
    private Button buttonJobCompleted, buttonback;
    private String completed_time, completed_date, userID, landscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_price, customer_startdate,customer_starttime, customer_userID;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "AdminJobQuote";
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private EditText EditTextCompleteddate, EditTextCompletedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscaper_view_jobs);

        //Setting up GUI
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);
        EditTextCompleteddate = (EditText) findViewById(R.id.EditTextCompleteddate);
        EditTextCompletedTime = (EditText)findViewById(R.id.EditTextCompletedTime);

        //Setting up Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseRefrence = FirebaseDatabase.getInstance().getReference("/LandscaperNotificationForm/Stack/Landscaper");
        databaseRefrenceDelete = FirebaseDatabase.getInstance().getReference("/LandscaperNotificationForm/Stack/Landscaper/"+userID);

        //Reading data from Firebase
        databaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot NewSnapshot = dataSnapshot.child(userID);
                showdata(NewSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //OnClickListener Time picker
        EditTextCompletedTime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int minute = 0;
                int hour = 9;
                boolean is24Hour = true;
                TimePickerDialog timePickerDialog = new TimePickerDialog(LandscaperViewJobs.this, android.R.style.Theme_Holo_Light_Dialog, mOnTimeSetListener , hour, minute, is24Hour);
                timePickerDialog.show();
            }
        });

        //Creating TimePickerDialog
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                EditTextCompletedTime.setText("Completed time: "+hour + ":"+String.format("%02d", minute));
                completed_time = hour + ":"+String.format("%02d", minute);

            }
        };

        //setOnClickListener For Completed date and DatePickerDialog
        EditTextCompleteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        LandscaperViewJobs.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnDateSetListener, year, month, day);
                dialog.show();
            }
        });

        //Creating DatePickerDialog
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //January counts as 0 so needed +1
                month = month + 1;
                //Update the Screen with details and storing Data to variables
                completed_date = year+"-"+month+"-"+dayOfMonth;
                EditTextCompleteddate.setText("Completion Date: "+dayOfMonth+" / "+month+" / "+ year);

            }
        };

        //Job Completed Button
        buttonJobCompleted = (Button) findViewById(R.id.buttonJobCompleted);
        buttonJobCompleted.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                jobcompleted();
            }
        });
        buttonJobCompleted.setVisibility(View.GONE);

        //Back button
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(LandscaperViewJobs.this, LandscaperActivity.class);
                startActivity(intent);
            }
        });

    }

    //Show Data method
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting up arraylist
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("customer_firstname").getValue(String.class)+" "+ds.child("customer_lastname").getValue(String.class)+": "+ds.child("customer_jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("startdate").getValue(String.class) + ds.child("customerID").getValue(String.class));
        }

        //Checking if arraylist empty and displaying to user and removing buttonss
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("You have Accepted quotes, Please go back");
            lv_mainlist.setVisibility(View.GONE);
        }

        //Setting arraydatapter and displaying it to user
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {


                customer_userID = dataSnapshot.child(arraylistUser.get(pos)).child("customerID").getValue(String.class);
                customer_firstName = dataSnapshot.child(arraylistUser.get(pos)).child("customer_firstname").getValue(String.class);
                customer_lastName = dataSnapshot.child(arraylistUser.get(pos)).child("customer_lastname").getValue(String.class);
                customer_email = dataSnapshot.child(arraylistUser.get(pos)).child("customer_email").getValue(String.class);
                customer_address = dataSnapshot.child(arraylistUser.get(pos)).child("customer_address").getValue(String.class);
                customer_jobdescribtion = dataSnapshot.child(arraylistUser.get(pos)).child("customer_jobdescribtion").getValue(String.class);
                customer_phone_number = dataSnapshot.child(arraylistUser.get(pos)).child("customer_phone_number").getValue(String.class);
                customer_price = dataSnapshot.child(arraylistUser.get(pos)).child("price").getValue(String.class);
                landscaperID = dataSnapshot.child(arraylistUser.get(pos)).child("landscaperID").getValue(String.class);
                customer_startdate = dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class);
                customer_starttime = dataSnapshot.child(arraylistUser.get(pos)).child("starttime").getValue(String.class);


                //Show Job Completed Button
                buttonJobCompleted.setVisibility(View.VISIBLE);
                //Update the Screen with details
                selectionscreentextview_display.setText(
                                "First Name: " + customer_firstName  + "\n" +
                                "Last Name: " + customer_lastName + "\n" +
                                "Email: " + customer_email + "\n" +
                                "Phone Number: " + customer_phone_number + "\n" +
                                "Address: " + customer_address + "\n" +
                                "Job Describtion: " + customer_jobdescribtion + "\n" +
                                "Price " + customer_price + "\n" +
                                "Start Date: " + customer_startdate + "\n"+
                                "Start Time: " + customer_starttime + "\n"
                );



            }
        });

    }

    //Job Completed Method
    public void jobcompleted(){

        //Checking user input if empty
        if(completed_date==null){
            Toast.makeText(LandscaperViewJobs.this, "Please enter Start date!", Toast.LENGTH_LONG).show();
        }
        else if(completed_time==null){
            Toast.makeText(LandscaperViewJobs.this, "Please enter Start time!", Toast.LENGTH_LONG).show();
        } else {

            //Email Intent
            String[] TO = {"info@poplarservices.com"};//poplar system
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Landscaper Notification Form for Completed job: " + customer_firstName + " " + customer_lastName);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName + "\nLast Name: " + customer_lastName + "\n Phone Number:" + customer_phone_number + "\n Address:" + customer_address + "\n Job Describtion:" + customer_jobdescribtion + "\n Price:" + customer_price + "\n Start Date:" + customer_startdate + "\n Start Time:" + customer_starttime + "\nCompleted job date: " + completed_date + "\nCompleted job time: " + completed_time + "\n\n\n");
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));

            //Updating firebase database
            LandscaperNotificationFormO requestedJob = new LandscaperNotificationFormO(customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, completed_date, completed_time, customer_startdate,customer_starttime,customer_price, customer_userID, landscaperID);
            databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/CompletedJob/Landscaper/" + landscaperID);
            databaseRefrenceJobAccept.child(customer_startdate + customer_userID).setValue(requestedJob);
            databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/CompletedJob/Customer/" + customer_userID);
            databaseRefrenceJobAccept.child(customer_startdate + customer_userID).setValue(requestedJob);
            databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/Invoice/nonpaid/" + customer_userID);
            databaseRefrenceJobAccept.child(customer_startdate + customer_userID).setValue(requestedJob);

            //Removing data from Stack
            databaseRefrenceDelete.child(customer_startdate+customer_userID).setValue(null);
            databaseRefrenceDelete = FirebaseDatabase.getInstance().getReference("/JobQuote/Stack/Customer/"+customer_userID+"/");
            databaseRefrenceDelete.child(customer_startdate+" - "+customer_jobdescribtion).setValue(null);

        }
    }


}