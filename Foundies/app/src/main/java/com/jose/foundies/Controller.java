package com.jose.foundies;

import android.app.Application;
import android.widget.EditText;

/**
 * Created by kylepreston on 4/3/17.
 */

public class Controller extends Application {

    UserModel um = new UserModel();
    LostModel lm = new LostModel();
    FoundModel fm = new FoundModel();



/* --------------------------------- User Controller Functionality ------------------------------------ */
    public String createUser(String first_name, String last_name, String email, String password){
        if (this.isValidEmail(email) && this.emailIsUnique(email)) {
            Contact c = new Contact(first_name, last_name, email, password);
            String userJson = um.jsonUserPost(c);
            um.postToAPI(userJson);
            return null;
        }else {
            return "Invalid Email";
        }

    }

    public boolean isEmptyField(EditText fname, EditText lname, EditText email, EditText pass) {
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
        Contact user = um.getUserByUsername(email);
        System.out.println(user);
        if(user == null){
            return true;
        }else{
            return false;
        }
    }

    public String checkCredentials(EditText email, EditText password){
        String emailstr = email.getText().toString();
        String passstr = password.getText().toString();

        Contact user = um.getUserByUsername(emailstr);
        if(user == null){
            return "No one by this email";
        }else if(user.getPass().equals(passstr)){
            return null;
        }else{
            return "Password does not match email";
        }
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /* --------------------------------- Found Controller Functionality ------------------------------------ */

    /* --------------------------------- Lost Controller Functionality ------------------------------------ */
}
