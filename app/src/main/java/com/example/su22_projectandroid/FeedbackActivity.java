package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.FeedbackDatabaseAdapter;

import java.util.Calendar;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }
    //Handle Event and Create FeedBack
    public void Feedback(View view){
        TextView textViewContent = findViewById(R.id.txtContent);
        if(textViewContent.length()==0){
            textViewContent.requestFocus();
            textViewContent.setError("Content cannot be blank");
        }else{
            Intent intent = getIntent();
            String appointId = intent.getStringExtra("appointId");
            FeedbackDatabaseAdapter.insertEntry(textViewContent.getText().toString(), Calendar.getInstance().getTime().toLocaleString(),Integer.parseInt(appointId));
            gotoBooking();
        }
    }
    public void gotoBooking(){
        Intent intent = new Intent(this,UserMainActivity.class);
        startActivity(intent);
    }
}