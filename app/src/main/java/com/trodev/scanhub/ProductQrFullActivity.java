package com.trodev.scanhub;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.scanhub.activities.ProductQrActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProductQrFullActivity extends AppCompatActivity {

    TextView making_date_tv, expire_date_tv, product_name_tv, product_code_tv, company_name_tv;
    String makeTv, expireTv, nameTv, codeTv, companyTv;
    Button generate, qr_download, pdf_download;
    public final static int QRCodeWidth = 500;
    Bitmap card, qrimage;
    ImageView qr_image;
    LinearLayout infoLl;
    CardView cardView;
    final static int REQUEST_CODE = 1232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qr_full);

        askPermissions();

        /*ini view*/
        making_date_tv = findViewById(R.id.making_date_tv);
        expire_date_tv = findViewById(R.id.expire_date_tv);
        product_name_tv = findViewById(R.id.product_name_tv);
        product_code_tv = findViewById(R.id.product_code_tv);
        company_name_tv = findViewById(R.id.company_name_tv);

        /*init buttons*/
        generate = findViewById(R.id.generate);
        qr_download = findViewById(R.id.qr_download);
        pdf_download = findViewById(R.id.pdf_download);

        /*image*/
        qr_image = findViewById(R.id.qr_image);

        /*card view init*/
        infoLl = findViewById(R.id.infoLl);
        cardView = findViewById(R.id.cardView);

        makeTv = getIntent().getStringExtra("mDate");
        expireTv = getIntent().getStringExtra("eDate");
        nameTv = getIntent().getStringExtra("pName");
        codeTv = getIntent().getStringExtra("code");
        companyTv = getIntent().getStringExtra("company");

        /*set text on text views*/
        making_date_tv.setText(makeTv);
        expire_date_tv.setText(expireTv);
        product_name_tv.setText(nameTv);
        product_code_tv.setText(codeTv);
        company_name_tv.setText(companyTv);

        /*auto create qr image*/
        make_QR();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pdf_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_pdf();
            }
        });

        qr_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card = null;
                card = getBitmapFromUiView(infoLl);
                saveBitmapImage(card);

            }
        });

    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    /* ##################################################################### */
    /* ##################################################################### */
    /*method
    * qr image downloader*/
    private Bitmap getBitmapFromUiView(View view) {

        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);

        //return the bitmap
        return returnedBitmap;
    }
    private void saveBitmapImage(Bitmap card) {

        long timestamp = System.currentTimeMillis();

        //Tell the media scanner about the new file so that it is immediately available to the user.
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            if (uri != null) {
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        try {
                            card.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.close();
                        } catch (Exception e) {
                            Log.e(TAG, "saveToGallery: ", e);
                        }
                    }

                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    getContentResolver().update(uri, values, null, null);

                    Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "saveToGallery: ", e);
                }
            }

        } else {
            File imageFileFolder = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs();
            }
            String mImageName = "" + timestamp + ".png";

            File imageFile = new File(imageFileFolder, mImageName);
            try {
                OutputStream outputStream = new FileOutputStream(imageFile);
                try {
                    card.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "saveToGallery: ", e);
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "saveToGallery: ", e);
            }
        }
    }


    /* ##################################################################### */
    /* ##################################################################### */
    /*qr pdf file*/
    private void make_pdf() {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        cardView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        Log.d("my log", "Width Now " + cardView.getMeasuredWidth());

        // cardView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();

        // Obtain the width and height of the view
        int viewWidth = cardView.getMeasuredWidth();
        int viewHeight = cardView.getMeasuredHeight();


        // Create a PageInfo object specifying the page attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas object to draw on the page
        Canvas canvas = page.getCanvas();

        // Create a Paint object for styling the view
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw the view on the canvas
        cardView.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Specify the path and filename of the output PDF file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String fileName = "product_qr.pdf";

        File filePath = new File(downloadsDir, fileName);

        try {
            // Save the document to a file
            FileOutputStream fos = new FileOutputStream(filePath);
            document.writeTo(fos);
            document.close();
            fos.close();
            // PDF conversion successful
            Toast.makeText(this, "PDF download successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF download un-successful", Toast.LENGTH_SHORT).show();
        }

    }


    /* ##################################################################### */
    /* ##################################################################### */
    /*method
     * text to image encode method
     * this method create a image get on text*/
    private void make_QR() {
        if (making_date_tv.getText().toString().trim().length() +
                expire_date_tv.getText().toString().length() +
                product_name_tv.getText().toString().length() +
                product_code_tv.getText().toString().length() +
                company_name_tv.getText().toString().length() == 0) {

        } else {
            try {
                qrimage = textToImageEncode(
                        "Manufacture Date:  " + making_date_tv.getText().toString() +
                                "\nExpire Date:  " + expire_date_tv.getText().toString().trim() +
                                "\nProduct Name:  " + product_name_tv.getText().toString().trim() +
                                "\nProduct Code:  " + product_code_tv.getText().toString().trim() +
                                "\nCompany Name:  " + company_name_tv.getText().toString().trim());
                // + "\n\n\nMake by Altai Platforms"
                qr_image.setImageBitmap(qrimage);

                /*visibility button section*/
                // download.setVisibility(View.VISIBLE);
                // saveBtn.setVisibility(View.VISIBLE);
                qr_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), qrimage, "product_qr_image"
                                , null);
                        Toast.makeText(ProductQrFullActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (WriterException e) {
                e.printStackTrace();
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