package com.example.su22_projectandroid.connectDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.su22_projectandroid.models.Contact;
import com.example.su22_projectandroid.models.UserModel;

import org.json.JSONException;

import java.util.ArrayList;

public class ContactDatabaseAdapter {
    static ArrayList<Contact> contacts=new ArrayList<>();
    static final String DATABASE_NAME = "ShinyTeethUsersDatabase.db";
    static final String TABLE_NAME = "CONTACTS";
    static final int DATABASE_VERSION = 6;
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+TABLE_NAME+"( ID integer primary key autoincrement,fullname  text,email  text,phone text, content text);";
    private static final String TAG = "ContactsDatabaseAdapter:";


    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;
    public  ContactDatabaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to open the Database
    public  ContactDatabaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close()
    {
        db.close();
    }

    // method returns an Instance of the Database
    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    // method to insert a record in Table
    public static String insertEntry(String fullname, String email, String phone, String content)
    {

        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("fullname", fullname);
            newValues.put("email", email);
            newValues.put("phone",phone);
            newValues.put("content",content);

            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result=db.insert(TABLE_NAME, null, newValues);
            Log.i("Row Insert Result ", String.valueOf(result));
            db.close();

        }catch(Exception ex) {
        }
        return "ok";
    }

    // method to get all Rows Saved in Table
    public static ArrayList<Contact> getRows() throws JSONException {
        contacts.clear();
        Contact contact;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, null,null);
        while (projCursor.moveToNext()) {
            contact=new Contact();
            contact.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            contact.setFullname(projCursor.getString(projCursor.getColumnIndexOrThrow("fullname")));
            contact.setEmail(projCursor.getString(projCursor.getColumnIndexOrThrow("email")));
            contact.setPhone(projCursor.getString(projCursor.getColumnIndexOrThrow("phone")));
            contact.setContent(projCursor.getString(projCursor.getColumnIndexOrThrow("content")));
            contacts.add(contact);
        }
        projCursor.close();
        return contacts;
    }

    // method to delete a Record in Tbale using Primary Key Here it is ID
    public static int deleteEntry(String ID)
    {
        String where="ID=?";
        int numberOFEntriesDeleted= db.delete(TABLE_NAME, where, new String[]{ID}) ;
        return numberOFEntriesDeleted;
    }

    // method to get Count of Toatal Rows in Table
    public static int getRowCount()
    {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor.getCount();
    }

    // method to Truncate/ Remove All Rows in Table
    public static void truncateTable()
    {
        db=dbHelper.getReadableDatabase();
        db.delete(TABLE_NAME, "1", null);
        db.close();
    }
}
