package com.jose.foundies;

import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kylepreston on 3/27/17.
 */

public class UserController {
    UserModel model = new UserModel();
    //Where the data will be manipulated and sent to the views
    public void UserController(){}

    public boolean createUser(String first_name, String last_name, String email, String password){

        Contact c = new Contact(first_name, last_name, email, password);
        String userJson = model.jsonUserPost(c);
        model.postToAPI(userJson);

        return true;
    }

    public boolean isEmptyField( EditText fname, EditText lname, EditText email, EditText pass) {
        if (fname.getText().toString().length() <= 0) {
            return false;
        }
        else if (lname.getText().toString().length() <= 0) {
            return false;
        }
        else if (email.getText().toString().length() <= 0) {
            return false;
        }
        else if (pass.getText().toString().length() <= 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean emailIsUnique(String email){
        Contact user = model.getUserByUsername(email);
        System.out.println(user);
        if(user == null){
            return true;
        }else{
            return false;
        }
    }


}
