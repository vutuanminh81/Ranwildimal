package com.example.ranwildimal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.CameraSelector;

import androidx.camera.core.ImageCapture;

import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;


import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_ACTION_CODE = 1;


    PreviewView cameraView ;

    private ListenableFuture<ProcessCameraProvider> cameraProvider;
    private ImageCapture imageCapture;
    private Button btnGallery, btnCamera;

    //public final String FILE_PATH = this.getExternalCacheDir().getPath()+"/data/com.example.ranwildimal";
    public String FILE_PATH = "";
    String filepath = "MyFileDir";
    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private  int imageSizeX;
    private  int imageSizeY;
    private TensorBuffer outputProbabilityBuffer;
    private TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    String filePathAll;
    private List<String> labels;
    Uri imageuri;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result!=null && result.getResultCode()==RESULT_OK && result.getData()!=null) {
                        imageuri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                            File photoDir = new File(FILE_PATH);
                            if(!photoDir.exists()){
                                photoDir.mkdir();
                            }
                            Date date = new Date();
                            String timestamp=  String.valueOf(date.getTime());
                            String photoFilePath =  photoDir.getAbsolutePath()+"/"+timestamp+".jpg";
                            filePathAll = photoFilePath;
                            File photoFile = new File(photoFilePath);
                            try (FileOutputStream out = new FileOutputStream(photoFile)) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            filePathAll = UriUtils.getPathFromUri(CameraActivity.this,imageuri);
                            classifyImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = findViewById(R.id.cameraView);
        btnGallery = findViewById(R.id.btn_Gallery);
        btnCamera = findViewById(R.id.btn_Camera);

        FILE_PATH = getExternalFilesDir(filepath).getPath();

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);

            } catch (InterruptedException | ExecutionException e) {

            }
        }, ContextCompat.getMainExecutor(this));

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                getCaptureImage();

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startForResult.launch(intent);
            }
        });

        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void classifyImage(){
        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        int probabilityTensorIndex = 0;
        int[] probabilityShape =
                tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
        DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);
        outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

        try {
            inputImageBuffer = loadImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tflite.run(inputImageBuffer.getBuffer(),outputProbabilityBuffer.getBuffer().rewind());
        System.out.println("///////////////////////");
        float[] listResult = outputProbabilityBuffer.getFloatArray();
        boolean checkResult = false;
        int i=1;
        for(Float f : listResult){
            if(f>=0.7){
                checkResult = true;

            }
            System.out.println(i++ + "//////////////////"+f);
        }
        if(checkResult){
            showresult();
        }else{
            Intent intent = new Intent(this, ResultErrorActivity.class);
            intent.putExtra("filePathImg",filePathAll);
            this.startActivity(intent);
        }

    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getCaptureImage(){
        imageCapture.takePicture(this.getMainExecutor(), new ImageCapture.OnImageCapturedCallback() {

            @Override
            public void onCaptureSuccess (ImageProxy imageProxy) {
                // Use the image, then make sure to close it before returning from the method
                bitmap= imageProxyToBitmap(imageProxy);
                imageProxy.close();

            }

            @Override
            public void onError (ImageCaptureException exception) {
                // Handle the exception however you'd like
            }
        });

        File photoDir = new File(FILE_PATH);
        if(!photoDir.exists()){
            photoDir.mkdir();
        }
        Date date = new Date();
        String timestamp=  String.valueOf(date.getTime());
        String photoFilePath =  photoDir.getAbsolutePath()+"/"+timestamp+".jpg";
        filePathAll = photoFilePath;
        File photoFile = new File(photoFilePath);



        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getMainExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(CameraActivity.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
                        classifyImage();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        System.out.println("//////Error: "+exception.getMessage());
                        Toast.makeText(CameraActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
    }

    private TensorImage loadImage(final Bitmap bitmap) throws IOException {
        // Loads bitmap into a TensorImage.
//        Utils.bitmapToMat(bitmap,matConvert);
//        Imgproc.cvtColor(matConvert, matConvert, Imgproc.COLOR_BGR2GRAY);
//        Utils.matToBitmap(matConvert, bitmap);
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("newBestModel_4.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            labels = FileUtil.loadLabels(this,"newdict.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {

            if (entry.getValue()==maxValueInMap) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(this, ResultSuccessActivity.class);
//                intent.putExtra("imgBitmap",byteArray);
                intent.putExtra("animalName",entry.getKey());
                intent.putExtra("filePathImg",filePathAll);
                this.startActivity(intent);
                break;
            }
        }
    }
}