package com.example.poplartree;


public class CustomerRequestJobFormO {

    public String firstname;
    public String lastname;
    public String email;
    public String phone_number;
    public String address;
    public String jobdescribtion;
    public String maxbudget;
    public String startdate;
    public String starttime;
    public String userID;

    public CustomerRequestJobFormO(String firstname,String lastname, String email, String phone_number, String address, String jobdescribtion, String maxbudget, String startdate, String starttime, String userID)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.jobdescribtion = jobdescribtion;
        this.maxbudget = maxbudget;
        this.startdate = startdate;
        this.starttime = starttime;
        this.userID =  userID;
    }

}
