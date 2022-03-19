package com.example.ranwildimal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.slider.Slider;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    Slider threshold;
    ImageView originalFrame;
    Button testBtn;
    Uri imageuri;
    private Bitmap bitmap;
    public String FILE_PATH = "";
    String filepath = "MyFileDir";
    String filePathAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        threshold = findViewById(R.id.threshold);
        originalFrame = findViewById(R.id.originalFrame);
        testBtn = findViewById(R.id.testbutton);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startForResult.launch(intent);
            }
        });
        FILE_PATH = getExternalFilesDir(filepath).getPath();
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Mat frame = new Mat();
                        Mat frame2 = new Mat();
                        imageuri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                            Utils.bitmapToMat(bitmap,frame);
                            //frame = getMask(frame);
                            Mat mask= new Mat(frame.size(),frame.type());
                            mask = getMask(frame);
                            Core.bitwise_and(frame,frame,frame,mask);
                            Utils.matToBitmap(frame,bitmap);
                            originalFrame.setImageBitmap(bitmap);

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
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Apply Canny
     *
     * @param frame
     *            the current frame
     * @return an image elaborated with Canny
     */
    private Mat doCanny(Mat frame)
    {
        // init
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        // reduce noise with a 3x3 kernel
        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

        // canny detector, with ratio of lower:upper threshold of 3:1
        Imgproc.Canny(detectedEdges, detectedEdges, 30, 90);

        // using Canny's output as a mask, display the result
        Mat dest = new Mat();
        frame.copyTo(dest, detectedEdges);

        return dest;
    }


    /**
     * Perform the operations needed for removing a uniform background
     *
     * @param frame
     *            the current frame
     * @return an image with only foreground objects
     */
    private Mat doBackgroundRemoval(Mat frame)
    {
        // init
        Mat hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();

        int thresh_type = Imgproc.THRESH_BINARY;

        // threshold the image with the average hue value
        hsvImg.create(frame.size(), CvType.CV_8U);
        Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
        Core.split(hsvImg, hsvPlanes);

        // get the average hue value of the image
        double threshValue = this.getHistAverage(hsvImg, hsvPlanes.get(0));

        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 150, thresh_type);

        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));


        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);

        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);
        Imgproc.adaptiveThreshold(thresholdImg, thresholdImg, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 21, 4);
        // create the new image
        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        frame.copyTo(foreground, thresholdImg);

        return foreground;
    }

    /**
     * Get the average hue value of the image starting from its Hue channel
     * histogram
     *
     * @param hsvImg
     *            the current frame in HSV
     * @param hueValues
     *            the Hue component of the current frame
     * @return the average Hue value
     */
    private double getHistAverage(Mat hsvImg, Mat hueValues)
    {
        // init
        double average = 0.0;
        Mat hist_hue = new Mat();
        // 0-180: range of Hue values
        MatOfInt histSize = new MatOfInt(180);
        List<Mat> hue = new ArrayList<>();
        hue.add(hueValues);

        // compute the histogram
        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

        // get the average Hue value of the image
        // (sum(bin(h)*h))/(image-height*image-width)
        // -----------------
        // equivalent to get the hue of each pixel in the image, add them, and
        // divide for the image size (height and width)
        for (int h = 0; h < 180; h++)
        {
            // for each bin, get its value and multiply it for the corresponding
            // hue
            average += (hist_hue.get(h, 0)[0] * h);
        }

        // return the average hue of the image
        return average = average / hsvImg.size().height / hsvImg.size().width;
    }

    public Mat process(Mat img){
        Mat img_gray= new Mat();
        Mat img_blurred= new Mat();
        Mat img_thresh= new Mat();
        Mat img_dilate = new Mat();
        Mat kernel= new Mat();
        Imgproc.cvtColor(img,img_gray,Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(img_gray,img_blurred, new Size(7,7),0);
        Imgproc.adaptiveThreshold(img_blurred, img_thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 21, 4);

        kernel = Mat.ones(new Size(13,13), CvType.CV_8UC1);
        Imgproc.dilate(img_thresh,img_dilate,kernel);
        Imgproc.erode(img_dilate,img_dilate,kernel);
        return img_dilate;
    }

    public Mat getMask(Mat img){
        Mat img2= new Mat();
        img2=img;
        List<MatOfPoint> contours= new ArrayList<>();
        List<MatOfPoint> contours2= new ArrayList<>();
        Mat hie = new Mat();
        Imgproc.findContours(process(img2),contours,hie,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_NONE);
        Mat blank = new Mat(img.size(),img.type());
        blank = Mat.zeros(img.size(),img.type());
        for (int i = 0; i < contours.size(); i++) {
            if(Imgproc.contourArea(contours.get(i)) > 500){
                MatOfPoint2f cnt2 = new MatOfPoint2f();
                contours.get(i).convertTo(cnt2,CvType.CV_32F);
                double peri = Imgproc.arcLength(cnt2,true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(cnt2,approx,peri * 0.004, true);
                MatOfPoint approx2=new MatOfPoint();
                approx.convertTo(approx2,CvType.CV_32F);
                contours2.add(approx2);
                Imgproc.drawContours(blank,contours,i,new Scalar(0, 255, 0),-1);
            }
        }
//        for(MatOfPoint cnt:contours){
//            if(Imgproc.contourArea(cnt) > 500){
//
//                Imgproc.drawContours(blank,[approx2],-1,new Scalar(0, 255, 0),-1);
//            }
//        }
//        Imgproc.drawContours(blank,contours2,-1,new Scalar(0, 255, 0),-1);
        return blank;
    }
}