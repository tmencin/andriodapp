package com.example.poplartree;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class CustomerInvoice extends AppCompatActivity {

    //Global Variables
    public DatabaseReference databaseReference;
    private ListView lv_mainlist;
    private ArrayList<String>  arraylist, arraylistUser;
    private ArrayAdapter<String> aa_strings, dataAdapter;
    private TextView selectionscreentextview_display;
    private Button buttonPayment, buttonback;
    private String acceptancestatus, userID, landscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_price,customer_starttime, customer_startdate,customer_CompletedDate,customer_CompletedTime, customer_userID;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);

        //Setting the GUI
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);

        //Setting Firebase and Database Reference
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("/Invoice/nonpaid");


        //Reading from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot NewSnapshot = dataSnapshot.child(userID);
                showdata(NewSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Send Payment Button
        buttonPayment = (Button) findViewById(R.id.buttonPayment);
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                payment();
            }
        });

        //Back Button
        buttonback = (Button) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(CustomerInvoice.this, CustomerActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }


    //Show Data Method
    public void showdata(final DataSnapshot dataSnapshot) {

        //Setting Arraylist
        arraylist = new ArrayList<String>();
        arraylistUser = new ArrayList<String>();

        //For loop though all data and storing to arraylist
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arraylist.add(ds.child("customer_jobdescribtion").getValue(String.class));
            arraylistUser.add(ds.child("startdate").getValue(String.class) + ds.child("customerID").getValue(String.class));
        }
        //Checking if arraylist empty and displaying to user and removing buttons
        if (arraylist.isEmpty()){
            selectionscreentextview_display.setText("You have no Jobs! Please go back");
            lv_mainlist.setVisibility(View.GONE);
            buttonPayment.setVisibility(View.GONE);
        }
        //Setting arraydatapter and displaying it to user
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        lv_mainlist.setAdapter(aa_strings);

        // add in a listener that listens for short clicks on our list items
        lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // overridden method that we must implement to get access to short clicks
            public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {

                //Update the Screen with details
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

                //Update the Screen with details
                selectionscreentextview_display.setText(
                                "First Name: " + customer_firstName + "\n" +
                                "Last Name: " + customer_lastName + "\n" +
                                "Address: " + customer_address + "\n" +
                                "Job Describtion: " + customer_jobdescribtion + "\n" +
                                "Date Started: " + customer_startdate + "\n" +
                                "Time Started: " + customer_CompletedTime + "\n" +
                                "Date Completed: " + customer_CompletedDate + "\n" +
                                "Time Completed: " + customer_CompletedTime + "\n" +
                                "Total Price: "+customer_price + "\n");

            }
        });

    }

    //payment method
    public void payment(){
        //Checking user input if empty
        if (customer_price == null) {
            Toast.makeText(this, "Please select Invoice!", Toast.LENGTH_LONG).show();
        }
        else{
            // Linking Payment with Intent
            Intent browsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://PayPal.Me/poplartreeservices/" + customer_price));
            startActivity(browsIntent);
        }
    }

}