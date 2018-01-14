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
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by Shlez on 11/24/17.
 */

public class CreateEmail extends AppCompatActivity {


    private static final String TAG = "CreateEmail";
    private Prayer prayer;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_email);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);


        prayer = (Prayer) getIntent().getSerializableExtra("prayer");

        final TextView txt_email = (TextView) findViewById(R.id.txt_create_email);
        final Button button = (Button) findViewById(R.id.btn_clear_txt_email);
        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt_email.getText().length() > 0) {
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
                txt_email.setText("");
                button.setVisibility(View.GONE);
            }
        });

        AppCompatButton create_email = (AppCompatButton) findViewById(R.id.btn_create_email);
        create_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = ((TextView) findViewById(R.id.txt_create_email)).getText().toString();

                boolean validEmail = Validations.isValidEmail(email);
                if (validEmail) {
                    prayer.setEmail(email);
                    updateUI(prayer);
                }
                else {
                    Toast.makeText(CreateEmail.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                    TextView txt_email = (TextView) findViewById(R.id.txt_create_email);
                    txt_email.requestFocus();
                }
            }
        });

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


    public void updateUI(Prayer prayer) {
        Intent intent = new Intent(this, CreatePassword.class);
        intent.putExtra("prayer", prayer);
        startActivity(intent);
    }


    //    Pass Intent to given class
    public void updateUI(Class<?> class_name) {
        Intent intent = new Intent(this, class_name);
        startActivity(intent);
    }
}

