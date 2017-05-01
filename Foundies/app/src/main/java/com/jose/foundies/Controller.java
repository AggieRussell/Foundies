package com.jose.foundies;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import static java.lang.System.out;

import java.text.ParseException;
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

class PostItemClass extends Thread{
    FoundModel fm = new FoundModel();
    Item t = null;
    public PostItemClass(Item t){
        this.t = t;
    }
    public void run (){
        String jsonPost = fm.jsonFoundPost(t);
        fm.postToFound(jsonPost);
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

    public boolean isLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    private boolean locationEnabled = false;
    Contact user;

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

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
    
    public String createUser(String first_name, String last_name, String email, String password, String last_accessed, String query_found, String query_lost){
        if (this.isValidEmail(email) && this.emailIsUnique(email)) {

            String passwordHash = computeSHAHash(password);
            user = new Contact(first_name, last_name, email, passwordHash, Utility.uniqueID(), last_accessed, query_found, query_lost);
            PostClass post = new PostClass(user);
            um.setUser(user);
            currentItem.setUserID(user.getId());
            post.start();
            System.out.println("THIS IS THE USER EMAIL: " + user.getEmail());
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
            this.user = user;
            um.setUser(user);
            currentItem.setUserID(user.getId());
            return null;
        }else{
            return "Password does not match email";
        }
    }

    public void updateLastAccessed(){
        //System.out.println("THis is the check: ");
        if(!(user.last_accessed.equals(Utility.getDate()))) {
            um.updateLastAccessed(user.getEmail(), user.updateLastAccessed());
        }
    }

    //Once the user has had it's elements updated, this function will PUT those changes in the database
    public void updateUserInfo() {
        um.updateUserInfo(user.getEmail(), user.updateUserInfo());
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
        um.setUser(user);
    }

    public void setUserEmail(String email){
        user.setEmail(email);
    }

    public String getNameOfUser(){
        return user.first_name + " " + user.last_name;
    }

    public Boolean checkQueryLostCount() {
        if(user.getQuery_count_lost() == null) {
            user.setQuery_count_lost("0");
            um.updateQueryCountLost(user.getEmail(), user.updateQueryCountLost());
            return true;
        }else if(Integer.parseInt(user.getQuery_count_lost()) < 3){
            int count = Integer.parseInt(user.getQuery_count_lost()) + 1;
            user.setQuery_count_lost(""+count);
            um.updateQueryCountLost(user.getEmail(), user.updateQueryCountLost());
            return true;
        }else {
            return false;
        }
    }

    public Boolean checkQueryFoundCount() {
        if(user.getQuery_count_found() == null) {
            user.setQuery_count_found("0");
            um.updateQueryCountFound(user.getEmail(), user.updateQueryCountFound());
            return true;
        }else if(Integer.parseInt(user.getQuery_count_found()) < 3){
            int count = Integer.parseInt(user.getQuery_count_found()) + 1;
            user.setQuery_count_found(""+count);
            um.updateQueryCountFound(user.getEmail(), user.updateQueryCountFound());
            return true;
        }
        return false;
    }

    public Contact getUser(){
        return this.user;
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

    public ArrayList<ArrayList<String>> getSubsequentChoices(int i) {
        return qm.getSubsequentChoices(getChoices(), i);
    }

    /* --------------------------------- Item Controller Functionality ------------------------------------ */

    public Double getLatitude(){ return currentItem.getLatitude(); }

    public Double getLongitude(){ return currentItem.getLongitude(); }

    public ArrayList<String> getAnswers(){ return currentItem.getAnswers(); }

    public void sendSelections(String category, String subcategory, String date) {
        currentItem.setSelections(category, subcategory, date);
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
        PostItemClass post = new PostItemClass(currentItem);
        post.start();
    }

    public ArrayList<Item> getFoundItems() throws ParseException {
        return fm.getFoundItemWithCategories(currentItem);
    }

    public ArrayList<Item> getUsersFoundItems(){
        return fm.getFoundItemsByUsername(user.getEmail());
    }

    public void deleteFoundItem(Item item){
        fm.deleteFoundItem(item);
    }

    /* --------------------------------- Lost Controller Functionality ------------------------------------ */

    public ArrayList<Item> getUsersLostItems(){
        return lm.getLostItemsByUsername(user.getEmail());
    }

    //Used for filtering the matches of a found item and the lost items
    public ArrayList<Item> getLostItems(){
        return lm.getLostItemWithCategories(currentItem);
    }

    public void postLostItem(){
        String jsonPost = lm.jsonLostPost(currentItem);
        lm.postToLost(jsonPost);
    }

    public void deleteLostItem(Item item){
        lm.deleteLostItem(item);
    }


    /* --------------------------------- Item Controller Functionality ------------------------------------ */

    public void setUserID(String userID) {
        currentItem.setUserID(userID);
    }
}
