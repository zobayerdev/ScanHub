package com.trodev.scanhub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.scanhub.R;
import com.trodev.scanhub.models.SMSModels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SmsActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, generate, uploadBtn;
    private EditText smsET, fromET, toET;
    private ImageView imageView;
    String sms, from, to;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        // set title in activity
        getSupportActionBar().setTitle("Create Message QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*database location*/
        reference = FirebaseDatabase.getInstance().getReference("QR_DB").child("sms_qr");

        /*init views*/
        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);
        smsET = findViewById(R.id.smsET);
        imageView = findViewById(R.id.imageIV);
        generate = findViewById(R.id.make_btn);
        uploadBtn = findViewById(R.id.uploadBtn);
        download = findViewById(R.id.downloadBtn);

        /**/
        download.setVisibility(View.INVISIBLE);
        uploadBtn.setVisibility(View.INVISIBLE);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromET.getText().toString().length() + toET.getText().toString().length() + smsET.getText().toString().length() == 0) {
                    Toast.makeText(SmsActivity.this, "please fill all fields..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("From :  " + fromET.getText().toString().trim() +
                                "\nTo :  " + toET.getText().toString().trim() +
                                "\nMessage:  " + smsET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        uploadBtn.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "SMS_Identity", null);
                                Toast.makeText(SmsActivity.this, "download complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        /*click button on save button and it save it firebase database*/
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_to_db();

            }
        });

    }

    private void save_to_db() {
        sms = smsET.getText().toString().trim();
        from = fromET.getText().toString();
        to = toET.getText().toString();

        if(sms.isEmpty())
        {
            smsET.setError("please fill it");
        } else if (from.isEmpty()) {
            fromET.setError("please fill it");
        } else if (to.isEmpty()) {
            toET.setError("please fill it");
        }else
        {
            // Its a time and date code show our user and admin
            // amra date and time init kore database e save korchi
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            String time = currentTime.format(calForTime.getTime());

            String Key = reference.push().getKey();
            SMSModels smsModels = new SMSModels(sms, from, to, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child(Key).setValue(smsModels);
            Toast.makeText(this, "save successful !!! please check your history dashboard", Toast.LENGTH_LONG).show();

            /*clear text from edittext*/
            smsET.setText("");
            fromET.setText("");
            toET.setText("");
        }

    }

    /*text to image convert*/
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