package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.FeedbackDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;

import java.time.LocalDateTime;
import android.util.Patterns;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    //Handle Register Activity
    public void Register(View view){
        TextView userEmailTxtView = findViewById(R.id.txtEmail);
        TextView username = findViewById(R.id.txtUsername);
        TextView userPasswordTxtView = findViewById(R.id.txtPassword);
        TextView addressTxtView = findViewById(R.id.txtAddress);
        TextView phoneTxtView = findViewById(R.id.txtPhone);
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String email = userEmailTxtView.getText().toString().trim();
        String phone = phoneTxtView.getText().toString().trim();

        if(userEmailTxtView.length()==0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmailTxtView.requestFocus();
            userEmailTxtView.setError("Email is invalid");
        }else if(username.length()==0){
            username.requestFocus();
            username.setError("User name is invalid");
        }else if(userPasswordTxtView.length()==0){
            userPasswordTxtView.requestFocus();
            userPasswordTxtView.setError("Password is invalid");
        }else if(phoneTxtView.length()!=10){
            phoneTxtView.requestFocus();
            phoneTxtView.setError("Phone is invalid");
        }else{
            UsersDatabaseAdapter.insertEntry(username.getText().toString().trim(),userEmailTxtView.getText().toString(), userPasswordTxtView.getText().toString(),phoneTxtView.getText().toString(),addressTxtView.getText().toString());
            Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
            RegisterActivity.this.startActivity(myIntent);
        }
    }
}