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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.scanhub.R;

import java.util.Calendar;

public class ProductQrActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;
    private EditText makeET, expireET, productET, companyET;
    private ImageView imageView;
    private ImageButton dateBtn, expireDateBtn;

    //define calender & date picker
    Calendar c;
    private DatePickerDialog.OnDateSetListener makeDate, expireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qr);

        /*set title in activity*/
        getSupportActionBar().setTitle("Create Product QR");

        /*back button default*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*init views*/
        makeET = findViewById(R.id.makeDateET);
        expireET = findViewById(R.id.expiredateET);
        productET = findViewById(R.id.productCodeET);
        companyET = findViewById(R.id.companyET);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.make_btn);
        dateBtn = findViewById(R.id.dateBtn);
        expireDateBtn = findViewById(R.id.expireDateBtn);


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

                if (makeET.getText().toString().trim().length() + expireET.getText().toString().length()
                        + productET.getText().toString().length() + companyET.getText().toString().length() == 0) {
                    Toast.makeText(ProductQrActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Manufacture Date:  " + makeET.getText().toString() + "\nExpire Date:  " + expireET.getText().toString().trim() +
                                "\nProduct Code:  " + productET.getText().toString().trim() + "\nCompany Name:  " + companyET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Product_Identity"
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

    }

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