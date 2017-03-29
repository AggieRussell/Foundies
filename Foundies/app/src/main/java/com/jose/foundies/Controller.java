package com.jose.foundies;

import android.app.Application;

/**
 * Created by christine on 3/27/17.
 */

public class Controller extends Application {

    UserModel um = new UserModel();
    LostModel lm = new LostModel();
    FoundModel fm = new FoundModel();

    public boolean createUser(String first_name, String last_name, String email, String password){
        return um.createUser(first_name, last_name, email, password);
    }

}
