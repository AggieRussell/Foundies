package com.jose.foundies;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josec on 3/4/2017.
 */

public class Contact {

    public Contact(String first_name, String last_name, String email, String pass, String id, String last_accessed, String query_count_found, String query_count_lost) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.pass = pass;
        this.id = id;
        this.last_accessed = last_accessed;
        this.query_count_found = query_count_found;
        this.query_count_lost = query_count_lost;
    }

    public Contact(String first_name, String last_name, String email, String password){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.pass = password;
    }

    public Contact(){}

    String first_name, last_name, email, pass, id, last_accessed, query_count_found, query_count_lost;


    //User ID
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }

    //User Password

    public String getQuery_count_found() {
        return query_count_found;
    }

    public void setQuery_count_found(String query_count_found) {
        this.query_count_found = query_count_found;
    }

    public String getLast_accessed() {

        return last_accessed;
    }

    public void setLast_accessed(String last_accessed) {
        this.last_accessed = last_accessed;
    }

    public String getQuery_count_lost() {

        return query_count_lost;
    }

    public void setQuery_count_lost(String query_count_lost) {
        this.query_count_lost = query_count_lost;
    }

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

    public Map<String, String> createContactMap(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("first_name", this.first_name);
        contactMap.put("last_name", this.last_name);
        contactMap.put("username", this.email);
        contactMap.put("password", this.pass);
        contactMap.put("last_accessed", this.last_accessed);
        contactMap.put("query_count_found", this.query_count_found);
        contactMap.put("query_count_lost", this.query_count_lost);
        return contactMap;
    }

    public Map<String, String> updateQueryCounts(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("query_count_found", this.query_count_found);
        contactMap.put("query_count_lost", this.query_count_lost);
        return contactMap;
    }

    public Map<String, String> updateQueryCountLost(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("query_count_lost", this.query_count_lost);
        return contactMap;
    }

    public Map<String, String> updateQueryCountFound(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("query_count_found", this.query_count_found);
        return contactMap;
    }

    public Map<String, String> testForUsername(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("query_count_found", "0");
        contactMap.put("query_count_lost", "0");
        return contactMap;
    }

    public Map<String, String> updateLastAccessed(){
        Map<String, String> contactMap = new HashMap<>();
        contactMap.put("query_count_found", "0");
        contactMap.put("query_count_lost", "0");
        contactMap.put("last_accessed", Utility.getDate());
        return contactMap;
    }


}
