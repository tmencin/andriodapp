package com.example.poplartree;

/**
 * Created by TMENCIN on 7/24/2018.
 */

public class LandscaperNotificationFormO {

    public String customer_firstname;
    public String customer_lastname;
    public String customer_email;
    public String customer_phone_number;
    public String customer_address;
    public String customer_jobdescribtion;
    public String completed_date;
    public String completed_time;
    public String startdate;
    public String starttime;
    public String price;
    public String landscaperID;
    public String customerID;

    public LandscaperNotificationFormO(String customer_firstname,String customer_lastname, String customer_email, String customer_phone_number, String customer_address, String customer_jobdescribtion,String completed_date, String completed_time, String startdate, String starttime, String price, String customerID, String landscaperID)
    {
        this.customer_firstname = customer_firstname;
        this.customer_lastname = customer_lastname;
        this.customer_email = customer_email;
        this.customer_phone_number = customer_phone_number;
        this.customer_address = customer_address;
        this.customer_jobdescribtion = customer_jobdescribtion;
        this.completed_date =completed_date;
        this.completed_time = completed_time;
        this.startdate = startdate;
        this.starttime = starttime;
        this.price = price;
        this.customerID = customerID;
        this.landscaperID = landscaperID;
    }
}
