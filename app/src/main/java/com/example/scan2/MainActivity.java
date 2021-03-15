package com.example.scan2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Button openCameraButton, importImage, confirmImage;
    private ImageView imageView;
    private Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int RESULT_LOAD_IMG = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView2);
        openCameraButton = (Button) findViewById(R.id.button2);
        importImage = (Button) findViewById(R.id.button3);
        confirmImage = (Button) findViewById(R.id.confirm);
        openCameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        importImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImage();
            }
        });
        confirmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanResultScreen();
            }
        });


    }

    private void takePicture() {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //*****important-make getPackageManager()) == null (convert != to ==) if you run this on a real device
        if (imageTakeIntent.resolveActivity(getPackageManager()) == null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void importImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            imageView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageView.setImageBitmap(imageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Error", "permission not granted");
        }



    }
    public void openScanResultScreen(){
        if(imageBitmap!=null){
            Intent intent = new Intent(this,ScanResults.class);
            intent.putExtra("scannedImage", imageBitmap);
            startActivity(intent);
        }

    }

}