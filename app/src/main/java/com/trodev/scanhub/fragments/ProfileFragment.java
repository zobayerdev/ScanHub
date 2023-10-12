package com.trodev.scanhub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.scanhub.R;
import com.trodev.scanhub.activities.LoginActivity;
import com.trodev.scanhub.activities.ProfileActivity;
import com.trodev.scanhub.models.User;

public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    ImageView logout;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView nameET =view. findViewById(R.id.nameEt);
        final TextView emailET = view.findViewById(R.id.emailTv);
        final TextView numberET =view. findViewById(R.id.mobileTv);
        final TextView passEt =view. findViewById(R.id.passTv);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String uname = userProfile.uname;
                    String email = userProfile.email;
                    String num = userProfile.num;
                    String pass = userProfile.pass;

                    nameET.setText(uname);
                    emailET.setText("E-mail: "+email);
                    numberET.setText("Mobile: "+num);
                    passEt.setText("Password: "+pass);

                    /*toast sms*/
                    Toast.makeText(getActivity(), uname+" your data found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(), "log-out successful", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
            }
        });

        return view;
    }
}