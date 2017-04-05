package com.jose.foundies;

import android.app.Application;
import static java.lang.System.out;
import java.util.ArrayList;
import android.widget.EditText;


public class Controller extends Application {

    UserModel um;
    LostModel lm;
    FoundModel fm;
    QuestionModel qm;

    public Controller(){
        um = new UserModel();
        lm = new LostModel();
        fm = new FoundModel();
        qm = new QuestionModel();
        // pre-load all question combinations from database
        qm.getQuestions();
    }


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

    /* --------------------------------- Question Controller Functionality --------------------------------- */

    public ArrayList<String> getCategories() {
        return qm.getCategories();
    }

    public ArrayList<String> getSubcategories(String category) {
        return qm.getSubcategories(category);
    }

    public void sendSelections(String category, String subcategory) {
        qm.setSelections(category, subcategory);
    }

    public ArrayList<String> getKinds() {
        return qm.getKinds();
    }

    public ArrayList<String> getNames() {
        return qm.getNames();
    }

    public ArrayList<ArrayList<String>> getChoices() {
        return qm.getChoices();
    }


    /* --------------------------------- Found Controller Functionality ------------------------------------ */

    /* --------------------------------- Lost Controller Functionality ------------------------------------ */

}
