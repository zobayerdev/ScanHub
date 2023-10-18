package com.trodev.scanhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import com.trodev.scanhub.activities.EmailActivity;
import com.trodev.scanhub.activities.LocationActivity;
import com.trodev.scanhub.activities.ProductQrActivity;
import com.trodev.scanhub.activities.ScanGalleryActivity;
import com.trodev.scanhub.activities.ScannerActivity;
import com.trodev.scanhub.activities.SmsActivity;
import com.trodev.scanhub.activities.URLActivity;
import com.trodev.scanhub.activities.WifiQrActivity;


public class HomeFragment extends Fragment {

    MaterialCardView product_qr, message, wifi;
    MaterialCardView email_qr, location_qr, url_qr;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*init views*/
        product_qr = view.findViewById(R.id.product_qr);
        message = view.findViewById(R.id.message);
        wifi = view.findViewById(R.id.wifi);
        email_qr = view.findViewById(R.id.email_qr);
        location_qr = view.findViewById(R.id.location_qr);
        url_qr = view.findViewById(R.id.url_qr);


        /*set on click listener*/
        product_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_product();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_message();
            }
        });

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_wifi();
            }
        });

        email_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_email();
            }
        });

        location_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_location();
            }
        });
        url_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_url();
            }
        });


        return view;
    }

    private void goto_url() {
        startActivity(new Intent(getContext(), URLActivity.class));
    }

    private void goto_location() {
        startActivity(new Intent(getContext(), LocationActivity.class));
    }

    private void goto_email() {

        startActivity(new Intent(getContext(), EmailActivity.class));

    }

    private void goto_wifi() {
        startActivity(new Intent(getContext(), WifiQrActivity.class));
    }

    private void goto_message() {
        startActivity(new Intent(getContext(), SmsActivity.class));
    }

    private void goto_product() {

        startActivity(new Intent(getContext(), ProductQrActivity.class));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.images_item_scan) {
            Intent intent = new Intent(getContext(), ScannerActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.gallery_item_scan) {
            Intent intent = new Intent(getContext(), ScanGalleryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}