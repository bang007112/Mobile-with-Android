package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.UserModel;
import com.google.android.material.bottomappbar.BottomAppBar;

import org.json.JSONException;

public class ProfileActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        //Get data of user
        UserModel user = UsersDatabaseAdapter.getUserByID(sp.getString("id",null));

        TextView textViewUsername = findViewById(R.id.displayUsername);
        TextView textViewEmail   = findViewById(R.id.displayEmail);
        TextView textViewPhone = findViewById(R.id.displayPhone);
        TextView textViewAddress = findViewById(R.id.displayAddress);

        if(user !=null){
            textViewUsername.setText(user.getUsername());
            textViewEmail.setText(user.getUseremail());
            textViewPhone.setText(user.getUserphone());
            textViewAddress.setText(user.getAddress());
        }
        //Handle Event Booking history Activity
        Button btn_booking_history = findViewById(R.id.btn_booking_history);
        btn_booking_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AppointHistoryActivity.class);
                startActivity(intent);
            }
        });
        //Handle Event Update Profile
        Button btn_update_profile = findViewById(R.id.btn_update_profile);
        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                try {
                    openDialog(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //Handle Event Button in Menu
        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.profile_menu){

                }
                else if(item.getItemId() == R.id.log_out_menu){
                    logout();
                }
                return false;
            }
        });
    }
    //Open Dialog to edit profile user
    public void openDialog(UserModel user) throws JSONException {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogview_update);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width,height);

        EditText edUsername =  dialog.findViewById(R.id.edittextUsername);
        EditText edPassword = dialog.findViewById(R.id.edittextPassword);
        EditText edPhone = dialog.findViewById(R.id.edittextPhone);
        EditText edEmail = dialog.findViewById(R.id.edittextEmail);
        EditText edAddress = dialog.findViewById(R.id.edittextAddress);


        Button btn_update = dialog.findViewById(R.id.btn_update);

        Button btn_close = dialog.findViewById(R.id.btn_close);

        if(user!=null){
            edUsername.setText(user.getUsername());
            edPassword.setText(user.getPassword());
            edPhone.setText(user.getUserphone());
            edEmail.setText(user.getUseremail());
            edAddress.setText(user.getAddress());
            //Handle Event Update
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edUsername.getText().toString().trim().equals("") || edPhone.getText().toString().trim().equals("") || edEmail.getText().toString() .equals("") || edPassword.getText().toString().trim() .equals("") || edAddress.getText().toString() .equals("")){
                        Toast.makeText(getApplicationContext(),"Input invalid",Toast.LENGTH_LONG).show();
                    }else{
                        UsersDatabaseAdapter.updateEntry(user.getID(),edUsername.getText().toString().trim(),edPhone.getText().toString().trim(),edEmail.getText().toString(),edPassword.getText().toString().trim(),edAddress.getText().toString());
                        finish();
                        startActivity(getIntent());
                    }
                }
            });
            //Handle Event Button Close
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }
    //Go To UserMain Activity
    public void userMain(View view){
        SharedPreferences sp =getSharedPreferences("Login", MODE_PRIVATE);
        if(sp.getString("username",null).equals("admin")){
            Intent myIntent = new Intent(ProfileActivity.this, ManageActivity.class);
            ProfileActivity.this.startActivity(myIntent);
        }else if(sp.getString("username",null)!=null){
            Intent myIntent = new Intent(ProfileActivity.this, UserMainActivity.class);
            ProfileActivity.this.startActivity(myIntent);
        }else {
            Intent myIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            ProfileActivity.this.startActivity(myIntent);
        }
    }
    //Logout and go to main activity
    public void logout(){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"Logout success", Toast.LENGTH_LONG ).show();
        sp.edit().remove("username").commit();
        sp.edit().remove("password").commit();
        Intent myIntent = new Intent(ProfileActivity.this, MainActivity.class);
        ProfileActivity.this.startActivity(myIntent);
    }
}