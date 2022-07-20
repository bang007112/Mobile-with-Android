package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;

public class UserMainActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        //Handle Event Button in Menu
        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.profile_menu){
                    profile();
                }
                else if(item.getItemId() == R.id.log_out_menu){
                    logout();
                }
                return false;
            }
        });

    }
    //Logout and go to Main Activity
    public void logout(){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"Logout success", Toast.LENGTH_LONG ).show();
        sp.edit().remove("username").commit();
        sp.edit().remove("password").commit();
        Intent myIntent = new Intent(UserMainActivity.this, MainActivity.class);
        UserMainActivity.this.startActivity(myIntent);
    }
    //Go to Profile
    public void profile(){
        Intent myIntent = new Intent(UserMainActivity.this, ProfileActivity.class);
        UserMainActivity.this.startActivity(myIntent);
    }
    //Go to booking
    public void booking(View view){
        Intent myIntent = new Intent(UserMainActivity.this, BookingActivity.class);
        UserMainActivity.this.startActivity(myIntent);
    }
    //Go to feedback
    public void feedback(View view){
        Intent myIntent = new Intent(UserMainActivity.this, AppointHistoryActivity.class);
        UserMainActivity.this.startActivity(myIntent);
    }
    //Go to UserMain
    public void userMain(View view){
        SharedPreferences sp =getSharedPreferences("Login", MODE_PRIVATE);
        if(sp.getString("username",null).equals("admin")){
            Intent myIntent = new Intent(UserMainActivity.this, ManageActivity.class);
            UserMainActivity.this.startActivity(myIntent);
        }else if(sp.getString("username",null)!=null){

        }else {
            Intent myIntent = new Intent(UserMainActivity.this, LoginActivity.class);
            UserMainActivity.this.startActivity(myIntent);
        }
    }
}