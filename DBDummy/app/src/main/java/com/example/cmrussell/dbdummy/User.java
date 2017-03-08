package com.example.cmrussell.dbdummy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kylepreston on 3/8/17.
 */

public class User {
    String username;

    String first_name;

    String last_name;

    public User(String userName, String firstName, String lastName){
        this.username = userName;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public String getUserName(){
        return username;
    }
    public String getFirstName(){
        return first_name;
    }
    public String getLastName(){
        return last_name;
    }

    public void setUserName(String userName){
        this.username = userName;
    }
    public void setFirstName(String firstName){
        this.first_name = firstName;
    }
    public void setLastName(String lastName){
        this.last_name = lastName;
    }
}
