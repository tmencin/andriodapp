package com.example.poplartree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerWelcome extends Activity {

    //Global variables
    private Button buttonCustomerLogin;
    private Button buttonCustomerRegister;
    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        //initlizing buttons
        buttonCustomerLogin = (Button) findViewById(R.id.buttonCustomerLogin);
        buttonCustomerRegister = (Button) findViewById(R.id.buttonCustomerRegister);

        //Onclick listener for Login button for Customer
        buttonCustomerLogin.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcome.this, CustomerLogin.class);
                finish();
                startActivity(intent);
            }
        });

        //Onclick listener for Register button
        buttonCustomerRegister.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                //Intent to Customer Registation
                Intent intent = new Intent(CustomerWelcome.this, CustomerRegister.class);
                finish();
                startActivity(intent);
            }
        });

        //Back Button
        backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcome.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }

}
