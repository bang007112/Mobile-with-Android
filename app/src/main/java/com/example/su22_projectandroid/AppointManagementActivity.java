package com.example.su22_projectandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.AppointmentDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.AppointmentModel;
import com.example.su22_projectandroid.models.UserModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AppointManagementActivity extends AppCompatActivity {
    static ListView listView ;
    ArrayAdapter<AppointmentModel> appointments;
    static AppointmentDatabaseAdapter updateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_management);
        try {
            ArrayList<AppointmentModel> appointmentModels = AppointmentDatabaseAdapter.getRows();
            appointments = new AppointmentAdapter(this,appointmentModels);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create a ListView
        listView = (ListView) findViewById(R.id.listupdateviewID);
        listView.setAdapter(appointments);
        listView.setTextFilterEnabled(true);

        EditText edSearch = findViewById(R.id.editTextSearch);
        Button btn_search = findViewById(R.id.btn_search);
        //Handle Event Search
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle Search Function
                if(edSearch.getText().toString().equals("")==false){
                    ArrayList<AppointmentModel> arrayList= new ArrayList<>();
                    int size = 0;
                    try {
                        size = AppointmentDatabaseAdapter.getRows().size();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < size; i++) {
                        AppointmentModel appoint = null;
                        try {
                            appoint = AppointmentDatabaseAdapter.getRows().get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(appoint.userModel().getUsername().contains(edSearch.getText().toString().toLowerCase(Locale.ROOT))){
                            try {
                                arrayList.add((AppointmentModel) AppointmentDatabaseAdapter.getRows().get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //Create and set for Adapter
                    appointments = new AppointmentAdapter(AppointManagementActivity.this,arrayList);
                    listView = (ListView) findViewById(R.id.listupdateviewID);
                    listView.setAdapter(appointments);
                }
                else {
                    try {
                        ArrayList<AppointmentModel> appointmentModels = AppointmentDatabaseAdapter.getRows();
                        appointments = new AppointmentAdapter(AppointManagementActivity.this,appointmentModels);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView = (ListView) findViewById(R.id.listupdateviewID);
                    listView.setAdapter(appointments);
                    listView.setTextFilterEnabled(true);
                }
            }
        });

    }
    //Go to Manage Activity
    public void manage_admin(View view){
        Intent intent = new Intent(AppointManagementActivity.this, ManageActivity.class);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
//Create a Adapter to Set data fot items ListView
class AppointmentAdapter extends ArrayAdapter<AppointmentModel> {
    ArrayList<AppointmentModel> appointments;
    public AppointmentAdapter(Context context, ArrayList<AppointmentModel> appointments) {
        super(context, 0, appointments);
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AppointmentModel appointment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_appointment, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvHour = (TextView) convertView.findViewById(R.id.tvHour);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddrress);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        Button deleteBtn = (Button) convertView.findViewById(R.id.confirmAppoint);

        // Populate the data into the template view using the data object
        UserModel userModel = UsersDatabaseAdapter.getUserByID(appointment.getUserID());

        tvName.setText(userModel.getUsername());
        tvPhone.setText(userModel.getUserphone());
        tvAddress.setText(userModel.getAddress());
        tvContent.setText(appointment.getAppointmentContent());
        tvDate.setText(appointment.getDate());
        tvHour.setText(appointment.getHour());
        //Handle Event Button Confirm
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppointmentDatabaseAdapter.deleteEntry(appointment.getID());
                appointments.remove(position);
                notifyDataSetChanged();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}




