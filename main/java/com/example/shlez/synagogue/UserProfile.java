package com.example.shlez.synagogue;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shlez on 11/21/17.
 */

public class UserProfile extends Fragment {


    private static final String TAG = "UserProfile";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private StorageReference profileImageRef;
    final Context context = getActivity();

    int PLACE_PICKER_REQUEST = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (container != null) container.removeAllViews();

        View v = inflater.inflate(R.layout.user_profile, container, false);

        ImageView civ = (ImageView) v.findViewById(R.id.img_profile_image);
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(getActivity(), "Select your image:");
            }
        });

        TextView phone_num = (TextView) v.findViewById(R.id.txt_profile_phone);
        phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetPhoneNumberDialog(v);
            }
        });


        TextView address = (TextView) v.findViewById(R.id.txt_profile_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetLocationPicker(v);
            }
        });


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        profileImageRef = mStorageRef.child("profile_images/" + mUser.getUid().substring(10) + ".jpg");

        updateProfileFields();
    }


    private void updateProfileFields() {

        String user_id = mUser.getUid();

        //        Set user_image profile image in circle imageview
        ImageView profile_img = (ImageView) getActivity().findViewById(R.id.img_profile_image);
        DatabaseReference imageURL = mDatabase.child("database").child("prayer").child(user_id).child("imageURL");
        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String full_image_path = uri.getPath();
                int index = full_image_path.indexOf("/profile_images/");
                String short_image_path = full_image_path.substring(index);

                imageURL.setValue(short_image_path);
                Picasso.with(getActivity()).load(uri).into(profile_img);
                profile_img.setEnabled(false);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Can't load existing image.");
            }
        });


        //        Set user_profile name in textview
        final TextView txt_name = (TextView) getView().findViewById(R.id.txt_profile_name);
        DatabaseReference fname_value = mDatabase.child("database").child("prayer").child(user_id).child("name");
        fname_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_name.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadName:onCancelled", databaseError.toException());
            }
        });


        //        Set user_profile phone in textview
        final TextView txt_phone = (TextView) getView().findViewById(R.id.txt_profile_phone);
        DatabaseReference phone_value = mDatabase.child("database").child("prayer").child(user_id).child("phone");
        phone_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phone = dataSnapshot.getValue(String.class);
                if (phone != null && phone.length() > 0) {
                    txt_phone.setText(phone);
                    txt_phone.setTextColor(Color.BLACK);
                    txt_phone.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadName:onCancelled", databaseError.toException());
            }
        });


        //        Set user_profile email in textview
        final TextView txt_email = (TextView) getView().findViewById(R.id.txt_profile_email);
        DatabaseReference email_value = mDatabase.child("database").child("prayer").child(user_id).child("email");
        email_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_email.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadName:onCancelled", databaseError.toException());
            }
        });


        //        Set user_profile birthday in textview
        final TextView txt_birthday = (TextView) getView().findViewById(R.id.txt_profile_birthday);
        DatabaseReference birthday_value = mDatabase.child("database").child("prayer").child(user_id).child("birthday");
        birthday_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_birthday.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadName:onCancelled", databaseError.toException());
            }
        });


        //        Set user_address Address in textview
        final TextView txt_address = (TextView) getView().findViewById(R.id.txt_profile_address);
        DatabaseReference address_value = mDatabase.child("database").child("prayer").child(user_id).child("address");
        address_value.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String address = dataSnapshot.getValue(String.class);
                if (address.length() > 0) {
                    txt_address.setTextColor(Color.BLACK);
                    txt_address.setText(address);
                    txt_address.setEnabled(false);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadName:onCancelled", databaseError.toException());
            }
        });
    }


    //    Upon user click on +Add on phone column show "Set phone number" dialog.
    public void showSetPhoneNumberDialog(View v) {
        // custom dialog
        Log.w(TAG, "in phone dialog");
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_phone_number_dialog);
        dialog.setTitle("Enter Your Phone Number");

        final TextView txt_phone = (TextView) dialog.findViewById(R.id.txt_create_phone_number);
        final Button clear = (Button) dialog.findViewById(R.id.btn_clear_txt_phone_number);
        txt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt_phone.getText().length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt_phone.setText("");
                clear.setVisibility(View.GONE);
            }
        });


        Button btn_save = (Button) dialog.findViewById(R.id.btn_save_phone_number);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = ((TextView) dialog.findViewById(R.id.txt_create_phone_number)).getText().toString();
                if (phone.length() == 10) {
                    mDatabase.child("database").child("prayer").child(mUser.getUid()).child("phone").setValue(phone);
                    txt_phone.setText(phone);
                    txt_phone.setTextColor(Color.BLACK);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Phone Number is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }


    public void showSetLocationPicker(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            getActivity().startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    //    Go to MainActivity upon signing out
    public void launchMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


}
