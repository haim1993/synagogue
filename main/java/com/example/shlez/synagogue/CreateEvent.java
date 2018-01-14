package com.example.shlez.synagogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by maor shabtay on 28-Dec-17.
 */

public class CreateEvent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private EditText et_des, et_contact, et_date, et_time;
    private String des, contact, date, time;
    private Event event;
    Button btn_enter;


    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        et_des = (EditText) findViewById(R.id.txt_create_description_event);
        et_contact = (EditText) findViewById(R.id.txt_create_contact);
        et_date = (EditText) findViewById(R.id.txt_create_date);
        et_time = (EditText) findViewById(R.id.txt_create_time);
        btn_enter = (Button) findViewById(R.id.btn_create_event);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event = (Event) getIntent().getSerializableExtra("Event");
                create(view);

            }

        });
    }
    public void create(View view){
        intialize();
        if(!validate()){
            Toast.makeText(this, "Create Event has Failed" ,Toast.LENGTH_SHORT).show();
        }
        else {
            onEventSuccess(view);
        }
    }
    public void onEventSuccess(View view){
            try {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("database").child("Event").setValue(event);

            updateUI(view);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
    public boolean validate(){
        boolean valid = true;
        if (event.getDescription_event().isEmpty()) {
            et_des.setError("Please enter description event");
            valid = false;
        }
        if (event.contact.isEmpty()) {
            et_contact.setError("Please enter Contact");
            valid = false;
        }
        if (event.date.isEmpty()) {
            et_date.setError("Please enter date event");
            valid = false;
        }
        if (event.time.isEmpty()) {
            et_time.setError("Please enter time event");
            valid = false;
        }

        return valid;
    }
    public void intialize(){
        des=et_des.getText().toString().trim();
        contact=et_contact.getText().toString().trim();
        date=et_date.getText().toString().trim();
        time=et_time.getText().toString().trim();
        event = new Event(des,contact,date,time);
    }
    public void updateUI(View v) {
        Intent intent = new Intent(CreateEvent.this, AboutUs.class);
        startActivity(intent);
    }
}
