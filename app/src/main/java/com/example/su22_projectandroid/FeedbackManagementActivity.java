package com.example.su22_projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.su22_projectandroid.connectDb.AppointmentDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.ContactDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.FeedbackDatabaseAdapter;
import com.example.su22_projectandroid.connectDb.UsersDatabaseAdapter;
import com.example.su22_projectandroid.models.AppointmentModel;
import com.example.su22_projectandroid.models.Contact;
import com.example.su22_projectandroid.models.Feedback;
import com.example.su22_projectandroid.models.UserModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

public class FeedbackManagementActivity extends AppCompatActivity {

    static ListView listView ;
    ArrayAdapter<Feedback> feedbackArrayAdapters;
    static FeedbackAdapter updateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_management);
        try {
            ArrayList<Feedback> feedbacks = FeedbackDatabaseAdapter.getRows();
            feedbackArrayAdapters = new FeedbackAdapter(this,feedbacks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create a ListView
        listView = (ListView) findViewById(R.id.listviewFeedbackID);
        listView.setAdapter(feedbackArrayAdapters);


        EditText edSearch = findViewById(R.id.editTextSearch);
        Button btn_search = findViewById(R.id.btn_search);
        //Handle Event Button Search
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function Search By Username
                if(edSearch.getText().toString().equals("")==false){
                    ArrayList<Feedback> arrayList= new ArrayList<>();
                    int size = 0;
                    try {
                        size = FeedbackDatabaseAdapter.getRows().size();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < size; i++) {
                        Feedback feedback = null;
                        try {
                            feedback = FeedbackDatabaseAdapter.getRows().get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(feedback.getAppointmentModel().userModel().getUsername().contains(edSearch.getText().toString().toLowerCase(Locale.ROOT))){
                            try {
                                arrayList.add((Feedback) FeedbackDatabaseAdapter.getRows().get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    feedbackArrayAdapters = new FeedbackAdapter(FeedbackManagementActivity.this,arrayList);
                    listView = (ListView) findViewById(R.id.listviewFeedbackID);
                    listView.setAdapter(feedbackArrayAdapters);
                }else {
                    try {
                        ArrayList<Feedback> feedbacks_list = FeedbackDatabaseAdapter.getRows();
                        feedbackArrayAdapters = new FeedbackAdapter(FeedbackManagementActivity.this,feedbacks_list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView = (ListView) findViewById(R.id.listviewFeedbackID);
                    listView.setAdapter(feedbackArrayAdapters);
                    listView.setTextFilterEnabled(true);
                }

            }
        });
    }
    //Go to Manage Activity
    public void manage_admin(View view){
        Intent intent = new Intent(FeedbackManagementActivity.this, ManageActivity.class);
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
//Create Adapter to set data for items send ListView
class FeedbackAdapter extends ArrayAdapter<Feedback> {
    ArrayList<Feedback> feedbacks;
    public FeedbackAdapter(Context context, ArrayList<Feedback> feedbacks) {
        super(context, 0, feedbacks);
        this.feedbacks = feedbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Feedback feedback = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_feedback, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvAppointContent = (TextView) convertView.findViewById(R.id.tvAppointContent);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvFeedbackContent = (TextView) convertView.findViewById(R.id.tvFeedbackContent);
        Button deleteBtn = (Button) convertView.findViewById(R.id.confirmFeedback);

        if(feedback.getAppointmentID()!=null && AppointmentDatabaseAdapter.getAppointByID(feedback.getAppointmentID())!=null){
            AppointmentModel appointmentModel = AppointmentDatabaseAdapter.getAppointByID(feedback.getAppointmentID());

            // Populate the data into the template view using the data object
            UserModel userModel = UsersDatabaseAdapter.getUserByID((appointmentModel.getUserID()));
            tvName.setText(userModel.getUsername());
            tvAppointContent.setText(appointmentModel.getAppointmentContent());
        }
        tvDate.setText(feedback.getDate());
        tvFeedbackContent.setText(feedback.getFeedbackContent());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FeedbackDatabaseAdapter.deleteEntry(feedback.getID());
                feedbacks.remove(position);
                notifyDataSetChanged();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}