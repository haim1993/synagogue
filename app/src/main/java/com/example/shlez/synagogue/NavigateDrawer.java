package com.example.shlez.synagogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

public class NavigateDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "NavigateDrawer";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;

//    private boolean isGabay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigate_drawer);

//        Refresh page to current fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_weekly_updates, new WeeklyUpdates()).commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


//        Add to nav, current user full name.
        DatabaseReference name_value = mDatabase.child("database").child("prayer").child(mUser.getUid()).child("name");
        name_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.nav_profile_name)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


//        Add to nav, current user email address.
//        String email_value = mUser.getEmail();
//        ((TextView) findViewById(R.id.nav_profile_email)).setText(email_value);
        DatabaseReference email_value = mDatabase.child("database").child("prayer").child(mUser.getUid()).child("email");
        email_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.nav_profile_email)).setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_add_update, menu);

        MenuItem mItem = menu.findItem(R.id.action_add_update);

        boolean isGabay = getIntent().getBooleanExtra("isGabay", false);
        if (!isGabay) { mItem.setVisible(false); }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_update:
                Intent intent = new Intent(this, CreateUpdate.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    public int getCurrentFragmentId() {
//        Fragment current_frag = getSupportFragmentManager().findFragmentById(R.id.nav_updates);
//        if (current_frag != null && current_frag.isVisible()) return current_frag.getId();
//        current_frag = getSupportFragmentManager().findFragmentById(R.id.nav_logout);
//
//        if (current_frag != null && current_frag.isVisible()) return current_frag.getId();
//
//        current_frag = getSupportFragmentManager().findFragmentById(R.id.nav_about);
//        if (current_frag != null && current_frag.isVisible()) return current_frag.getId();
//
//        current_frag = getSupportFragmentManager().findFragmentById(R.id.nav_tasks);
//        if (current_frag != null && current_frag.isVisible()) return current_frag.getId();
//
//        current_frag = getSupportFragmentManager().findFragmentById(R.id.nav_profile);
//        if (current_frag != null && current_frag.isVisible()) return current_frag.getId();
//
//        return -1; // couldn't find visible fragment.
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

//        int current_fragment_id = getCurrentFragmentId();
        switch (id) {
            case R.id.nav_profile:
                fragment = new UserProfile();
                break;
            case R.id.nav_updates:
                fragment = new WeeklyUpdates();
                break;
            case R.id.nav_tasks:
                fragment = new Tasks();
                break;
            case R.id.nav_about:
                fragment = new AboutUs();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_weekly_updates, fragment);
            ft.commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
