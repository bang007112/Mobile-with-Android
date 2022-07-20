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

public class ManageActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        //Handle Event Button in Menu
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
    //go to manage appointment
    public void appointManagement(View view){
        Intent myIntent = new Intent(ManageActivity.this, AppointManagementActivity.class);
        ManageActivity.this.startActivity(myIntent);
    }
    //Go to contactManament
    public void contactManagement(View view){
        Intent myIntent = new Intent(ManageActivity.this, ContactManagementActivity.class);
        ManageActivity.this.startActivity(myIntent);
    }
    //Go to feedbackManagement
    public void feedbackManagement(View view){
        Intent myIntent = new Intent(ManageActivity.this, FeedbackManagementActivity.class);
        ManageActivity.this.startActivity(myIntent);
    }
    //Go to Logout
    public void logout(){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"Logout success", Toast.LENGTH_LONG ).show();
        sp.edit().remove("username").commit();
        sp.edit().remove("password").commit();
        Intent myIntent = new Intent(ManageActivity.this, MainActivity.class);
        ManageActivity.this.startActivity(myIntent);
    }
    //Go to profile
    public void profile(){
        Intent myIntent = new Intent(ManageActivity.this, ProfileActivity.class);
        ManageActivity.this.startActivity(myIntent);
    }
}