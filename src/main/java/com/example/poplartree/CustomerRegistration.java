package com.example.poplartree;



public class CustomerRegistration {

    public String type;
    public String firstname;
    public String lastname;
    public String email;
    public String phone_number;
    public String address;

    public CustomerRegistration(String type,String firstname,String lastname, String email, String phone_number, String address)
    {
        this.type = type;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
    }

}
