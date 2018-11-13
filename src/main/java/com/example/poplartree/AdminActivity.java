package com.example.poplartree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    //Global Varibles
    private Button buttonLogout,buttonAddStaffMember,buttonAssignJob;
    private TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Setting up the UI
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout.setOnClickListener(this);

        //Setting up Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Setting up the User info
        if(firebaseAuth.getCurrentUser()==null){
            Toast.makeText(this, "User Not Login", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser user = firebaseAuth.getCurrentUser();
            textViewUserEmail.setText("Welcome "+user.getEmail());
        }

        //Add Staff Member Button
        buttonAddStaffMember = (Button) findViewById(R.id.buttonAddStaffMember);
        buttonAddStaffMember.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(AdminActivity.this, StaffRegistration.class);
                startActivity(intent);
            }
        });

        //View Assign Job To Landscaper Button
        buttonAssignJob = (Button) findViewById(R.id.buttonAssignJob);
        buttonAssignJob.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(AdminActivity.this, AdminJobQuote.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view) {
        //Log out Button
        if(view == buttonLogout){
            finishAffinity();
            firebaseAuth.signOut();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
