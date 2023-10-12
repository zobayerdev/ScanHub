package com.trodev.scanhub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.trodev.scanhub.models.WIFIMModels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WifiQrActivity extends AppCompatActivity {

    String[] items = {"WPA", "WPA2", "WPA3", "WEP", "None"};
    String[] item_two = {"Yes", "No"};
    AutoCompleteTextView ssid, encryption;
    ArrayAdapter<String> adapterItems;
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate, saveBtn;
    private EditText networkname, password;
    private ImageView imageView;
    String type, name, pass, hidden_type ;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_qr);

        // set title in activity
        getSupportActionBar().setTitle("Create WIFI QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*database location*/
        reference = FirebaseDatabase.getInstance().getReference("QR_DB").child("wifi_qr");

        /*view init code*/
        ssid = findViewById(R.id.ssid_text);
        encryption = findViewById(R.id.encryption);
        networkname = findViewById(R.id.networkEt);
        password = findViewById(R.id.passeordEt);

        /*button init code*/
        download = findViewById(R.id.downloadBtn);
        saveBtn = findViewById(R.id.save_btn);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.make_btn);

        /*button invisible code*/
        download.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);

        /*set encryption adapter on adapter*/
        adapterItems = new ArrayAdapter<String>(WifiQrActivity.this, R.layout.list_item, items);
        ssid.setAdapter(adapterItems);

        ssid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterItems = new ArrayAdapter<String>(WifiQrActivity.this, R.layout.list_item, item_two);
        encryption.setAdapter(adapterItems);

        encryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ssid.getText().toString().length() + networkname.getText().toString().length() + password.getText().toString().length() + encryption.getText().toString().length() == 0) {
                    Toast.makeText(WifiQrActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Encryption ssid :  " + ssid.getText().toString().trim()
                                + "\nNetwork name :  " + networkname.getText().toString().trim()
                                + "\nPassword:  " + password.getText().toString().trim()
                                + "\nPassword hidden:  " + encryption.getText().toString().trim());
                        // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        saveBtn.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(WifiQrActivity.this.getContentResolver(), bitmap, "wifi_Identity", null);
                                Toast.makeText(WifiQrActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
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

        type = ssid.getText().toString().trim();
        name = networkname.getText().toString();
        pass = password.getText().toString();
        hidden_type = encryption.getText().toString();

        if(type.isEmpty())
        {
            ssid.setError("please fill it");
        } else if (name.isEmpty()) {
            networkname.setError("please fill it");
        } else if (pass.isEmpty()) {
            encryption.setError("please fill it");
        } else if (hidden_type.isEmpty()) {
            encryption.setError("please fill it");
        } else
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
            WIFIMModels wifimModels = new WIFIMModels(type, name, pass, hidden_type, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child(Key).setValue(wifimModels);
            Toast.makeText(this, "save successful !!! please check your history dashboard", Toast.LENGTH_LONG).show();

            /*clear text from edittext*/
            ssid.setText("");
            networkname.setText("");
            password.setText("");
            encryption.setText("");
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