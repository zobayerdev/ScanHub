package com.trodev.scanhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.scanhub.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    RecyclerView Rv;
    DatabaseReference reference;
    UserListAdapter adapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Rv = findViewById(R.id.Rv);
        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        /*create methods*/
        load_data();

    }

    private void load_data() {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        /*all value dekhanor jonno register users*/
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    User modelUser = ds.getValue(User.class);

                    if (modelUser != null && !modelUser.getEmail().equals(fUser.getUid())) {
                        userList.add(modelUser);
                    }

                    // adapters
                    adapter = new UserListAdapter(UserListActivity.this, userList);
                    Rv.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}