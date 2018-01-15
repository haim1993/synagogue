package com.example.shlez.synagogue;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Shlez on 11/20/17.
 */


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final TextView txt_email = (TextView) findViewById(R.id.txt_input_email);
        final Button btn_clear_email = (Button) findViewById(R.id.btn_clear_input_email);
        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    btn_clear_email.setVisibility(View.GONE);
                } else {
                    if (txt_email.getText().length() > 0) {
                        btn_clear_email.setVisibility(View.VISIBLE);
                    }
                    txt_email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (txt_email.getText().length() > 0) {
                                btn_clear_email.setVisibility(View.VISIBLE);
                            }
                            else {
                                btn_clear_email.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) { }

                        @Override
                        public void afterTextChanged(Editable s) { }
                    });
                }
            }
        });
        btn_clear_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt_email.setText("");
                btn_clear_email.setVisibility(View.GONE);
            }
        });

        final TextView txt_password = (TextView) findViewById(R.id.txt_input_password);
        final Button btn_clear_password = (Button) findViewById(R.id.btn_clear_input_password);
        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    btn_clear_password.setVisibility(View.GONE);
                } else {
                    if (txt_password.getText().length() > 0) {
                        btn_clear_password.setVisibility(View.VISIBLE);
                    }
                    txt_password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (txt_password.getText().length() > 0) {
                                btn_clear_password.setVisibility(View.VISIBLE);
                            }
                            else {
                                btn_clear_password.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) { }

                        @Override
                        public void afterTextChanged(Editable s) { }
                    });

                }
            }
        });
        btn_clear_password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt_password.setText("");
                btn_clear_password.setVisibility(View.GONE);
            }
        });


//        Click on Login button
        AppCompatButton login = (AppCompatButton) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = ((TextView) findViewById(R.id.txt_input_email)).getText().toString();
                String password = ((TextView) findViewById(R.id.txt_input_password)).getText().toString();
                signIn(email, password);

            }
        });


//        No account yet? Create one. OnClick, directs user to 'create_account'
        TextView signup = (TextView) findViewById(R.id.txt_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchCreateAccountPage();

            }
        });


    }


    //    Launches the CreateAccount page

    public void launchCreateAccountPage() {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }


    //    Checks if user is logged in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }


    //    user Sign in
    public void signIn(String email, String password) {

        boolean validEmail = Validations.isValidEmail(email);

        if (validEmail && password.length() > 5) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                String email = mAuth.getCurrentUser().getEmail();
                                Toast.makeText(MainActivity.this, email + " signed in.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                        }
                    });
        } else if (!validEmail) {
            Toast.makeText(MainActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
            TextView txt_email = (TextView) findViewById(R.id.txt_input_email);
            txt_email.requestFocus();
        } else if (password.length() <= 5) {
            Toast.makeText(MainActivity.this, "Password must contain more than 6 characters", Toast.LENGTH_SHORT).show();
            TextView txt_password = (TextView) findViewById(R.id.txt_input_password);
            txt_password.requestFocus();
        }

    }


    //    Update User Interface to UserProfile upon sign-in / signed-in
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference gabay_reference = mDatabase.child("database").child("prayer").child(user_id).child("isGabay");
            gabay_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isGabay = false;

                    if (dataSnapshot.getValue(Boolean.class) != null) {
                        isGabay = dataSnapshot.getValue(Boolean.class);
                    }
                        Intent intent;
                        if (isGabay) {
                            intent = new Intent(MainActivity.this, NavigateDrawer.class);
                            intent.putExtra("isGabay", true);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, NavigateDrawer.class);
                            intent.putExtra("isGabay", false);
                            startActivity(intent);
                        }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadName:onCancelled", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }
}


