package com.example.ranwildimal;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.CameraSelector;

import androidx.camera.core.ImageCapture;

import androidx.camera.core.Preview;


import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;



import android.os.Bundle;


import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_ACTION_CODE = 1;


    PreviewView cameraView = findViewById(R.id.cameraView);

    private ListenableFuture<ProcessCameraProvider> cameraProvider;
    private ImageCapture imageCapture;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);

            } catch (InterruptedException | ExecutionException e) {

            }
        }, ContextCompat.getMainExecutor(this));

    }


    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

        cameraProvider.bindToLifecycle(
                        ((LifecycleOwner) this),
                        cameraSelector,
                        preview,
                        imageCapture);
    }


}