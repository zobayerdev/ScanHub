package com.trodev.scanhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.trodev.scanhub.activities.ProfileActivity;
import com.trodev.scanhub.fragments.HistoryFragment;
import com.trodev.scanhub.fragments.HomeFragment;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    /*init widget*/
    private DrawerLayout drawerLayout;
    SmoothBottomBar smoothBottomBar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*init all drawer layout*/
        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.navigation_view);

        /*init views*/
        smoothBottomBar = findViewById(R.id.bottombar);


        // #######################
        // Drawer Layout implement
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // #################################################################
        // navigation view work process
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        /*When apps start show HomeFragments*/
        setTitle("Home");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new HomeFragment());
        fragmentTransaction.commit();


/*        *//*set all status bar, navigation bar, toolbar color*//*
        smoothBottomBar.setBarBackgroundColor(Color.parseColor("#6DB3D1"));
        getWindow().setNavigationBarColor(Color.parseColor("#6DB3D1"));
        getWindow().setStatusBarColor(Color.parseColor("#6DB3D1"));*/

        /*smooth bar working process*/
        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                if (i == 0) {
                    setTitle("Home");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, new HomeFragment());
                    fragmentTransaction.commit();

/*                    *//*set all status bar, navigation bar, toolbar color*//*
                    smoothBottomBar.setBarBackgroundColor(Color.parseColor("#6DB3D1"));
                    getWindow().setNavigationBarColor(Color.parseColor("#6DB3D1"));
                    getWindow().setStatusBarColor(Color.parseColor("#6DB3D1"));*/

                }
                if (i == 1) {
                    setTitle("History");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, new HistoryFragment());
                    fragmentTransaction.commit();


/*                    smoothBottomBar.setBarBackgroundColor(Color.parseColor("#6DB3D1"));
                    getWindow().setNavigationBarColor(Color.parseColor("#6DB3D1"));
                    getWindow().setStatusBarColor(Color.parseColor("#6DB3D1"));*/
                }

                return false;
            }
        });


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int itemId = item.getItemId();

        if (itemId == R.id.menu_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            Toast.makeText(this, "user profile", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_privacy) {
            Toast.makeText(this, "privacy policy", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_share) {
            Toast.makeText(this, "share apps", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_rate) {
            Toast.makeText(this, "rate us", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_contact) {
            Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_apps) {
            Toast.makeText(this, "our apps", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}