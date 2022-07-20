package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.ContactDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Open Map Fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view_tag, MapsFragment.class, null)
                .commit();
    }
    //Handle Event Contact
    public void CreateContact(View view){
        TextView fullname = findViewById(R.id.txtFullname);
        TextView email = findViewById(R.id.txtEmail);
        TextView phone = findViewById(R.id.txtPhone);
        TextView content = findViewById(R.id.txtContent);

        if(fullname.length()==0){
            fullname.requestFocus();
            fullname.setError("Fullname cannot be blank");
        }else if(phone.length()==0){
            phone.requestFocus();
            phone.setError("Phone cannot be blank");
        }else if(content.length()==0){
            content.requestFocus();
            content.setError("Content cannot be blank");
        }

        if(fullname.getText().toString().trim().equals("")
                || phone.getText().toString().trim().equals("") || content.getText().toString().trim().equals("")){
        }else{
            ContactDatabaseAdapter.insertEntry(fullname.getText().toString().trim(),email.getText().toString(), phone.getText().toString(),content.getText().toString());
            Intent myIntent = new Intent(ContactActivity.this, MainActivity.class);
            ContactActivity.this.startActivity(myIntent);
        }
    }
}