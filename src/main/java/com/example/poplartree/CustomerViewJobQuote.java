package com.example.poplartree;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class CustomerViewJobQuote extends AppCompatActivity implements View.OnClickListener{

        //Global Varibles
        public DatabaseReference databaseRefrence, databaseRefrenceJobAccept;
        private ListView lv_mainlist;
        private ArrayList<String> arraylist, arraylistUser;
        private ArrayAdapter<String> aa_strings;
        private TextView selectionscreentextview_display;
        private Button buttonRejectQuote, buttonAcceptQuote, buttonback;
        private String acceptancestatus, userID, landscaperID, customer_firstName, customer_lastName, customer_email, customer_phone_number, customer_address, customer_jobdescribtion, customer_maxbudget,customer_price, customer_startdate,customer_starttime, landscaperEmail, customer_userID;
        private FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customer_view_job_quote);

            //Setting up GUI
            lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
            selectionscreentextview_display = (TextView) findViewById(R.id.selectionscreentextview_display);

            //Setting up Firebase
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            userID = user.getUid();
            databaseRefrence = FirebaseDatabase.getInstance().getReference("/JobQuote/Stack/Customer");

            //Read Data from Firebase
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

            //Accept Quote button
            buttonAcceptQuote = (Button) findViewById(R.id.buttonAcceptQuote);
            buttonAcceptQuote.setOnClickListener(new View.OnClickListener() {
                // overridden method to handle a button click
                public void onClick(View v) {
                    //call accept quote method
                    acceptquote();
                }
            });

            //Reject Quote button
            buttonRejectQuote = (Button) findViewById(R.id.buttonRejectQuote);
            buttonRejectQuote.setOnClickListener(new View.OnClickListener() {
                // overridden method to handle a button click
                public void onClick(View v) {
                    //call reject quote method
                    rejectquote();
                }
            });

            //Remove Buttons atStart
            buttonAcceptQuote.setVisibility(View.GONE);
            buttonRejectQuote.setVisibility(View.GONE);

            //Back Button
            buttonback = (Button) findViewById(R.id.buttonback);
            buttonback.setOnClickListener(new View.OnClickListener() {
                // overridden method to handle a button click
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerViewJobQuote.this, CustomerActivity.class);
                    startActivity(intent);
                }
            });

        }

        //Show data method
        public void showdata(final DataSnapshot dataSnapshot) {

            //Setting Arraylist
            arraylist = new ArrayList<String>();
            arraylistUser = new ArrayList<String>();

            //For loop though all data and storing to arraylist
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                arraylist.add(ds.child("jobdescribtion").getValue(String.class));
                arraylistUser.add(ds.child("startdate").getValue(String.class) + " - " + ds.child("jobdescribtion").getValue(String.class));
            }

            //Checking if arraylist empty and displaying to user and removing buttons
            if (arraylist.isEmpty()){
                selectionscreentextview_display.setText("You have no job quotes! Please go back");
                lv_mainlist.setVisibility(View.GONE);
                buttonAcceptQuote.setVisibility(View.GONE);
                buttonRejectQuote.setVisibility(View.GONE);
            }

            //Setting arraydatapter
            aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
            lv_mainlist.setAdapter(aa_strings);

            // add in a listener that listens for short clicks on our list items
            lv_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // overridden method that we must implement to get access to short clicks
                public void onItemClick(AdapterView<?> adapterview, View view, int pos, long id) {

                    //Storing the data to variables
                    customer_userID = dataSnapshot.child(arraylistUser.get(pos)).child("userID").getValue(String.class);
                    customer_firstName = dataSnapshot.child(arraylistUser.get(pos)).child("firstname").getValue(String.class);
                    customer_lastName = dataSnapshot.child(arraylistUser.get(pos)).child("lastname").getValue(String.class);
                    customer_email = dataSnapshot.child(arraylistUser.get(pos)).child("email").getValue(String.class);
                    customer_address = dataSnapshot.child(arraylistUser.get(pos)).child("address").getValue(String.class);
                    customer_jobdescribtion = dataSnapshot.child(arraylistUser.get(pos)).child("jobdescribtion").getValue(String.class);
                    customer_phone_number = dataSnapshot.child(arraylistUser.get(pos)).child("phone_number").getValue(String.class);
                    customer_maxbudget = dataSnapshot.child(arraylistUser.get(pos)).child("maxbudget").getValue(String.class);
                    customer_price = dataSnapshot.child(arraylistUser.get(pos)).child("price").getValue(String.class);
                    customer_startdate = dataSnapshot.child(arraylistUser.get(pos)).child("startdate").getValue(String.class);
                    customer_starttime = dataSnapshot.child(arraylistUser.get(pos)).child("starttime").getValue(String.class);
                    landscaperID = dataSnapshot.child(arraylistUser.get(pos)).child("landscaperID").getValue(String.class);
                    landscaperEmail = dataSnapshot.child(arraylistUser.get(pos)).child("landscaperEmail").getValue(String.class);
                    acceptancestatus = dataSnapshot.child(arraylistUser.get(pos)).child("acceptancestatus").getValue(String.class);

                    //if the quote status has been accepted
                    if (acceptancestatus != null) {
                        //Remove Buttons
                        buttonAcceptQuote.setVisibility(View.GONE);
                        buttonRejectQuote.setVisibility(View.GONE);
                        //Update the Screen with details
                        selectionscreentextview_display.setText(
                                        "Application status: " + acceptancestatus + "\n" +
                                        "First Name: " + customer_firstName + "\n" +
                                        "Last Name: " + customer_lastName + "\n" +
                                        "Address: " + customer_address+ "\n" +
                                        "Job Describtion: " + customer_jobdescribtion + "\n" +
                                        "Start Date: " + customer_startdate + "\n" +
                                        "Start Time: " + customer_starttime+ "\n"+
                                        "Max Budget: " + customer_maxbudget + "\n" +
                                        "Price: " + customer_price + "\n"

                        );

                    }
                    //if the quote status has been NOT accepted
                    else {
                        //Show Buttons
                        buttonAcceptQuote.setVisibility(View.VISIBLE);
                        buttonRejectQuote.setVisibility(View.VISIBLE);
                        //Update the Screen with details
                        selectionscreentextview_display.setText(
                                        "Application status: Not Accepted!" + "\n" +
                                        "First Name: " + customer_firstName + "\n" +
                                        "Last Name: " + customer_lastName + "\n" +
                                        "Address: " + customer_address+ "\n" +
                                        "Job Describtion: " + customer_jobdescribtion + "\n" +
                                        "Start Date: " + customer_startdate + "\n" +
                                        "Start Time: " + customer_starttime+ "\n"+
                                        "Max Budget: " + customer_maxbudget + "\n" +
                                        "Price: " + customer_price + "\n"
                                );

                    }

                }
            });


        }

    //Accepted Quote Method
    public void acceptquote(){

        //Email Intent
        String[] TO = { landscaperEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Quote Accepted: "+customer_firstName+" "+customer_lastName );
        emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName+ "\nLast Name: "+customer_lastName+"\n Phone Number:"+customer_phone_number+"\n Address:"+customer_address+"\nApplication status: Accepted!"+"\n Job Describtion:"+customer_jobdescribtion+"\n Max Budget:"+customer_maxbudget+"\n Price:"+customer_price+"\n Start Date:"+customer_startdate+"\n Start Time:"+customer_starttime+"\n\n\n");
        startActivity(Intent.createChooser(emailIntent, "Send Email..."));


        //Creating Records to database
        databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/LandscaperNotificationForm/Stack/Landscaper/"+landscaperID);
        LandscaperNotificationFormO requestedJob = new LandscaperNotificationFormO(customer_firstName,customer_lastName,customer_email,customer_phone_number,customer_address,customer_jobdescribtion,"","",customer_startdate,customer_starttime,customer_price,userID,landscaperID);
        databaseRefrenceJobAccept.child(customer_startdate+userID).setValue(requestedJob);
        //Creating Records to database
        databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/LandscaperNotificationForm/Database/Landscaper/"+landscaperID);
        databaseRefrenceJobAccept.child(customer_startdate+userID).setValue(requestedJob);
        //Updating Records to database
        databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/JobQuote/Stack/Customer/"+userID+"/"+customer_startdate+" - "+customer_jobdescribtion+"/");
        databaseRefrenceJobAccept.child("acceptancestatus").setValue("Accepted!");
        //Updating Records to database
        databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/JobQuote/Database/Customer/"+userID+"/"+customer_startdate+" - "+customer_jobdescribtion+"/");
        databaseRefrenceJobAccept.child("acceptancestatus").setValue("Accepted!");
        //Informing User
        Toast.makeText(this, "Quote has been accepted, Please send us Email", Toast.LENGTH_LONG).show();
    }

        //Reject Quote Method
        public void rejectquote(){

            //Email Intent
            String[] TO = {"info@poplarservices.com"}; //Poplar services.
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Quote Rejected: "+customer_firstName+" "+customer_lastName );
            emailIntent.putExtra(Intent.EXTRA_TEXT, "First Name: " + customer_firstName+ "\nLast Name: "+customer_lastName+"\n Phone Number:"+customer_phone_number+"\n Address:"+customer_address+"\nApplication status: Rejected!"+"\n Job Describtion:"+customer_jobdescribtion+"\n Max Budget:"+customer_maxbudget+"\n Price:"+customer_price+"\n Start Date:"+customer_startdate+"\n Start Time:"+customer_starttime+"\n\n\n");
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));

            //Removing Record
            databaseRefrenceJobAccept = FirebaseDatabase.getInstance().getReference("/JobQuote/Stack/Customer/"+userID+"/");
            databaseRefrenceJobAccept.child(customer_startdate+" - "+customer_jobdescribtion).setValue(null);
            //Informing User
            Toast.makeText(this, "Quote has been Rejected, Please send us Email", Toast.LENGTH_LONG).show();
        }

        public void onClick(View view) {
            //Reject Quote Button
            if (view == buttonRejectQuote) {
                rejectquote();
            }
            //Accepted Quote Button
            else if (view == buttonAcceptQuote) {
                acceptquote();
            }
        }
    }

