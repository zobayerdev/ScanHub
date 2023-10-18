package com.trodev.scanhub.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.scanhub.R;
import com.trodev.scanhub.models.LocationModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LocationActivity extends AppCompatActivity {
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button make_btn, download_btn, save_btn;
    private EditText addressET, fromET, toET;
    private ImageView imageView;
    String loc_from, loc_to, loc_sms, date, time;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // set title in activity
        getSupportActionBar().setTitle("Create Location QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*database location*/
        reference = FirebaseDatabase.getInstance().getReference("QR_DB").child("location_qr");


        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);
        addressET = findViewById(R.id.addressET);

        download_btn = findViewById(R.id.downloadBtn);
        download_btn.setVisibility(View.INVISIBLE);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        make_btn = findViewById(R.id.make_btn);

        make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromET.getText().toString().length() + toET.getText().toString().length() + addressET.getText().toString().length() == 0) {
                    Toast.makeText(LocationActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Your Location :  " + fromET.getText().toString().trim() + "\nDestination :  " + toET.getText().toString().trim() + "\nAddress:  " + addressET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download_btn.setVisibility(View.VISIBLE);
                        save_btn.setVisibility(View.VISIBLE);

                        download_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "SMS_Identity", null);
                                Toast.makeText(LocationActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*click button on save button and it save it firebase database*/
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_to_db();

            }
        });

    }

    private void save_to_db() {

        /*get text from edit text*/
        loc_from = fromET.getText().toString().trim();
        loc_to = toET.getText().toString().trim();
        loc_sms = addressET.getText().toString().trim();

        /*making condition*/
        if (loc_from.isEmpty()) {
            fromET.setError("Please fill your location");
        } else if (loc_to.isEmpty()) {
            toET.setError("please fill destination");
        } else if (loc_sms.isEmpty()) {
            addressET.setError("please fill sms");
        } else {

            // Its a time and date code show our user and admin
            // amra date and time init kore database e save korchi
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            String time = currentTime.format(calForTime.getTime());


            String Key = reference.push().getKey();

            if (Key != null) {
                LocationModel locationModel = new LocationModel(loc_from, loc_to, loc_sms, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(Key).setValue(locationModel);
                Toast.makeText(this, "save successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "un-successful", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}