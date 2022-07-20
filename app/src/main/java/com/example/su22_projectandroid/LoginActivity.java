package com.example.su22_projectandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.UserModel;

public class LoginActivity extends AppCompatActivity {
    private AccountManager accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    //Handle Event Login
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Login(View view){
        TextView mUserName = (TextView) findViewById(R.id.txtUsername);
        TextView mUserPassword = (TextView) findViewById(R.id.txtPassword);
        if(mUserName.getText().toString().trim().equals("") || mUserPassword.getText().toString().trim().equals("")){

        }else {
            UserModel user = UsersDatabaseAdapter.Login(mUserName.getText().toString(),mUserPassword.getText().toString());
            if (user != null){
                //use sharePreference to Save Login data
                SharedPreferences sp =getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("username", mUserName.getText().toString() );
                Ed.putString("password", mUserPassword.getText().toString());
                Ed.putString("id", user.getID());
                Ed.commit();

                Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_LONG ).show();
                if(sp.getString("username",null).equals("admin")==true){
                    Intent myIntent = new Intent(LoginActivity.this, ManageActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }else {
                    Intent myIntent = new Intent(LoginActivity.this, UserMainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }

            }else {
                Toast.makeText(getApplicationContext(),"Login Fail", Toast.LENGTH_LONG );
            }
        }
    }
    //Go to Register Activity
    public void Register(View view){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);

        if (sp.getString("username",null)!=null){
            Toast.makeText(getApplicationContext(),"Welcome to Shinyteeth", Toast.LENGTH_LONG ).show();
        }else {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
    }
}