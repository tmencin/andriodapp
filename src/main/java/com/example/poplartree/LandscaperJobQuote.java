package com.example.poplartree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandscaperJobQuote extends AppCompatActivity {

    //Global Varibles
     private Button buttonJobQuote, buttonback;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefrence,databaseRefrenceJobQuote, databaseRefrenceremove;
    private EditText EditTextcustomer_firstName, EditTextcustomer_lastName,task_describtion,Address,customer_Email,phone,etStartdate,etStarttime,etMaxBudget,etPrice;
    private String price,userID,landscaperEmail, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget, customer_startdate, customer_starttime, customer_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscaper_job_quote);

        //Getting Customer UserID for Intent
        final String customer_userID = getIntent().getExtras().getString("record");

        //Setting up the GUI
        buttonJobQuote = (Button) findViewById(R.id.buttonJobQuote);
        EditTextcustomer_firstName = (EditText) findViewById(R.id.EditTextcustomer_firstName);
        EditTextcustomer_lastName = (EditText) findViewById(R.id.EditTextcustomer_lastName);
        task_describtion = (EditText) findViewById(R.id.task_describtion);
        Address = (EditText) findViewById(R.id.address);
        customer_Email = (EditText) findViewById(R.id.customer_Email);
        phone = (EditText) findViewById(R.id.phone);
        etMaxBudget = (EditText) findViewById(R.id.editTextMaxBudget);
        etStartdate = (EditText) findViewById(R.id.edittextStartdate);
        etStarttime = (EditText) findViewById(R.id.edittextStarttime);
        etPrice = (EditText) findViewById(R.id.edittextPrice);

        //Setting up Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRefrence = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        landscaperEmail = user.getEmail();
        databaseRefrenceremove = FirebaseDatabase.getInstance().getReference("/AssignedJob/Stack/Landscaper/"+userID);


        //Reading data from firebase
        databaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot Snapshot = dataSnapshot.child("/AssignedJob/Stack/Landscaper/"+userID);
                DataSnapshot NewSnapshot = Snapshot.child(customer_userID);
                showdata(NewSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //Send Quote to Customer Button
        buttonJobQuote.setOnClickListener(new View.OnClickListener() {
        // overridden method to handle a button click
        public void onClick(View v) {
            //Storing User details to variables
            price = etPrice.getText().toString().trim();
            //Checking user input if empty
            if(price.matches("")) {
                Toast.makeText(LandscaperJobQuote.this, "Please enter Quote price!", Toast.LENGTH_LONG).show();
            }
            else{
                //Email intent Send to Job Quote to customer
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] TO = {customer_email};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, " Poplar Services Job Quote: " + customer_firstName + " " + customer_lastName);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName + "\nLast Name: " + customer_lastName + "\n Phone Number:" + customer_phone_number + "\n Address:" + customer_address + "\n Job Describtion:" + customer_jobdescribtion + "\n Max Budget:" + customer_maxbudget+ "\n Price:" + price  + "\n Start Date:" + customer_startdate + "\n Start Time:" + customer_starttime + "\n\n\n");
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose email client..."));

                //adding JobQuote to database
                LandscaperJobQuoteO requestedJob = new LandscaperJobQuoteO(customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget, customer_startdate, customer_starttime, price,landscaperEmail, userID);
                databaseRefrenceJobQuote = FirebaseDatabase.getInstance().getReference("JobQuote/Stack/Customer/" + customer_userID);
                databaseRefrenceJobQuote.child(customer_startdate + " - " + customer_jobdescribtion).setValue(requestedJob);
                databaseRefrenceJobQuote = FirebaseDatabase.getInstance().getReference("JobQuote/Database/Customer/" + customer_userID);
                databaseRefrenceJobQuote.child(customer_startdate + " - " + customer_jobdescribtion).setValue(requestedJob);
                //Removing Job Quote from Stack
                databaseRefrenceremove.child(customer_userID).setValue(null);

                //Update the user
                Toast.makeText(LandscaperJobQuote.this, "Job Quote has been Send", Toast.LENGTH_SHORT).show();
            }

        }
    });

        //Back Button
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(LandscaperJobQuote.this, LandscaperActivity.class);
                startActivity(intent);
            }
        });

    }

    //Show data Method
    public void showdata(final DataSnapshot dataSnapshot) {
        //Storing the data to variables
        customer_firstName = (String) dataSnapshot.child("firstname").getValue(String.class);
        customer_lastName =  (String)dataSnapshot.child("lastname").getValue(String.class);
        customer_email = (String) dataSnapshot.child("email").getValue(String.class);
        customer_jobdescribtion = (String)dataSnapshot.child("jobdescribtion").getValue(String.class);
        customer_address = (String)dataSnapshot.child("address").getValue(String.class);
        customer_email = (String)dataSnapshot.child("email").getValue(String.class);
        customer_phone_number= (String)dataSnapshot.child("phone_number").getValue(String.class);
        customer_maxbudget=(String)dataSnapshot.child("maxbudget").getValue(String.class);
        customer_startdate=(String)dataSnapshot.child("startdate").getValue(String.class);
        customer_starttime=(String)dataSnapshot.child("starttime").getValue(String.class);

        //Update the Screen with details
        EditTextcustomer_firstName.setText("First Name: "+ customer_firstName);
        EditTextcustomer_lastName.setText("Last Name: "+ customer_lastName);
        customer_Email.setText("Email: "+ customer_email);
        task_describtion.setText("Task Describtion: "+ customer_jobdescribtion);
        Address.setText("Address: "+ customer_address);
        phone.setText("Phone: "+ customer_phone_number);
        etMaxBudget.setText("Max Budget: "+ customer_maxbudget);
        etStartdate.setText("Start Date: "+ customer_startdate);
        etStarttime.setText("Start Time: "+ customer_starttime);
    }
}


