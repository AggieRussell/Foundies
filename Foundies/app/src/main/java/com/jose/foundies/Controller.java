package com.jose.foundies;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import static java.lang.System.out;
import java.util.ArrayList;
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

    //TODO: Set an item object

    public UserModel getUm() {
        return um;
    }

    public void setUm(UserModel um) {
        this.um = um;
    }

    private UserModel um;
    private LostModel lm;
    private FoundModel fm;
    private QuestionModel qm;
    private Contact user;
    private Item currentItem;

    boolean queryType; // true is lost item; false is found item

    public Controller(){
        um = new UserModel();
        lm = new LostModel();
        fm = new FoundModel();
        qm = new QuestionModel();
        // pre-load all question combinations from database
        qm.getQuestions();
        currentItem = new Item();
        queryType=false;
    }

    /* -------------------------------------- Define Query Type ------------------------------------------- */
    public void setQueryTypeLost() {
        queryType = true;
        FoundMap.setIsLost(true);
    }

    public void setQueryTypeFound() {
        queryType = false;
        FoundMap.setIsLost(false);
    }

    public boolean getQueryType() { return queryType; }

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
/*
    public void updateUserInfo(){
        um.updateUserInfo(user.getEmail(), user.testForUsername("jd@test.com"));
        System.out.println("New user email = " + user.getEmail());
    }
*/

    public void updateUserInfo() {
        um.updateUserInfo(user.getEmail(), user.testForUsername());
        System.out.println("New user email = " + user.getEmail());
    }

    public void updatedLastAccessed(){
        um.updateLastAccessed(user.getEmail(), user.updatedLastAccessed());
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
            this.user = user;
            currentItem.setUserID(user.getId());
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

    public void setCurrentUser(String username){
        Contact user = um.getUserByUsername(username);
        this.user = user;
    }

    public void setUserEmail(String email){
        user.setEmail(email);
        System.out.println("user email after being reset:  " + user.getEmail());
    }

    public String getNameOfUser(){
        return user.first_name + " " + user.last_name;
    }


    /* --------------------------------- Question Controller Functionality --------------------------------- */

    public ArrayList<String> getCategories() {
        return qm.getCategories();
    }

    public ArrayList<Question> getQuestions() { return qm.getQs(); }

    public ArrayList<String> getSubcategories(String category) {
        return qm.getSubcategories(category);
    }

    public ArrayList<String> getKinds() {
        return qm.getKinds(currentItem.getCategory(), currentItem.getSubcategory());
    }

    public ArrayList<String> getNames() {
        return qm.getNames(currentItem.getCategory(), currentItem.getSubcategory());
    }

    public ArrayList<ArrayList<String>> getChoices() {
        return qm.getChoices(currentItem.getCategory(), currentItem.getSubcategory());
    }


    /* --------------------------------- Item Controller Functionality ------------------------------------ */

    public Double getLatitude(){ return currentItem.getLatitude(); }

    public Double getLongitude(){ return currentItem.getLongitude(); }

    public ArrayList<String> getAnswers(){ return currentItem.getAnswers(); }

    public void sendSelections(String category, String subcategory) {
        currentItem.setSelections(category, subcategory);
    }

    public String getSubcategory() { return currentItem.getSubcategory(); }

    public String getCategory() { return currentItem.getCategory(); }

    public String getAnswersString() { return currentItem.getAnswersAsString(); }

    public String getUserId() { return currentItem.getUserID(); }

    /*----- Not currently being used, but may be used in the future if graphic buttons are used
            instead of spinners -- Only being used by Categories class, which is not being used -----*/
    public void setCategory(String c) { currentItem.setCategory(c); }

    public void setSubcategory(String s) { currentItem.setSubcategory(s); }

    public void setAnswers(ArrayList<String> answers, String extraDetails) {
        currentItem.setAnswers(answers);
        currentItem.setExtraDetails(extraDetails);
        for (int i=0; i<answers.size(); ++i){
            out.println(answers.get(i));
        }
        currentItem.printItem();
    }

    public void setLatLong(Double lat, Double lng){
        currentItem.setLatLong(lat, lng);
        currentItem.printItem();
    }



    /* --------------------------------- Found Controller Functionality ------------------------------------ */

    public void postFoundItem(){
        String jsonPost = fm.jsonFoundPost(currentItem);
        fm.postToFound(jsonPost);
    }

    public ArrayList<Item> getFoundItems(){
        return fm.getFoundItemWithCategories(currentItem);
    }

    public ArrayList<Item> getUsersFoundItems(){
        return fm.getFoundItemsByUsername(user.getEmail());
    }

    /* --------------------------------- Lost Controller Functionality ------------------------------------ */

    public ArrayList<Item> getUsersLostItems(){
        return lm.getLostItemsByUsername(user.getEmail());
    }

    public void postLostItem(){
        String jsonPost = lm.jsonLostPost(currentItem);
        lm.postToLost(jsonPost);
    }


    /* --------------------------------- Item Controller Functionality ------------------------------------ */

    public void setUserID(String userID) {
        currentItem.setUserID(userID);
    }
}
