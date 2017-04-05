package com.jose.foundies;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class PostClass extends Thread{
    UserModel um = new UserModel();
    LostModel lm = new LostModel();
    FoundModel fm = new FoundModel();
    Contact c = null;
    public PostClass(Contact c){
        this.c = c;
    }
    public void run (){
        um.postToAPI(c);
    }
}


public class Controller extends Application {
    private String SHAHash;
    public static int NO_OPTIONS=0;

    UserModel um = new UserModel();
    LostModel lm = new LostModel();
    FoundModel fm = new FoundModel();



    /* --------------------------------- User Controller Functionality ------------------------------------ */
    
    public String createUser(String first_name, String last_name, String email, String password){
        if (this.isValidEmail(email) && this.emailIsUnique(email)) {

            String passwordHash = computeSHAHash(password);
            Contact c = new Contact(first_name, last_name, email, passwordHash);
            PostClass post = new PostClass(c);
            post.start();
            //um.postToAPI(c);
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
        if(user == null){
            return true;
        }else{
            return false;
        }
    }

    public String checkCredentials(EditText email, EditText password){
        String emailstr = email.getText().toString();
        String passstr = password.getText().toString();
        String hashedPassword = computeSHAHash(passstr);
        Contact user = um.getUserByUsername(emailstr);
        if(user == null){
            return "No one by this email";
        }else if(user.getPass().equals(hashedPassword)){
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

    private static String convertToHex(byte[] data) throws java.io.IOException
    {


        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }


    public String computeSHAHash(String password)
    {
        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash=convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SHAHash.trim();

    }

    /* --------------------------------- Found Controller Functionality ------------------------------------ */

    /* --------------------------------- Lost Controller Functionality ------------------------------------ */

}
