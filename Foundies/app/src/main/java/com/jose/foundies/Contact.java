package com.jose.foundies;

/**
 * Created by josec on 3/4/2017.
 */

public class Contact {

    int id;
    String first_name, last_name, email, pass;

    //User ID
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    //User Password
    public void setPassword(String pass){
        this.pass = pass;
    }
    public String getPass(){
        return this.pass;
    }

    //User First Name
    public void setFname(String first_name){
        this.first_name = first_name;
    }
    public String getFname(){
        return this.first_name;
    }

    //User Last Name
    public void setLname(String last_name) {
        this.last_name = last_name;
    }
    public String getLname(){
        return this.last_name;
    }

    //USer Email
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }



}
