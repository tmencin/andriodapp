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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CustomerRegister extends AppCompatActivity implements View.OnClickListener  {

    //Global Varibles
    private Button buttonRegister, backbutton;
    private EditText editTextPassword, editTextFirstName, editTextLastName,editTextEmail, editTextAddress1,editTextAddress2,editTextAddress3, editTextPhoneNumber, editTextConfrimPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Customer");


        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPassword = (EditText) findViewById(R.id.EditTextpassword);
        textViewSignin = (TextView)findViewById(R.id.TextViewSignin);
        editTextFirstName = (EditText) findViewById(R.id.EditTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.EditTextLastName);
        editTextAddress1 = (EditText) findViewById(R.id.EditTextAddress1);
        editTextAddress2 = (EditText) findViewById(R.id.EditTextAddress2);
        editTextAddress3 = (EditText) findViewById(R.id.EditTextAddress3);
        editTextPhoneNumber = (EditText) findViewById(R.id.EditTextPhoneNumber);
        editTextConfrimPassword = (EditText) findViewById(R.id.EditTextConfrimPassword);

        //onclick Listeners
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        //Back Button
        backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegister.this, CustomerWelcome.class);
                finish();
                startActivity(intent);

            }
        });

    }

    private void registerUser() {
        //Storing user info to varibles
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String comfirmpassword = editTextConfrimPassword.getText().toString().trim();
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String phone_number = editTextPhoneNumber.getText().toString().trim();
        //Getting Address from three edit text and separtate them with ", "
        String address = editTextAddress1.getText().toString().trim() + ", " + editTextAddress2.getText().toString().trim() + ", " + editTextAddress3.getText().toString().trim();

        if(password.equals(comfirmpassword)) {
            //Checking If fields are empty let user know
            if (TextUtils.isEmpty(email)) {
                //letting user know if email empty
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                //letting user know if password is empty
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(comfirmpassword)) {
                //letting user know if comfirm password is empty
                Toast.makeText(this, "Please comfirm password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(firstname)) {
                //letting user know if First name empty
                Toast.makeText(this, "Please enter, First Name ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(lastname)) {
                //letting user know if Last name empty
                Toast.makeText(this, "Please enter, Last Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phone_number)) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(address)) {
                //letting user know if Address is empty
                Toast.makeText(this, "Please enter, Address", Toast.LENGTH_SHORT).show();
                return;
            }


            if (editTextAddress1.getText().toString().trim().length() == 0) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Address Line 1", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextAddress2.getText().toString().trim().length() == 0) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Address Line 2", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextAddress3.getText().toString().trim().length() == 0) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Address Line 3", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phone_number)) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phone_number)) {
                //letting user know if phone number empty
                Toast.makeText(this, "Please enter, Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            //Creating a CustomerRegister Object storing in Firebase
            //CustomerRegistration(String type,String firstname,String lastname, String email, String phone_number, String address)
            final CustomerRegistration registration = new CustomerRegistration("Customer", firstname, lastname, email, phone_number, address);

            //Displaying info to user
            progressDialog.setMessage("Registrating");
            progressDialog.show();

            //Creating creating user
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        //Storing into in firebase
                        databaseRefrence.child(user.getUid()).setValue(registration);
                        //Displaying info to user
                        Toast.makeText(CustomerRegister.this, "Register Succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CustomerRegister.this, CustomerActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        //Displaying info to user
                        Toast.makeText(CustomerRegister.this, "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(CustomerRegister.this, "Retry Password, doesnt match", Toast.LENGTH_LONG).show();
        }

    }
    //Setting onClick
    public void onClick(View view){
        //Setting button Register
        if(view == buttonRegister){
            registerUser();
        }

        //Setting Login in Customer
        if(view == textViewSignin){
            Intent intent = new Intent(CustomerRegister.this, CustomerLogin.class);
            finish();
            startActivity(intent);
        }
    }

}
