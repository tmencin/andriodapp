package com.example.poplartree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LandscaperJobQuoteView extends AppCompatActivity implements View.OnClickListener{

    //Global Varibles
    public DatabaseReference databaseRefrence;
    private ListView lv_mainlist;
    private ArrayList<String>  arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private TextView selectionscreentextview_display;
    private Button buttonJobQuoteOpen, buttonback;
    private String userID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget, customer_startdate,customer_starttime, customer_userID;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscaper_job_quote_view);

        //Setting up the GUI
        buttonJobQuoteOpen = (Button) findViewById(R.id.buttonJobQuoteOpen);
        buttonback = (Button) findViewById(R.id.buttonback);
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);

        //Setting up the Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRefrence = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        //Hide Job Quote Open Button
        buttonJobQuoteOpen.setVisibility(View.GONE);

        //Reading data from Firebase
        databaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot NewSnapshot = dataSnapshot.child("AssignedJob/Stack/Landscaper/"+userID);
                showdata(NewSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //OnClickListener for Open Job Quote Button
        buttonJobQuoteOpen.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                openJobQuote();
            }
        });

        //OnClickListener for Back Button
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(LandscaperJobQuoteView.this, LandscaperActivity.class);
                startActivity(intent);
            }
        });

    }

    //Show data method
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting up Arraylist
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("firstname").getValue(String.class) + " " + ds.child("jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("userID").getValue(String.class));
        }
        //Checking if arraylist empty and displaying to user and removing buttons
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("You have no Job Assigned! Please go back");
            lv_mainlist.setVisibility(View.GONE);
            buttonJobQuoteOpen.setVisibility(View.GONE);
        }

        //Setting arraydatapter and displaying it to user
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {
                //Show Job Quote Button
                buttonJobQuoteOpen.setVisibility(View.VISIBLE);

                //Storing the data to variables
                customer_userID = dataSnapshot.child(arraylistUser.get(pos)).child("userID").getValue(String.class);
                customer_firstName = dataSnapshot.child(arraylistUser.get(pos)).child("firstname").getValue(String.class);
                customer_lastName = dataSnapshot.child(arraylistUser.get(pos)).child("lastname").getValue(String.class);
                customer_email = dataSnapshot.child(arraylistUser.get(pos)).child("email").getValue(String.class);
                customer_address = dataSnapshot.child(arraylistUser.get(pos)).child("address").getValue(String.class);
                customer_jobdescribtion = dataSnapshot.child(arraylistUser.get(pos)).child("jobdescribtion").getValue(String.class);
                customer_phone_number = dataSnapshot.child(arraylistUser.get(pos)).child("phone_number").getValue(String.class);
                customer_maxbudget = dataSnapshot.child(arraylistUser.get(pos)).child("maxbudget").getValue(String.class);
                customer_startdate = dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class);
                customer_starttime = dataSnapshot.child(arraylistUser.get(pos)).child("starttime").getValue(String.class);

                //Update the Screen with details
                selectionscreentextview_display.setText(
                                "First Name: " + customer_firstName  + "\n" +
                                "Last Name: " + customer_lastName + "\n" +
                                "Email: " + customer_email + "\n" +
                                "Phone Number: " + customer_phone_number + "\n" +
                                "Address: " + customer_address + "\n" +
                                "Job Describtion: " + customer_jobdescribtion + "\n" +
                                "Max budget: " + customer_maxbudget+ "\n" +
                                "Start Date: " + customer_startdate + "\n" +
                                "Start Time: " + customer_starttime + "\n" );

            }


        });


    }


    //Opening Job Quote
    public void openJobQuote(){
        Intent intent = new Intent(LandscaperJobQuoteView.this, LandscaperJobQuote.class);
        intent.putExtra("record", customer_userID);
        finishAffinity();
        startActivity(intent);
    }

    public void onClick(View view) {
        //Open Job Quote
        if(view == buttonJobQuoteOpen){
            openJobQuote();
        }
    }
}
