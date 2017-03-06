package com.jose.foundies;

/**
 * Created by josec on 3/5/2017.
 */

public class ItemPostings {

    int id;
    String type, color, year;

    //Item ID
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    //Type
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

    //Color
    public void setColor(String color){ this.color = color; }
    public String getColor(){
        return this.color;
    }

    //Year
    public void setYear(String year) { this.year = year; }
    public String getYear(){
        return this.year;
    }


}
