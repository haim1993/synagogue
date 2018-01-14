package com.example.shlez.synagogue;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.Map;
import java.util.Vector;

/**
 * Created by Shlez on 1/13/18.
 */

public class WeeklyUpdates extends Fragment {

    private static final String TAG = "WeeklyUpdates";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;

    ListView listView;

    Vector<String> emailArray = new Vector<>();
    Vector<String> titleArray = new Vector<>();
    Vector<String> messageArray = new Vector<>();
    Vector<String> dateArray = new Vector<>();

    private boolean isGabay = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (container != null) container.removeAllViews();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        View v = inflater.inflate(R.layout.weekly_updates, container, false);
        getMessagesFromDatabase(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Weekly Updates");
    }



    public void getMessagesFromDatabase(View v) {
        DatabaseReference df_gabay = mDatabase.child("database").child("weekly_updates");
        df_gabay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UpdatePost post_update = snapshot.getValue(UpdatePost.class);

                    emailArray.add(0, post_update.getEmail());
                    Log.w(TAG, post_update.getEmail());
                    titleArray.add(0, post_update.getTitle());
                    Log.w(TAG, post_update.getTitle());
                    messageArray.add(0, post_update.getMessage());
                    Log.w(TAG, post_update.getMessage());
                    dateArray.add(0, post_update.getDate());
                    Log.w(TAG, post_update.getDate());
                }

                WeeklyUpdatesListAdapter whatever = new WeeklyUpdatesListAdapter(getActivity(), emailArray, titleArray, messageArray, dateArray);
                listView = (ListView) v.findViewById(R.id.list_weekly_updates);
                listView.setAdapter(whatever);
                if (listView.getAdapter().getCount() != 0) {
                    ((TextView) getView().findViewById(R.id.title_weekly_updates)).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
    });
    }

}


class WeeklyUpdatesListAdapter extends ArrayAdapter{


    private final Activity context;
    private final Vector<String> emailArray;
    private final Vector<String> titleArray;
    private final Vector<String> messageArray;
    private final Vector<String> dateArray;

    private FirebaseAuth mAuth;

    public WeeklyUpdatesListAdapter(Activity context, Vector<String> emailArray, Vector<String> titleArray, Vector<String> messageArray, Vector<String> dateArray){

        super(context,R.layout.listview_updates , titleArray);

        mAuth = FirebaseAuth.getInstance();

        this.context = context;
        this.emailArray = emailArray;
        this.titleArray = titleArray;
        this.messageArray = messageArray;
        this.dateArray = dateArray;


    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_updates, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView txt_title = (TextView) rowView.findViewById(R.id.update_title);
        TextView txt_message = (TextView) rowView.findViewById(R.id.update_message);
        TextView txt_data = (TextView) rowView.findViewById(R.id.update_date);
        TextView txt_email_sender = (TextView) rowView.findViewById(R.id.update_email_sender);

        //this code sets the values of the objects to values from the arrays
        txt_title.setText(titleArray.elementAt(position));
        txt_message.setText(messageArray.elementAt(position));
        txt_data.setText(dateArray.elementAt(position));
        txt_email_sender.setText(emailArray.elementAt(position));

        return rowView;

    };
}
