package com.example.poplartree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffLogin extends AppCompatActivity implements View.OnClickListener {

    //Global Varibles
    private Button buttonSignIn,backbutton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseRefrence;
    public String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        //Setting up firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Creating progress dialog
        progressDialog = new ProgressDialog(this);

        //Setting up GUI
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPassword = (EditText) findViewById(R.id.EditTextpassword);;

        //OnClickListener for Sign in button
        buttonSignIn.setOnClickListener(this);

        //Back Button
        backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(StaffLogin.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }


    private void UserLogin(){
        //Storing the data to variables
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Checking user input if empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        //Checking user input if empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Update the Screen with details
        progressDialog.setMessage("Login in");
        progressDialog.show();

        //Login in with FireBase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        progressDialog.dismiss();

                        //login in user
                        if(task.isSuccessful()){
                            //Setting up Firebase
                            databaseRefrence = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userID = user.getUid();

                            //Reading data from Firebase
                            databaseRefrence.addValueEventListener(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    DataSnapshot NewSnapshot = dataSnapshot.child("Staff");
                                    showdata(NewSnapshot);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError){
                                }
                            });

                        } else {
                            Toast.makeText(StaffLogin.this, "Email and Password is not found please check details and try again", Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }

    public void onClick(View view) {
        //Sign in Button
        if(view == buttonSignIn){
            UserLogin();
        }
    }

    //Show Data Method
    public void showdata(DataSnapshot dataSnapshot){
        //Storing the data to variables
        String landscaperCheck = (String)dataSnapshot.child("Landscaper").child(userID).child("type").getValue(String.class);
        String adminCheck = (String)dataSnapshot.child("Admin").child(userID).child("type").getValue(String.class);

        //Login in if Landscaper
       if (landscaperCheck != null){
            Intent intent = new Intent(StaffLogin.this, LandscaperActivity.class);
            finish();
            startActivity(intent);
        } //Login in if Admin
        else if(adminCheck != null) {
            //CHANGE TO ADMIN ACTIVITY
            Intent intent = new Intent(StaffLogin.this, AdminActivity.class);
            finish();
            startActivity(intent);
        }
        //inform user
        else{
            Toast.makeText(this, "Staff Member not found",
                    Toast.LENGTH_LONG).show();
        }

    }
}
