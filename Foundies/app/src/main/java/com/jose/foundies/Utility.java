package com.jose.foundies;

import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

import retrofit2.Retrofit;

/**
 * Created by kylepreston on 3/27/17.
 */

public final class Utility {

    private Utility() {
    }

    //Used to connect application to API hosted on Heroku
    public static HerokuService connectAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);
        return service;
    }

    //Used to create Unique IDs in the database
    public static String uniqueID(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    public static String getDate(){
        Date date = new Date();
        return date.toString();
    }
}
