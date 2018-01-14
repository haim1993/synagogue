package com.example.shlez.synagogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shlez on 1/13/18.
 */

public class CreateUpdate extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private UpdatePost post = new UpdatePost();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_update);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_update);
        toolbar.setTitle("Create Update");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        mStorageRef = FirebaseStorage.getInstance().getReference();



        Button btn_post = (Button) findViewById(R.id.btn_update_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt_title = (TextView) findViewById(R.id.txt_update_create_title);
                TextView txt_message = (TextView) findViewById(R.id.txt_update_create_message);

                if (txt_title.getText().length() > 0) {
                    if (txt_message.getText().length() > 0) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        DatabaseReference weekly_updates = mDatabase.child("database").child("weekly_updates").child(currentDateTimeString);

                        post.setEmail(mUser.getEmail());
                        post.setTitle(txt_title.getText().toString());
                        post.setMessage(txt_message.getText().toString());
                        post.setDate((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));

                        weekly_updates.setValue(post);

                        Toast.makeText(CreateUpdate.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                        updateUI();
                    }
                    else {
                        Toast.makeText(CreateUpdate.this, "Message is not valid", Toast.LENGTH_SHORT).show();
                        txt_message.requestFocus();
                    }
                }
                else {
                    Toast.makeText(CreateUpdate.this, "Title is not valid", Toast.LENGTH_SHORT).show();
                    txt_title.requestFocus();
                }
            }
        });

    }


    public void updateUI() {
        Intent intent = new Intent(this, NavigateDrawer.class);
        intent.putExtra("isGabay", true);
        startActivity(intent);
    }
}
