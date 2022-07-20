package com.example.su22_projectandroid.connectDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.su22_projectandroid.models.Feedback;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FeedbackDatabaseAdapter {
    static ArrayList<Feedback> feedbacks=new ArrayList<>();
    static final String DATABASE_NAME = "ShinyTeethUsersDatabase.db";
    static final String TABLE_NAME = "FEEDBACKS";
    static final int DATABASE_VERSION = 6;
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+TABLE_NAME+"( ID integer primary key autoincrement, feedbackContent text,date date, appointment_id integer not null, FOREIGN KEY (appointment_id) REFERENCES APPOINTMENTS(ID));";
    private static final String TAG = "AppointmentsDatabaseAdapter:";


    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;
    public  FeedbackDatabaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to open the Database
    public FeedbackDatabaseAdapter open() throws SQLException
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
    public static String insertEntry(String feedbackContent, String date, int appointment_id)
    {

        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("feedbackContent", feedbackContent);
            newValues.put("date", date);
            newValues.put("appointment_id", appointment_id);
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
    public static ArrayList<Feedback> getRows() throws JSONException {

        feedbacks.clear();
        Feedback feedback;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, null,null);
        while (projCursor.moveToNext()) {
            feedback =new Feedback();
            feedback.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            feedback.setAppointmentID(projCursor.getString(projCursor.getColumnIndexOrThrow("appointment_id")));
            feedback.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
            feedback.setFeedbackContent(projCursor.getString(projCursor.getColumnIndexOrThrow("feedbackContent")));


            feedback.setAppointmentModel(AppointmentDatabaseAdapter.getAppointByID(feedback.getAppointmentID()));

            feedbacks.add(feedback);
        }
        projCursor.close();
        return feedbacks;
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

    // method to Update an Existing Row in Table
    public static void  updateEntry(String ID,String appointment_id, String feedbackContent, String date)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("appointment_id", appointment_id);
        updatedValues.put("feedbackContent", feedbackContent);
        updatedValues.put("date", date);


        String where="ID = ?";
        db=dbHelper.getReadableDatabase();
        db.update(TABLE_NAME,updatedValues, where, new String[]{ID});
        db.close();
    }
}
