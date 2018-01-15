package com.example.shlez.synagogue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateBirthday extends AppCompatActivity {

    private static final String TAG = "CreateBirthday";
    private Prayer prayer;

    private static int day = 01;
    private static int month = 00;
    private static int year = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_birthday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_birthday);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);

        prayer = (Prayer) getIntent().getSerializableExtra("prayer");

        final TextView lbl_birthday = (TextView) findViewById(R.id.lbl_create_birthday);
        lbl_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerFragment();
                dialogfragment.show(getSupportFragmentManager(), "Dialog");
            }

        });

        AppCompatButton btn = (AppCompatButton) findViewById(R.id.btn_create_birthday);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView lbl_birthday = (TextView) findViewById(R.id.lbl_create_birthday);
                if (lbl_birthday.getText() != "") {
                    prayer.setBirthday(lbl_birthday.getText().toString());
                    updateUI(prayer);
                }
                else {
                    Toast.makeText(CreateBirthday.this, "Tell us when you were born", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this, year, month, day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int current_year, int current_month, int current_day){
            day = current_day;
            month = current_month;
            year = current_year;
            TextView txtview = (TextView) getActivity().findViewById(R.id.lbl_create_birthday);
            txtview.setText(day + "/" + (month+1) + "/" + year);
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
                        //No button clicked
                        break;
                }
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel account creation?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    //    Click on Next button brings you to the next UI
    public void updateUI(Prayer prayer) {
        Intent intent = new Intent(this, CreateEmail.class);
        intent.putExtra("prayer", prayer);
        startActivity(intent);
    }


    public void updateUI(Class<?> class_name) {
        Intent intent = new Intent(this, class_name);
        startActivity(intent);
    }

}


