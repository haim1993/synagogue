package com.example.shlez.synagogue;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shlez on 11/24/17.
 */

public class CreatePassword extends AppCompatActivity {

    private static final String TAG = "CreatePassword";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private Prayer prayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_password);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        prayer = (Prayer) getIntent().getSerializableExtra("prayer");

/*        if 'email' recieved from intent extras by CreateEmail if not null,
          registration continues.
          */
        final String email = prayer.getEmail();
        if (email != null) {

            final TextView txt_password = (TextView) findViewById(R.id.txt_create_password);
            final Button button = (Button) findViewById(R.id.btn_clear_txt_password);
            txt_password.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txt_password.getText().length() > 0) {
                        button.setVisibility(View.VISIBLE);
                    }
                    else {
                        button.setVisibility(View.GONE);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    txt_password.setText("");
                    button.setVisibility(View.GONE);
                }
            });


            AppCompatButton create_password = (AppCompatButton) findViewById(R.id.btn_create_password);
            create_password.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String password = ((TextView) findViewById(R.id.txt_create_password)).getText().toString();

                    boolean validPassword = Validations.isValidPassword(password);
                    if (true) {
                        if (password.length() > 5) {
                            createAccount(email, password);
                        } else {
                            Toast.makeText(CreatePassword.this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
                            TextView txt_password = (TextView) findViewById(R.id.txt_create_password);
                            txt_password.requestFocus();
                        }
                    } else {
                        Toast.makeText(CreatePassword.this, "Password is not valid", Toast.LENGTH_SHORT).show();
                        TextView txt_password = (TextView) findViewById(R.id.txt_create_password);
                        txt_password.requestFocus();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        updateUI(MainActivity.class);

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel account creation?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreatePassword.this, prayer.getEmail() + " has been created successfully.",
                                    Toast.LENGTH_SHORT).show();
                            mUser = mAuth.getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("database").child("prayer").child(mUser.getUid()).setValue(prayer);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreatePassword.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(MainActivity.class);
                        }

                        // ...
                    }
                });

    }


    public void updateUI() {
        Intent intent = new Intent(this, NavigateDrawer.class);
        intent.putExtra("isGabay", false);
        startActivity(intent);
    }


    //    Pass Intent to given class
    public void updateUI(Class<?> class_name) {
        Intent intent = new Intent(this, class_name);
        startActivity(intent);
    }
}

