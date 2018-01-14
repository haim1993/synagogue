package com.example.shlez.synagogue;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by maor shabtay on 28-Dec-17.
 */

public class ShowEvent extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView listEvent;
    private ArrayList<Prayer> arrayList = new ArrayList<>();
    private ArrayAdapter<Prayer> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Prayer");
        listEvent = (ListView) findViewById(R.id.event_list_view);
        adapter = new ArrayAdapter<Prayer>(this, android.R.layout.simple_list_item_1, arrayList);

        listEvent.setAdapter(adapter);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Prayer prayer = dataSnapshot.getValue(Prayer.class);
                arrayList.add(prayer);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Prayer prayer = dataSnapshot.getValue(Prayer.class);
                arrayList.remove(prayer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
