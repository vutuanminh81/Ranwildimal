package com.example.ranwildimal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.os.Build;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ImageView;

import com.google.common.util.concurrent.ListenableFuture;

import java.security.Provider;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_ACTION_CODE = 1;

    private ImageCapture imageCapture;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        TextureView textureView = findViewById(R.id.cameraView);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Camera camera = cameraProvider.bindToLifecycle(
                        ((LifecycleOwner) this),
                        cameraSelector,
                        preview,
                        imageCapture);

                preview.setSurfaceProvider(
                        (Preview.SurfaceProvider) textureView.getSurfaceTexture());

            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
            }
            }, ContextCompat.getMainExecutor(this));
    }
}