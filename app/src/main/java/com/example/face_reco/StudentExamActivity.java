package com.example.face_reco;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StudentExamActivity extends AppCompatActivity {
    Camera camera;
    Handler handler;
    Runnable runnable;
    SurfaceTexture surfaceTexture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_exam_activity);
        getSupportActionBar().hide();
        ActivityCompat.requestPermissions(StudentExamActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        surfaceTexture = new SurfaceTexture(10);
        handler = new Handler();
        final int delay = 10000; //milliseconds
        runnable = new Runnable() {
            @Override
            public void run() {
                takeSnapShots();
                handler.postDelayed(runnable, delay);

            }
        };
        runnable.run();
    }
    private void takeSnapShots() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(camIdx);
                    Log.e("tag", "Camera opened");

                } catch (RuntimeException e) {
                    Log.e("tag", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
//            camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e1) {
        }

        Camera.Parameters params = camera.getParameters();
        params.setPreviewSize(640, 480);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        params.setPictureFormat(ImageFormat.JPEG);
        params.setRotation(270);
        camera.setParameters(params);
        camera.startPreview();
        Log.e("tag", " image preview ");

        camera.takePicture(null, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
                bitmap = resize(bitmap, 800, 600);
                bitmap = rotateImage(bitmap, 270);
                new SendImageTask().execute(bitmap);


            }
        });
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private class SendImageTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == true) {
                Log.e("tag", "task finished ");


            } else {
                //Let user know login has failed
                Toast.makeText(getBaseContext(),"Face authentication failed",Toast.LENGTH_LONG).show();
                handler.removeCallbacks(runnable);
                Intent intent = new Intent(getBaseContext(), StudentHomeActivity.class);
                startActivity(intent);

            }
        }

        @Override
        protected Boolean doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.e("tag", " image string ");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getString(R.string.api_send_image));
            String str = null;

            List<BasicNameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("img", imageString));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                return false;
            }
            String result = "";
            try {
                HttpResponse response = httpclient.execute(httppost);
                Log.e("tag", " post excutedddd ");

                str = EntityUtils.toString(response.getEntity(),"UTF-8");
                JSONObject obj = new JSONObject(str);
                result = obj.optString("res");

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (result.equals("True")) {

                return true;
            } else {
                return false;
            }
        }
    }

//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        Log.e("tag", "Activity Minimized");
//        handler.removeCallbacks(runnable);
//        Intent intent = new Intent(getBaseContext(), StudentHomeActivity.class);
//        startActivity(intent);
//    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
