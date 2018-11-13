package com.example.poplartree;

/**
 * Created by TMENCIN on 4/10/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

    public class CustomerLogin extends AppCompatActivity implements View.OnClickListener {

        //Global Varibles
        private Button buttonSignIn, backbutton;
        private EditText editTextEmail;
        private EditText editTextPassword;
        private TextView textViewSignup;
        private ProgressDialog progressDialog;
        private FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customer_login);

            firebaseAuth = FirebaseAuth.getInstance();
            progressDialog = new ProgressDialog(this);

            buttonSignIn = (Button) findViewById(R.id.buttonSignin);
            editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
            editTextPassword = (EditText) findViewById(R.id.EditTextpassword);
            textViewSignup = (TextView)findViewById(R.id.TextViewSignup);

            //setting onClickListners
            buttonSignIn.setOnClickListener(this);
            textViewSignup.setOnClickListener(this);

            //Back Button
            backbutton = (Button) findViewById(R.id.backbutton);
            backbutton.setOnClickListener(new View.OnClickListener() {
                // overridden method to handle a button click
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerLogin.this, CustomerWelcome.class);
                    finish();
                    startActivity(intent);

                }
            });

        }

        private void UserLogin(){
            //Getting data and store it in varibles
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

            //Displaying info to user
            progressDialog.setMessage("Login in");
            progressDialog.show();

            //Signing in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult>task){
                            progressDialog.dismiss();

                            if(task.isSuccessful()){
                              //start the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                            } else {
                                Toast.makeText(CustomerLogin.this, "Email and Password is not found please check details and try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        public void onClick(View view) {
            //Sign in Button
            if(view == buttonSignIn){
               UserLogin();
            }
            //Link to Sign up Customer
            if(view == textViewSignup){
                Intent intent = new Intent(CustomerLogin.this, CustomerRegister.class);
                startActivity(intent);
            }
        }

    }

