package com.example.poplartree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerActivity  extends AppCompatActivity implements View.OnClickListener {

    //Global Variables
    private Button buttonLogout,buttonRequestJob,buttonViewJobQuote,buttonViewInVoiceAndPayment,buttonHistoryAndFeedback;
    private TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //Setting up logout button and email
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //OnClickListener For Logout
        buttonLogout.setOnClickListener(this);

        //firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //Checking Current User
        if(firebaseAuth.getCurrentUser()==null){
            Toast.makeText(this, "User Not Login", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textViewUserEmail.setText("Welcome "+user.getEmail());
        }


        //Request Job button (onclickListener and intent to Request job)
        buttonRequestJob = (Button) findViewById(R.id.buttonRequestJob);
        buttonRequestJob.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //closes all previous activities
                finishAffinity();
                Intent intent = new Intent(CustomerActivity.this, CustomerRequestedJobForm.class);
                startActivity(intent);
            }
        });

        //View Quote Button (onclickListener and intent)
        buttonViewJobQuote = (Button) findViewById(R.id.buttonViewJobQuote);
        buttonViewJobQuote.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //closes all previous activities
                finishAffinity();
                Intent intent = new Intent(CustomerActivity.this, CustomerViewJobQuote.class);
                startActivity(intent);
            }
        });

        //View Invoice and payment button (onclickListener and intent)
        buttonViewInVoiceAndPayment = (Button) findViewById(R.id.buttonViewInVoiceAndPayment);
        buttonViewInVoiceAndPayment.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //closes all previous activities
                finishAffinity();
                Intent intent = new Intent(CustomerActivity.this, CustomerInvoice.class);
                startActivity(intent);
            }
        });

        //History and Feedback (onclickListener and intent)
        buttonHistoryAndFeedback = (Button) findViewById(R.id.buttonHistoryAndFeedback);
        buttonHistoryAndFeedback.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(CustomerActivity.this, CustomerHistoryAndFeedback.class);
                startActivity(intent);
            }
        });


    }


    public void onClick(View view) {
        //Logout button
        if(view == buttonLogout){
            //closes all activities
            finishAffinity();
            firebaseAuth.signOut();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
