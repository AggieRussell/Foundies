package com.jose.foundies;

/**
 * Created by kylepreston on 3/27/17.
 */

public class UserController {
    //Where the data will be manipulated and sent to the views
    public void UserController(){}

    public boolean createUser(String first_name, String last_name, String email, String password){

        Contact c = new Contact(first_name, last_name, email, password);
        //String userJson = UserModel.jsonUserPost(c);
        //UserModel.postToAPI(userJson);

        return true;
    }


}
