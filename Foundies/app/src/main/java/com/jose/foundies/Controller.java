package com.jose.foundies;

import android.app.Application;

/**
 * Created by christine on 3/27/17.
 */

public class Controller extends Application {

    Model m = new Model();

    public void setUser(String user) {
        m.setUser(user);
    }

    public String getUser() {
        return m.getUser();
    }

}
