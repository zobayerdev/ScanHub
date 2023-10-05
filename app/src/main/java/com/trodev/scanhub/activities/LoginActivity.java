package com.trodev.scanhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trodev.scanhub.MainActivity;
import com.trodev.scanhub.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText emailET, passET;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private MaterialButton log_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*hide action bar*/
        getSupportActionBar().hide();

        /*when user login this account then auto sign in*/
        isUserSigninauto();

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        log_Btn = findViewById(R.id.log_Btn);
        log_Btn.setOnClickListener(this);

        /*edit_text*/
        emailET = findViewById(R.id.emailEt);
        passET = findViewById(R.id.passEt);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    private void isUserSigninauto() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    // Register textView clicking
    @Override
    public void onClick(View v) {

        int itemId = v.getId();

        if (itemId == R.id.register) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            Toast.makeText(LoginActivity.this, "Register Here", Toast.LENGTH_SHORT).show();
            finish();
        } else if (itemId == R.id.log_Btn) {
            userLogin();
        }

    }

    private void userLogin() {

        String email = emailET.getText().toString().trim();
        String pass = passET.getText().toString().trim();

        if (email.isEmpty()) {
            emailET.setError("Email is required");
            emailET.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            passET.setError("Password must be 6 character");
            passET.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "login un-successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}