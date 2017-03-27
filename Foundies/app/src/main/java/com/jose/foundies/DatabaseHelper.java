package com.jose.foundies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josec on 3/4/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Columns of Login Info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_EMAIL = "email";

    //Columns of Found Items
    private static final String TABLE_FOUND_ITEMS = "found";
    private static final String COLUMN_ITEM_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";

    SQLiteDatabase db;

    //Creates Contacts Table
    private static final String CREATE_TABLE_CONTACTS ="create table contacts (id integer primary key not null, " +
        "firstname text not null, lastname text not null, email text not null, password integer not null);";

    //Creates Found postings Table
    private static final String CREATE_TABLE_FOUND_ITEMS ="create table found (id integer primary key not null, " +
            "type text not null, color text not null, year text not null);";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    //Populate contact table
    public void insertContact(Contact c){
        db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        values.put(COLUMN_ID, count);
        values.put(COLUMN_FIRSTNAME, c.getFname());
        values.put(COLUMN_LASTNAME, c.getLname());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_PASSWORD, c.getPass());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    //Populate found items table
    public void insertItem(ItemPostings i){
        db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        String query = "select * from found";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        values.put(COLUMN_ID, count);
        values.put(COLUMN_TYPE, i.getType());
        values.put(COLUMN_COLOR, i.getColor());
        values.put(COLUMN_YEAR, i.getYear());
        values.put(COLUMN_LAT, i.getLat());
        values.put(COLUMN_LNG, i.getLng());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Funtion: Check to see if email/password match records
    public String searchPass(String email){
        db = this.getReadableDatabase();
        String query = "select email, password from " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b = "not found";
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                b = cursor.getString(1);
                if(a.equals(email)){
                    b = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return b;
    }

    //Retrieve Data from SQLite DB
    public Cursor getData(){
        db = this.getReadableDatabase();
        Cursor results = db.rawQuery("select * from " + TABLE_CONTACTS,  null);
        return results;
    }

    public void updateData(Contact c){
        db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_PASSWORD, c.getPass());
        db.update(TABLE_CONTACTS, values, "Email = ?" , new String[] {c.getEmail()});
    }

    //Delete Entry
    public Integer deleteData (String email){
        db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, "EMAIL = ?", new String[] {email});
    }

    //SQL Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_FOUND_ITEMS);
        this.db = db;
    }

    //SQL Clear Previous Tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP_TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP_TABLE IF EXISTS " + TABLE_FOUND_ITEMS);
        this.onCreate(db);
    }
}
