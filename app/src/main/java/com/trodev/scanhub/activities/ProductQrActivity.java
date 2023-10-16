package com.trodev.scanhub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.scanhub.models.QRModels;
import com.trodev.scanhub.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductQrActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate, saveBtn;
    private EditText makeET, expireET, productNameET, productET, companyET;
    private ImageView imageView;
    private ImageButton dateBtn, expireDateBtn;

    //define calender & date picker
    Calendar c;
    private DatePickerDialog.OnDateSetListener makeDate, expireDate;
    public String make_date, expire_date, company_name, product_info, product_name;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qr);

        /*set title in activity*/
        getSupportActionBar().setTitle("Create Product QR");

        /*back button default*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*database location*/
        reference = FirebaseDatabase.getInstance().getReference("QR_DB").child("product_qr");


        /*init views*/
        makeET = findViewById(R.id.makeDateET);
        expireET = findViewById(R.id.expiredateET);
        productET = findViewById(R.id.productCodeET);
        productNameET = findViewById(R.id.productNameET);
        companyET = findViewById(R.id.companyET);

        download = findViewById(R.id.downloadBtn);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.make_btn);
        dateBtn = findViewById(R.id.dateBtn);
        saveBtn = findViewById(R.id.save_btn);
        expireDateBtn = findViewById(R.id.expireDateBtn);

        /*button visibility hide code*/
        download.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);


        /*set on click listener on date picker button*/
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(ProductQrActivity.this, android.R.style.Theme_DeviceDefault_Light_Panel, makeDate, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_3)));
                dialog.show();

            }
        });


        makeDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;
                makeET.setText(date);

            }
        };

        /*set on click expire date picker button */
        expireDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(ProductQrActivity.this, android.R.style.Theme_DeviceDefault_Light_Panel, expireDate, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_3)));
                dialog.show();

            }
        });

        expireDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;
                expireET.setText(date);

            }
        };


        /*set on click on generate button*/
        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (makeET.getText().toString().trim().length() +
                        expireET.getText().toString().length() +
                        productNameET.getText().toString().length() +
                        productET.getText().toString().length() +
                        companyET.getText().toString().length() == 0) {
                    Toast.makeText(ProductQrActivity.this, "make sure your input isn't blank...!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode(
                                "Manufacture Date:  " + makeET.getText().toString() +
                                        "\nExpire Date:  " + expireET.getText().toString().trim() +
                                        "\nProduct Name:  " + productNameET.getText().toString().trim() +
                                        "\nProduct Code:  " + productET.getText().toString().trim() +
                                        "\nCompany Name:  " + companyET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);

                        /*visibility button section*/
                        download.setVisibility(View.VISIBLE);
                        saveBtn.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "product_qr_image"
                                        , null);
                                Toast.makeText(ProductQrActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        /*click button on save button and it save it firebase database*/
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_to_db();

            }
        });




    }

    private void save_to_db() {

        /*get text from edit text*/
        make_date = makeET.getText().toString().trim();
        expire_date = expireET.getText().toString().trim();
        company_name = companyET.getText().toString().trim();
        product_info = productET.getText().toString().trim();
        product_name = productNameET.getText().toString().trim();


        /*making condition*/
        if (make_date.isEmpty()) {
            makeET.setError("Please fill make date");
        } else if (expire_date.isEmpty()) {
            expireET.setError("please fill expire date");
        } else if (company_name.isEmpty()) {
            companyET.setError("please fill company name");
        } else if (product_info.isEmpty()) {
            productET.setError("please fill product info");
        } else if (product_name.isEmpty()) {
            productET.setError("please fill product name");
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
            QRModels qrModels = new QRModels(make_date, expire_date, company_name, product_name, product_info, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child(Key).setValue(qrModels);
            Toast.makeText(this, "save successful !!! please check your history dashboard", Toast.LENGTH_LONG).show();

        }

    }


    /* ##################################################################### */
    /* ##################################################################### */
    /*method
     * text to image encode method
     * this method create a image get on text*/
    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}