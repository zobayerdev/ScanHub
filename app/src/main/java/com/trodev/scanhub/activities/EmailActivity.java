package com.trodev.scanhub.activities;

import static android.app.ProgressDialog.show;

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
import com.trodev.scanhub.models.EmailModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmailActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button make_btn, save_btn, downloadBtn;
    private EditText smsET, fromET, toET;
    private ImageView imageView;
    String email_from, email_to, email_sms, date, time;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        // set title in activity
        getSupportActionBar().setTitle("Create E-mail QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*database location*/
        reference = FirebaseDatabase.getInstance().getReference("QR_DB").child("email_qr");


        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);
        smsET = findViewById(R.id.smsET);

        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        make_btn = findViewById(R.id.make_btn);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setVisibility(View.INVISIBLE);

        make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromET.getText().toString().length() + toET.getText().toString().length() + smsET.getText().toString().length() == 0) {
                    Toast.makeText(EmailActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("From :  " + fromET.getText().toString().trim() + "\nTo :  " + toET.getText().toString().trim() + "\nMessage:  " + smsET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        save_btn.setVisibility(View.VISIBLE);

                        downloadBtn.setVisibility(View.VISIBLE);
                        downloadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "SMS_Identity", null);
                                Toast.makeText(EmailActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
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

    private void save_to_db() {

        /*get text from edit text*/
        email_from = fromET.getText().toString().trim();
        email_to = toET.getText().toString().trim();
        email_sms = smsET.getText().toString().trim();

        /*making condition*/
        if (email_from.isEmpty()) {
            fromET.setError("Please fill sender email");
        } else if (email_to.isEmpty()) {
            toET.setError("please fill receiver email");
        } else if (email_sms.isEmpty()) {
            smsET.setError("please fill sms");
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
                EmailModel emailModel = new EmailModel(email_from, email_to, email_sms, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());

                /*these data save on new uid and also user id*/
                // reference.child(Key).setValue(emailModel);

                /*these data save on user id*/
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(emailModel);

                Toast.makeText(this, "save successful", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "un-successful", Toast.LENGTH_SHORT).show();
            }

        }

    }
}