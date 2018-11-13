package com.example.poplartree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LandscaperHistory extends AppCompatActivity {

    //Global Varibles
    public DatabaseReference databaseRefrence;
    private ListView lv_mainlist;
    private ArrayList<String> arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private TextView selectionscreentextview_display;
    private Button buttonback;
    private String jobRating,feedback_description, userID, landscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_price,customer_startdate,customer_starttime, customer_CompletedDate,customer_CompletedTime, customer_userID;
    private FirebaseAuth firebaseAuth;
    private EditText EditTextFeedback;
    private RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscaper_history);

        //Setting up the GUI
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);
        EditTextFeedback = (EditText) findViewById(R.id.EditTextFeedback);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        //Setting up Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseRefrence = FirebaseDatabase.getInstance().getReference("/CompletedJob/Landscaper");

        //reading from database
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

        //Back Button
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(LandscaperHistory.this, LandscaperActivity.class);
                startActivity(intent);
            }
        });
    }

    //Showdata method
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting Arraylist
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("customer_firstname").getValue(String.class)+ " "+ds.child("customer_lastname").getValue(String.class)+" : "+ds.child("customer_jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("startdate").getValue(String.class) + ds.child("customerID").getValue(String.class));
        }

        //Checking if arraylist empty and displaying to user and removing buttons
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("You have no Job History, Please go back");
            lv_mainlist.setVisibility(View.GONE);
        }

        //Setting arraydatapter and displaying it to user
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {
                //Storing the data to variables
                customer_userID = dataSnapshot.child(arraylistUser.get(pos)).child("customerID").getValue(String.class);
                customer_firstName = dataSnapshot.child(arraylistUser.get(pos)).child("customer_firstname").getValue(String.class);
                customer_lastName = dataSnapshot.child(arraylistUser.get(pos)).child("customer_lastname").getValue(String.class);
                customer_email = dataSnapshot.child(arraylistUser.get(pos)).child("customer_email").getValue(String.class);
                customer_address = dataSnapshot.child(arraylistUser.get(pos)).child("customer_address").getValue(String.class);
                customer_jobdescribtion = dataSnapshot.child(arraylistUser.get(pos)).child("customer_jobdescribtion").getValue(String.class);
                customer_phone_number = dataSnapshot.child(arraylistUser.get(pos)).child("customer_phone_number").getValue(String.class);
                customer_price = dataSnapshot.child(arraylistUser.get(pos)).child("price").getValue(String.class);
                customer_CompletedDate = dataSnapshot.child(arraylistUser.get(pos)).child("completed_date").getValue(String.class);
                customer_CompletedTime = dataSnapshot.child(arraylistUser.get(pos)).child("completed_time").getValue(String.class);
                customer_startdate = dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class);
                customer_starttime = dataSnapshot.child(arraylistUser.get(pos)).child("starttime").getValue(String.class);
                landscaperID = dataSnapshot.child(arraylistUser.get(pos)).child("landscaperID").getValue(String.class);
                jobRating = dataSnapshot.child(arraylistUser.get(pos)).child("jobRating").getValue(String.class);
                feedback_description = dataSnapshot.child(arraylistUser.get(pos)).child("feedback_description").getValue(String.class);

                //Update the Screen with details with Feedback and Job Rating
               if (jobRating != null && feedback_description != null) {
                    selectionscreentextview_display.setText(
                                    "First Name: " + customer_firstName + "\n" +
                                    "Last Name: " + customer_lastName  + "\n" +
                                    "Email Address: " + customer_email + "\n" +
                                    "Address: " + customer_address + "\n" +
                                    "Job Description: " + customer_jobdescribtion + "\n" +
                                    "Date Started: " + customer_startdate + "\n"+
                                    "Time Started: " + customer_starttime + "\n"+
                                    "Date Completed: " + customer_CompletedDate + "\n"+
                                    "Time Completed: " + customer_CompletedTime + "\n"+
                                    "Job Rating: " + jobRating + "\n"+
                                    "Feedback Description: " + feedback_description + "\n"+
                                    "Total Price: " + customer_price + "\n");

                }  //Update the Screen with details without Feedback and Job Rating
                else {
                    selectionscreentextview_display.setText(
                                    "First Name: " + customer_firstName + "\n" +
                                    "Last Name: " + customer_lastName  + "\n" +
                                    "Email Address: " + customer_email + "\n" +
                                    "Address: " + customer_address + "\n" +
                                    "Job Description: " + customer_jobdescribtion + "\n" +
                                    "Date Started: " + customer_startdate + "\n"+
                                    "Time Started: " + customer_starttime + "\n"+
                                    "Date Completed: " + customer_CompletedDate + "\n"+
                                    "Time Completed: " + customer_CompletedTime + "\n");

               }

            }
        });

    }



}