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

import com.example.su22_projectandroid.connectDb.AppointmentDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.UserModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BookingActivity extends AppCompatActivity {
    private String date;
    Calendar calendar;
    int newHour;
    int newMinute;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        //Handle event buttons in Menu
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
        //Handle Event Booking and then open dialog
            Button btn_booking = findViewById(R.id.btn_booking);
            btn_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(calendar.getTime()!=null) {
                        TextView txtContent = findViewById(R.id.txtContentBooking);
                        if (txtContent.length() == 0) {
                            txtContent.requestFocus();
                            txtContent.setError("content cannot blank");
                        }
                        String hour = String.valueOf(newHour) + ":" + String.valueOf(newMinute);
                        Date date = calendar.getTime();
                        date.setHours(newHour);
                        date.setMinutes(newMinute);
                        try {
                            openDialog(date.toLocaleString(), hour, txtContent.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
    //Not Used
    public void Booking(View view) throws JSONException, InterruptedException {

        if(calendar.getTime()!=null){
            TextView txtContent = findViewById(R.id.txtContentBooking);
            if(txtContent.length()==0){
                txtContent.requestFocus();
                txtContent.setError("content cannot blank");
            }
            String hour = String.valueOf(newHour) + ":" + String.valueOf(newMinute);
            Date date = calendar.getTime();
            date.setHours(newHour);
            date.setMinutes(newMinute);


            if(!txtContent.getText().toString().equals("")){
                SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
                AppointmentDatabaseAdapter.insertEntry(Integer.parseInt(sp.getString("id",null)),txtContent.getText().toString(),hour,date.toLocaleString());
                Intent myIntent = new Intent(BookingActivity.this, AppointHistoryActivity.class);
                BookingActivity.this.startActivity(myIntent);
            }else {

            }
        }
    }
    //Go to Profile Activity
    public void profile(){
        Intent myIntent = new Intent(BookingActivity.this, ProfileActivity.class);
        BookingActivity.this.startActivity(myIntent);
    }
    //Go to UserMain Activity
    public void userMain(View view){
        SharedPreferences sp =getSharedPreferences("Login", MODE_PRIVATE);
        if(sp.getString("username",null).equals("admin")){
            Intent myIntent = new Intent(BookingActivity.this, ManageActivity.class);
            BookingActivity.this.startActivity(myIntent);
        }else if(sp.getString("username",null)!=null){
            Intent myIntent = new Intent(BookingActivity.this, UserMainActivity.class);
            BookingActivity.this.startActivity(myIntent);
        }else {
            Intent myIntent = new Intent(BookingActivity.this, LoginActivity.class);
            BookingActivity.this.startActivity(myIntent);
        }
    }
    //Handle Event Button Logout
    public void logout(){
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"Logout success", Toast.LENGTH_LONG ).show();
        sp.edit().remove("username").commit();
        sp.edit().remove("password").commit();
        Intent myIntent = new Intent(BookingActivity.this, MainActivity.class);
        BookingActivity.this.startActivity(myIntent);
    }
    //Open Calender to user can choose date
    public void openDatePicker(View view){
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override public void onPositiveButtonClick(Long selection) {
                // Do something...
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);
            }
        });
    }
    //Open Calender to user can choose hour
    public void openTimePicker(View view){
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build();
        picker.show(getSupportFragmentManager(), "fragment_tag");

        picker.addOnPositiveButtonClickListener(dialog -> {
            newHour = picker.getHour();
            newMinute = picker.getMinute();
        });
    }
    //OpenDialog to view input
    public void openDialog(String date, String hour, String content) throws JSONException, InterruptedException {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_booking);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width,height);

        TextView textViewDate =  dialog.findViewById(R.id.textviewDate);
        TextView textViewHour = dialog.findViewById(R.id.textviewHour);
        TextView textViewContent = dialog.findViewById(R.id.textviewContent);
        Button btn_update = dialog.findViewById(R.id.btn_update);

        Button btn_close = dialog.findViewById(R.id.btn_close);

        if(date!=null && hour!=null && content != null){
            textViewDate.setText(date);
            textViewHour.setText(hour);
            textViewContent.setText(content);
            //Handle Event Update
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(date!=null && hour!=null && content != null){
                        SharedPreferences sp =BookingActivity.this.getSharedPreferences("Login", MODE_PRIVATE);
                        AppointmentDatabaseAdapter.insertEntry(Integer.parseInt(sp.getString("id",null)),content,hour,date);
                        Intent myIntent = new Intent(BookingActivity.this, AppointHistoryActivity.class);
                        BookingActivity.this.startActivity(myIntent);
                    }
                }
            });
            //Handle Event Close
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                    startActivity(getIntent());
                }
            });
        }
        dialog.show();
    }
}