package com.example.cameratester;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import java.io.IOException;
import java.util.List;

public class FaceDetection {

    public static Bitmap detectFaces(final Context context, Uri photoUri){


        FirebaseApp.initializeApp(context);


        final Bitmap pic = MainActivity.resamplePic(context,MainActivity
                .getRealPathFromURI(context,photoUri));

        FirebaseVisionImage image = null;

        try {
            image = FirebaseVisionImage.fromFilePath(context, photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f)
                        .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(highAccuracyOpts);


        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {

                                        Toast.makeText(context.getApplicationContext(),
                                                "Detection Completed Successfully:\n " +  faces.size() + " Faces Detected"
                                                ,Toast.LENGTH_LONG).show();


                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context.getApplicationContext(),
                                                "Detection Failed",Toast.LENGTH_LONG).show();
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

        try {
            detector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return pic;

    }


}
