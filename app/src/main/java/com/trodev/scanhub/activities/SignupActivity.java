package com.trodev.scanhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.trodev.scanhub.MainActivity;
import com.trodev.scanhub.R;
import com.trodev.scanhub.models.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton login;
    private EditText nameET, mobileET, emailET, passET;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        /*hide action bar*/
        getSupportActionBar().hide();

        /*auth profile from firebase*/
        mAuth = FirebaseAuth.getInstance();

        /*init widget views*/
        login = findViewById(R.id.log_Btn);
        login.setOnClickListener(this);
        nameET = findViewById(R.id.nameEt);
        mobileET = findViewById(R.id.numberEt);
        emailET = findViewById(R.id.emailEt);
        passET = findViewById(R.id.passEt);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {

        int itemId = view.getId();

        if (itemId == R.id.log_Btn) {
            registarUser();
        }

    }

    private void registarUser() {

        String name, number, email, pass;
        name = nameET.getText().toString().trim();
        number = mobileET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        pass = passET.getText().toString().trim();

        if (name.isEmpty()) {
            nameET.setError("Name is required");
            nameET.requestFocus();
            return;
        }
        if (number.isEmpty()) {
            mobileET.setError("Mobile number is required");
            mobileET.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailET.setError("E-mail is required");
            emailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please provide valid email!");
            emailET.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            passET.setError("Password is required");
            passET.requestFocus();
            return;
        }

        if (pass.length() <= 6) {
            passET.setError("Min password length should be 6 character");
            passET.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, number, email, pass);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                finish();

                                            } else {

                                                progressBar.setVisibility(View.VISIBLE);
                                                Toast.makeText(SignupActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });


    }
}