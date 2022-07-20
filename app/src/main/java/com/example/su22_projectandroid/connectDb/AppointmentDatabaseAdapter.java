package com.example.su22_projectandroid.connectDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.su22_projectandroid.models.AppointmentModel;
import com.example.su22_projectandroid.models.UserModel;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class AppointmentDatabaseAdapter {
    static ArrayList<AppointmentModel> appointments=new ArrayList<>();
    static final String DATABASE_NAME = "ShinyTeethUsersDatabase.db";
    static final String TABLE_NAME = "APPOINTMENTS";
    static final int DATABASE_VERSION = 6;
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+TABLE_NAME+"( ID integer primary key autoincrement, appointmentContent text, date date,hour text,user_id integer not null, FOREIGN KEY (user_id) REFERENCES USERS (ID));";
    private static final String TAG = "AppointmentsDatabaseAdapter:";


    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;
    public  AppointmentDatabaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to open the Database
    public AppointmentDatabaseAdapter open() throws SQLException
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
    public static String insertEntry(int user_id, String appointmentContent, String hour, String date)
    {
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("user_id", user_id);
            newValues.put("appointmentContent", appointmentContent);
            newValues.put("hour", hour);
            newValues.put("date", String.valueOf(date));
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
    public static ArrayList<AppointmentModel> getRows() throws JSONException {

        appointments.clear();
        AppointmentModel appointment;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, null,null);
        while (projCursor.moveToNext()) {
            appointment =new AppointmentModel();
            appointment.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            appointment.setAppointmentContent(projCursor.getString(projCursor.getColumnIndexOrThrow("appointmentContent")));
            appointment.setHour(projCursor.getString(projCursor.getColumnIndexOrThrow("hour")));
            appointment.setUserID(projCursor.getString(projCursor.getColumnIndexOrThrow("user_id")));
            UserModel userModel = UsersDatabaseAdapter.getUserByID(appointment.getUserID());

            appointment.setUserModel(userModel);
            appointment.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
            appointments.add(appointment);
        }
        projCursor.close();
        return appointments;
    }

    // method to delete a Record in Tbale using Primary Key Here it is ID
    public static int deleteEntry(String ID)
    {
        String where="ID=?";
        int numberOFEntriesDeleted= db.delete(TABLE_NAME, where, new String[]{ID}) ;
        return numberOFEntriesDeleted;
    }
    public static ArrayList<AppointmentModel> getAppointByUserID(String ID) {
        AppointmentModel appointment;
        appointments.clear();
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, null,null);

        while (projCursor.moveToNext()) {
            appointment =new AppointmentModel();
            appointment.setUserID(projCursor.getString(projCursor.getColumnIndexOrThrow("user_id")));
            if(appointment.getUserID().equals(ID)){
                appointment.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
                appointment.setAppointmentContent(projCursor.getString(projCursor.getColumnIndexOrThrow("appointmentContent")));
                appointment.setHour(projCursor.getString(projCursor.getColumnIndexOrThrow("hour")));

                appointment.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
                appointments.add(appointment);
            }
        }
        return appointments;
    }
    public static AppointmentModel getAppointByID(String ID) {
        AppointmentModel appointment;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, null,null);

        while (projCursor.moveToNext()) {
            appointment =new AppointmentModel();
            appointment.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            if(appointment.getID().equals(ID)){
                appointment.setAppointmentContent(projCursor.getString(projCursor.getColumnIndexOrThrow("appointmentContent")));
                appointment.setHour(projCursor.getString(projCursor.getColumnIndexOrThrow("hour")));
                appointment.setUserID(projCursor.getString(projCursor.getColumnIndexOrThrow("user_id")));
                appointment.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
                appointment.setUserModel(UsersDatabaseAdapter.getUserByID(appointment.getUserID()));
                return appointment;
            }
        }
        return null;
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
    public static void  updateEntry(String ID,String user_id, String apppontmentContent, String hour, String date)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("user_id", user_id);
        updatedValues.put("appointmentContent", apppontmentContent);
        updatedValues.put("hour", hour);
        updatedValues.put("date", date);


        String where="ID = ?";
        db=dbHelper.getReadableDatabase();
        db.update(TABLE_NAME,updatedValues, where, new String[]{ID});
        db.close();
    }
    public static ArrayList<AppointmentModel> getRowsByDateDES() throws JSONException {

        appointments.clear();
        AppointmentModel appointment;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, "date DESC",null);
        while (projCursor.moveToNext()) {
            appointment =new AppointmentModel();
            appointment.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            appointment.setAppointmentContent(projCursor.getString(projCursor.getColumnIndexOrThrow("appointmentContent")));
            appointment.setHour(projCursor.getString(projCursor.getColumnIndexOrThrow("hour")));
            appointment.setUserID(projCursor.getString(projCursor.getColumnIndexOrThrow("user_id")));
            UserModel userModel = UsersDatabaseAdapter.getUserByID(appointment.getUserID());

            appointment.setUserModel(userModel);
            appointment.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
            appointments.add(appointment);
        }
        projCursor.close();
        return appointments;
    }
    public static ArrayList<AppointmentModel> getRowsOfUserByDateDES(String user_id) throws JSONException {

        appointments.clear();
        AppointmentModel appointment;
        db=dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null,null, null, null, "date DESC",null);
        while (projCursor.moveToNext()) {
            appointment =new AppointmentModel();
            appointment.setID(projCursor.getString(projCursor.getColumnIndexOrThrow("ID")));
            appointment.setUserID(projCursor.getString(projCursor.getColumnIndexOrThrow("user_id")));
            UserModel userModel = UsersDatabaseAdapter.getUserByID(appointment.getUserID());
            if(userModel.getID().equals(user_id)){
                appointment.setAppointmentContent(projCursor.getString(projCursor.getColumnIndexOrThrow("appointmentContent")));
                appointment.setHour(projCursor.getString(projCursor.getColumnIndexOrThrow("hour")));
                appointment.setUserModel(userModel);
                appointment.setDate(projCursor.getString(projCursor.getColumnIndexOrThrow("date")));
                appointments.add(appointment);
            }
        }
        projCursor.close();
        return appointments;
    }

}
