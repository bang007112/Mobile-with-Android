package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.AppointmentDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.FeedbackDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.AppointmentModel;
import com.example.su22_projectandroid.models.UserModel;

import org.json.JSONException;

import java.util.ArrayList;

public class AppointHistoryActivity extends AppCompatActivity {

    static ListView listView;
    ArrayAdapter<AppointmentModel> appointments;
    static AppointmentDatabaseAdapter updateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_history);
        SharedPreferences sp =this.getSharedPreferences("Login", MODE_PRIVATE);
        //Create ListView for AppointHistory
        ArrayList<AppointmentModel> appointmentModels = AppointmentDatabaseAdapter.getAppointByUserID(sp.getString("id",null));
        appointments = new AppointHistoryAdapter(this, appointmentModels);
        listView = (ListView) findViewById(R.id.listapointhistoryviewID);
        listView.setAdapter(appointments);
        //Create a option to search
        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"...", "Sort by date"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        //Handle event Search Method
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("Sort by date"));{
                    ArrayList<AppointmentModel> arrayList= new ArrayList<>();
                    try {
                        arrayList = AppointmentDatabaseAdapter.getRowsOfUserByDateDES(sp.getString("id",null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(arrayList.size()>0){
                        appointments = new AppointHistoryAdapter(AppointHistoryActivity.this, arrayList);
                        listView = (ListView) findViewById(R.id.listapointhistoryviewID);
                        listView.setAdapter(appointments);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Check Login and Role to switch Activity
    public void userMain(View view){
        SharedPreferences sp =getSharedPreferences("Login", MODE_PRIVATE);
        if(sp.getString("username",null).equals("admin")){
            Intent myIntent = new Intent(AppointHistoryActivity.this, ManageActivity.class);
            AppointHistoryActivity.this.startActivity(myIntent);
        }else if(sp.getString("username",null)!=null){
            Intent myIntent = new Intent(AppointHistoryActivity.this, UserMainActivity.class);
            AppointHistoryActivity.this.startActivity(myIntent);
        }else {
            Intent myIntent = new Intent(AppointHistoryActivity.this, LoginActivity.class);
            AppointHistoryActivity.this.startActivity(myIntent);
        }
    }
}
//Create Adapter to set data to items in ListView
class AppointHistoryAdapter extends ArrayAdapter<AppointmentModel> {
    ArrayList<AppointmentModel> appointments;
    private Context context;
    public AppointHistoryAdapter(Context context, ArrayList<AppointmentModel> appointments) {
        super(context, 0, appointments);
        this.appointments = appointments;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AppointmentModel appointment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_appoint_history, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvHour = (TextView) convertView.findViewById(R.id.tvHour);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddrress);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);


        // Populate the data into the template view using the data object
        UserModel userModel = UsersDatabaseAdapter.getUserByID(appointment.getUserID());

        tvName.setText(userModel.getUsername());
        tvPhone.setText(userModel.getUserphone());
        tvAddress.setText(userModel.getAddress());
        tvDate.setText(appointment.getDate());
        tvHour.setText(appointment.getHour());
        tvContent.setText(appointment.getAppointmentContent());

        Button feedbackBtn = (Button) convertView.findViewById(R.id.btn_feedback);
        //Handle Event to FeedBack Appointment
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(context, FeedbackActivity.class);
                myIntent.putExtra("appointId",appointment.getID());
                context.startActivity(myIntent);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}