package com.example.poplartree;

/**
 * Created by TMENCIN on 4/10/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StaffRegistration extends AppCompatActivity implements View.OnClickListener  {

    //Global Varibles
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword, editTextFirstName, editTextLastName, editTextConfrimPassword;
    private RadioButton rbAdmin,rbLandscaper;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_registration);

        //Setting up Firebase and Progress Dialog
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //Setting up the GUI
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPassword = (EditText) findViewById(R.id.EditTextpassword);
        editTextFirstName = (EditText) findViewById(R.id.EditTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.EditTextLastName);
        editTextConfrimPassword = (EditText) findViewById(R.id.EditTextConfrimPassword);
        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbLandscaper = (RadioButton) findViewById(R.id.rbLandscaper);

        //OnClickListener Register Button
        buttonRegister.setOnClickListener(this);

    }

    //Register User Method
    private void registerUser() {

        //Storing the data to variables
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String comfirmpassword = editTextConfrimPassword.getText().toString().trim();

        if(password.equals(comfirmpassword)) {
        //Storing the data to variables
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        //Storing the data to variables
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        //Storing the data to variables
        if (TextUtils.isEmpty(comfirmpassword)) {
            Toast.makeText(this, "Please comfirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        //Informing user
        progressDialog.setMessage("Registrating");
        progressDialog.show();
        //Signing out Admin to Create new FireBase User
        firebaseAuth.signOut();

        //Creating a new User
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Storing the data to variables
                    String email = editTextEmail.getText().toString().trim();
                    String firstname = editTextFirstName.getText().toString().trim();
                    String lastname = editTextLastName.getText().toString().trim();

                    //Creating Landscaper or Admin
                    if (rbLandscaper.isChecked())
                    {
                        //Updating data to Firebase
                        databaseRefrence = FirebaseDatabase.getInstance().getReference("/Staff/Landscaper");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        final StaffRegistrationO registration = new StaffRegistrationO("Landscaper",firstname,lastname,email,user.getUid());
                        databaseRefrence.child(user.getUid()).setValue(registration);
                        Intent intent = new Intent(StaffRegistration.this, MainActivity.class);
                        startActivity(intent);

                    }
                    else
                    {
                        //Updating data to Firebase
                        databaseRefrence = FirebaseDatabase.getInstance().getReference("/Staff/Admin");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        final StaffRegistrationO registration = new StaffRegistrationO("Admin",firstname,lastname,email,user.getUid());
                        databaseRefrence.child(user.getUid()).setValue(registration);
                        Intent intent = new Intent(StaffRegistration.this, MainActivity.class);
                        startActivity(intent);
                    }


                    //Updating user screen
                    Toast.makeText(StaffRegistration.this, "Register Succesfully", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(StaffRegistration.this, "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    }
    public void onClick(View view){
        //Register Button
        if(view == buttonRegister){
            registerUser();
        }
    }

}
