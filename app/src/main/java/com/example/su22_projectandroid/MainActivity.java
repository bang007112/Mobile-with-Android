package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.su22_projectandroid.connectDb.AppointmentDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.ContactDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.FeedbackDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {
    //Create DataBaseAdapter Variable
    UsersDatabaseAdapter usersDatabaseAdapter;
    AppointmentDatabaseAdapter appointmentDatabaseAdapter;
    FeedbackDatabaseAdapter feedbackDatabaseAdapter;
    ContactDatabaseAdapter contactDatabaseAdapter;
    CallbackManager callbackManager;
    AccountManager accountManager;
    LoginButton loginButton;
    LoginManager loginManager;
    private static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Dependency injection for DataBaseAdapter
        appointmentDatabaseAdapter = new AppointmentDatabaseAdapter(getApplicationContext());
        usersDatabaseAdapter = new UsersDatabaseAdapter(getApplicationContext());
        feedbackDatabaseAdapter = new FeedbackDatabaseAdapter(getApplicationContext());
        contactDatabaseAdapter = new ContactDatabaseAdapter(getApplicationContext());
        accountManager = AccountManager.get(getApplicationContext());

        ImageView imageview_intro = findViewById(R.id.imageview_intro);
        ImageView imageview_intro2 = findViewById(R.id.imageview_intro2);

        AnimationDrawable animationDrawable = (AnimationDrawable) imageview_intro.getDrawable();
        AnimationDrawable animationDrawable2 = (AnimationDrawable) imageview_intro2.getDrawable();
        animationDrawable.start();
        animationDrawable2.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Go to Login Activity
    public void Login(View view){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        if(sp.getString("username",null)!=null){
            if (sp.getString("username",null).equals("admin")==false){
                Toast.makeText(getApplicationContext(),"Welcome to Shinyteeth", Toast.LENGTH_LONG ).show();
                Intent myIntent = new Intent(MainActivity.this, UserMainActivity.class);
                MainActivity.this.startActivity(myIntent);
            }else if(sp.getString("username",null).equals("admin")){
                Toast.makeText(getApplicationContext(),"Welcome to Shinyteeth", Toast.LENGTH_LONG ).show();
                Intent myIntent = new Intent(MainActivity.this, ManageActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        }
        else {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    }
    //Go to Register
    public void Register(View view){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);

        if (sp.getString("username",null)!=null){
            Toast.makeText(getApplicationContext(),"Welcome to Shinyteeth", Toast.LENGTH_LONG ).show();
            Intent myIntent = new Intent(MainActivity.this, UserMainActivity.class);
            MainActivity.this.startActivity(myIntent);
        }else {
            Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    }
    //Go to Contact
    public void Contact(View view){
        Intent myIntent = new Intent(MainActivity.this, ContactActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
    public void Logout(View view){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"Logout success", Toast.LENGTH_LONG ).show();
        sp.edit().remove("username").commit();
        sp.edit().remove("password").commit();
    }
}