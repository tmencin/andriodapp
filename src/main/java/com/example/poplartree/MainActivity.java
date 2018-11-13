package com.example.poplartree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //global variables
    private TextView welcometextview;
    private Button ButtonloginCustomer;
    private Button buttonLoginStaff;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Removing if any Firebase user
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            firebaseAuth.signOut();
        }


        //Login in for customer
        ButtonloginCustomer = (Button) findViewById(R.id.ButtonloginCustomer);
        ButtonloginCustomer.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //intent for customer welcome class
                Intent intent = new Intent(MainActivity.this, CustomerWelcome.class);
                finish();
                startActivity(intent);
            }
        });

        //Login Landscaper button
        buttonLoginStaff = (Button) findViewById(R.id.buttonLoginStaff);
        buttonLoginStaff.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //intent to Staff Login Screen
              Intent intent = new Intent(MainActivity.this, StaffLogin.class);
                finish();
               startActivity(intent);
            }
        });

    }
}
