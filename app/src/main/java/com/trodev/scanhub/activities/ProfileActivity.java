package com.trodev.scanhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.scanhub.R;
import com.trodev.scanhub.models.User;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ProgressBar progressBar;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();


        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                Toast.makeText(ProfileActivity.this, "log-out successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        progressBar = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        //  final TextView greatingText = findViewById(R.id.greetingTextView);
        final TextView nameET = findViewById(R.id.nameEt);
        final TextView emailET = findViewById(R.id.emailEt);
        final TextView numberET = findViewById(R.id.numberEt);
        final TextView passEt = findViewById(R.id.passEt);

        progressBar.setVisibility(View.VISIBLE);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String uname = userProfile.uname;
                    String email = userProfile.email;
                    String num = userProfile.num;
                    String pass = userProfile.pass;

                    //  greatingText.setText(uname);
                    nameET.setText(uname);
                    emailET.setText(email);
                    numberET.setText(num);
                    passEt.setText(pass);

                    Toast.makeText(ProfileActivity.this, uname+" your data found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }
}