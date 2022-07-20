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

public class ContactManagementActivity extends AppCompatActivity {
    static ListView listView ;
    ArrayAdapter<Contact> contacts;
    static ContactDatabaseAdapter updateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_management);
        try {
            ArrayList<Contact> contacts_list = ContactDatabaseAdapter.getRows();
            contacts = new ContactAdapter(this,contacts_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create List View
        listView = (ListView) findViewById(R.id.listviewContactID);
        listView.setAdapter(contacts);


        EditText edSearch = findViewById(R.id.editTextSearch);
        Button btn_search = findViewById(R.id.btn_search);
        //Handle Event Search
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function Search by fullname and then set data for ListView
                if(edSearch.getText().toString().equals("")==false){
                    ArrayList<Contact> arrayList= new ArrayList<>();
                    int size = 0;
                    try {
                        size = ContactDatabaseAdapter.getRows().size();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < size; i++) {
                        Contact contact = null;
                        try {
                            contact = ContactDatabaseAdapter.getRows().get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(contact.getFullname().contains(edSearch.getText().toString().toLowerCase(Locale.ROOT)) || contact.getEmail().contains(edSearch.getText().toString().toLowerCase(Locale.ROOT))){
                            try {
                                arrayList.add((Contact) ContactDatabaseAdapter.getRows().get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    contacts = new ContactAdapter(ContactManagementActivity.this,arrayList);
                    listView = (ListView) findViewById(R.id.listviewContactID);
                    listView.setAdapter(contacts);
                }else {
                    try {
                        ArrayList<Contact> contacts_list = ContactDatabaseAdapter.getRows();
                        contacts = new ContactAdapter(ContactManagementActivity.this,contacts_list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView = (ListView) findViewById(R.id.listviewContactID);
                    listView.setAdapter(contacts);
                    listView.setTextFilterEnabled(true);
                }

            }
        });
    }
    //Go to Manage Activity
    public void manage_admin(View view){
        Intent intent = new Intent(ContactManagementActivity.this, ManageActivity.class);
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
class ContactAdapter extends ArrayAdapter<Contact> {
    ArrayList<Contact> contacts;
    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_contact, parent, false);
        }
        // Lookup view for data population
        TextView tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        // Populate the data into the template view using the data object

        tvFullName.setText(contact.getFullname());
        tvPhone.setText(contact.getPhone());
        tvEmail.setText(contact.getEmail());
        tvContent.setText(contact.getContent());

        Button btn_confirm = convertView.findViewById(R.id.confirmContact);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContactDatabaseAdapter.deleteEntry(contact.getID());
                contacts.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}