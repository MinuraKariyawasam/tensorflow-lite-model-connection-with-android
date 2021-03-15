package com.example.scan2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scan2.ml.AayuAlexnetFinal;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ScanResults extends AppCompatActivity {

    private Bitmap scannedImage;
    private ImageView imageView;
    private TextView predictedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_results);

        scannedImage = getIntent().getParcelableExtra("scannedImage");
        imageView = (ImageView) findViewById(R.id.scanned_imageView);
        predictedResult = (TextView) findViewById(R.id.textView);

        if (scannedImage != null) {
            imageView.setImageBitmap(scannedImage);
        }
        scannedImage = Bitmap.createScaledBitmap(scannedImage,227,227,true);
        try {
            AayuAlexnetFinal model = AayuAlexnetFinal.newInstance(getApplicationContext());

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(scannedImage);

            ByteBuffer byteBuffer = tensorImage.getBuffer();

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 227, 227, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            AayuAlexnetFinal.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Releases model resources if no longer used.
            model.close();
            //System.out.println(outputFeature0.getIntArray()[4]);

            //int[] resultArray = outputFeature0.getIntArray();
            float max = outputFeature0.getFloatArray()[0];
            int index = 0;

            System.out.println(" ");
            //predictResult.setText("Result " + Arrays.toString(outputFeature0.getFloatArray()));

            for (int i = 0; i < outputFeature0.getFloatArray().length; i++){
                System.out.print(" " + outputFeature0.getFloatArray()[i] + " ");
            }

            for (int i = 0; i < outputFeature0.getFloatArray().length; i++)
            {
                if (max < outputFeature0.getFloatArray()[i]) {
                    max = outputFeature0.getFloatArray()[i];
                    index = i;

                    //System.out.println(index);
                }
            }

            //System.out.println(max);
            //System.out.println(index);

            if (index == 0) {
                predictedResult.setText("Arjun");
            } else if (index == 1){
                predictedResult.setText("Guava");
            } else if (index == 2){
                predictedResult.setText("Jamun");
            } else if (index == 3){
                predictedResult.setText("Jatropa");
            } else if (index == 4){
                predictedResult.setText("Lemon");
            } else if (index == 5){
                predictedResult.setText("Mango");
            }

        } catch (IOException e) {
            // TODO Handle the exception
        }


    }
}