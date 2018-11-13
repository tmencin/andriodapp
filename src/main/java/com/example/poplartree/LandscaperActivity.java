package com.example.poplartree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandscaperActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Varibles
    private Button buttonLogout,buttonProvideJobQuote,buttonViewAcceptedQuoutes,buttonHistory;
    private TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscaper);

        //Setting up the GUI
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //OnClickListner for Logout button
        buttonLogout.setOnClickListener(this);

        //Firebase setup
        firebaseAuth = FirebaseAuth.getInstance();

        //Displaying user login info
        if(firebaseAuth.getCurrentUser()==null){
            Toast.makeText(this, "User Not Login", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textViewUserEmail.setText("Welcome Landscaper homepage: "+user.getEmail());
        }

        //Provide Job Quote Button
        buttonProvideJobQuote = (Button) findViewById(R.id.buttonProvideJobQuote);
        buttonProvideJobQuote.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(LandscaperActivity.this, LandscaperJobQuoteView.class);
                startActivity(intent);
            }
        });

        //View Accepted Quoutes Button
        buttonViewAcceptedQuoutes  = (Button) findViewById(R.id.buttonViewAcceptedQuoutes);
        buttonViewAcceptedQuoutes.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(LandscaperActivity.this, LandscaperViewJobs.class);
                startActivity(intent);
            }
        });

        //View Feedback and History Button
        buttonHistory = (Button) findViewById(R.id.buttonHistory);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(LandscaperActivity.this, LandscaperHistory.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view) {
        //Logout button
        if(view == buttonLogout){
            finishAffinity();
            firebaseAuth.signOut();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
