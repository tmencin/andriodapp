package com.example.poplartree;

import android.content.Intent;
import android.net.Uri;
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

public class CustomerHistoryAndFeedback extends AppCompatActivity {

    //Global Variables
    public DatabaseReference databaseRefrence;
    private ListView lv_mainlist;
    private ArrayList<String> arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private TextView selectionscreentextview_display;
    private Button buttonFeedback, buttonback;
    private String starRating,acceptancestatus,checkfeedback, userID, landscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_price, customer_startdate,customer_starttime,customer_CompletedDate,customer_CompletedTime, customer_userID;
    private FirebaseAuth firebaseAuth;
    private EditText EditTextFeedback;
    private RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history_and_feedback);

        //Setting GUI and Firebase
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);
        EditTextFeedback = (EditText) findViewById(R.id.EditTextFeedback);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseRefrence = FirebaseDatabase.getInstance().getReference("/CompletedJob/Customer");


        //Reading From Firebase
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

        //Button Send Feedback
        buttonFeedback = (Button) findViewById(R.id.buttonFeedback);
        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Feedback();
            }
        });
        buttonFeedback.setVisibility(View.GONE);


        //Back Button
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CustomerHistoryAndFeedback.this, CustomerActivity.class);
                startActivity(intent);
            }
        });

    }

    // Show Data Method
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting ArrayList
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("customer_jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("startdate").getValue(String.class) + ds.child("customerID").getValue(String.class));
        }

        //Storing the data to variables
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        //Checking if arraylist empty and displaying to user and removing buttons
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("You have no Job History! Please go back");
            lv_mainlist.setVisibility(View.GONE);
            rating.setVisibility(View.GONE);
            buttonFeedback.setVisibility(View.GONE);
            EditTextFeedback.setVisibility(View.GONE);
        }

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
                checkfeedback= dataSnapshot.child(arraylistUser.get(pos)).child("feedback_description").getValue(String.class);
                //Update the Screen with details
                if (checkfeedback != null) {
                    buttonFeedback.setVisibility(View.GONE);
                    rating.setVisibility(View.GONE);
                    EditTextFeedback.setVisibility(View.GONE);
                    selectionscreentextview_display.setText(
                                   "First Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_firstname").getValue(String.class) + "\n" +
                                    "Last Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_lastname").getValue(String.class) + "\n" +
                                    "Address: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_address").getValue(String.class) + "\n" +
                                    "Job Describtion: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_jobdescribtion").getValue(String.class) + "\n" +
                                    "Feedback: " + dataSnapshot.child(arraylistUser.get(pos)).child("feedback_description").getValue(String.class) + "\n" +
                                    "Job Rating: " + dataSnapshot.child(arraylistUser.get(pos)).child("jobrating").getValue(String.class) + "\n" +
                                    "Date Started: " + customer_startdate + "\n"+
                                    "Time Started: " + customer_starttime + "\n"+
                                    "Date Completed: " + customer_CompletedDate + "\n"+
                                    "Time Completed: " + customer_CompletedTime + "\n"+
                                    "Total Price: " + customer_price + "\n"
                    );
                          }
                          else{
                    buttonFeedback.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                    EditTextFeedback.setVisibility(View.VISIBLE);
                    //Update the Screen with details
                    selectionscreentextview_display.setText(
                            "First Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_firstname").getValue(String.class) + "\n" +
                            "Last Name: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_lastname").getValue(String.class) + "\n" +
                            "Address: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_address").getValue(String.class) + "\n" +
                            "Job Describtion: " + dataSnapshot.child(arraylistUser.get(pos)).child("customer_jobdescribtion").getValue(String.class) + "\n" +
                            "Date Started: " + customer_startdate + "\n"+
                            "Time Started: " + customer_starttime + "\n"+
                            "Date Completed: " + customer_CompletedDate + "\n"+
                            "Time Completed: " + customer_CompletedTime + "\n"+
                            "Total Price: " + customer_price + "\n");
                    }
                }
        });

    }

    //Send feedback Method
    public void Feedback(){

        //Storing the data to variables
        String feedback= EditTextFeedback.getText().toString().trim();
        String starRating = String.valueOf((int)rating.getRating());

        //Checking user input if empty
        if(feedback.matches("")){
            Toast.makeText(CustomerHistoryAndFeedback.this, "Please enter Feedback! And Select Job", Toast.LENGTH_LONG).show();
        }
        else if(starRating==null){

            Toast.makeText(CustomerHistoryAndFeedback.this, "Please, job rate us", Toast.LENGTH_LONG).show();
        } else {
            //Creating Email Intent
            String[] TO = {};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Feedback: " + customer_firstName + " " + customer_lastName);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName + "\nLast Name: " + customer_lastName + "\n Phone Number:" + customer_phone_number + "\n Address:" + customer_address + "\n Job Describtion:" + customer_jobdescribtion + "\n Price:" + customer_price + "\n Start Date:" + customer_startdate + "\n Start Time:" + customer_starttime +"\n Completed Job Date:" + customer_CompletedDate + "\n Completed Job Time:" + customer_CompletedTime + "\nFeedback:" + feedback + "\nStar Rating: " + starRating + "\n\n\n");
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));

            //Creating Records to database
            databaseRefrence = FirebaseDatabase.getInstance().getReference("/CompletedJob/Landscaper/" + landscaperID + "/" + customer_startdate + customer_userID);
            databaseRefrence.child("jobRating").setValue(starRating);
            databaseRefrence.child("feedback_description").setValue(feedback);
            //Creating Records to database
            databaseRefrence = FirebaseDatabase.getInstance().getReference("/CompletedJob/Customer/" + customer_userID + "/" + customer_startdate + customer_userID);
            databaseRefrence.child("jobrating").setValue(starRating);
            databaseRefrence.child("feedback_description").setValue(feedback);
            //Reseting databaseRefrence to default
            databaseRefrence = FirebaseDatabase.getInstance().getReference("/CompletedJob/Customer");

        }
    }

}