package com.example.face_reco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class StudentSignUpActivity extends AppCompatActivity {
    Button signUpBtn, faceIdBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_signup_activity);
        getSupportActionBar().hide();
        signUpBtn = findViewById(R.id.S_signup);
        faceIdBtn = findViewById(R.id.S_face_id);
        progressBar = findViewById(R.id.progress);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), StudentLoginActivity.class);
                startActivity(intent);
            }
        });
        faceIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
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

    private class SendImageRegTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == true) {

                Log.e("tag", "task finished ");
                Toast.makeText(getBaseContext(),"تم حفظ بصمة الوجه",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);



            } else {
                //Let user know login has failed
                Toast.makeText(getBaseContext(),"فشل اضافة بصمة الوجه, حاول مجددا",Toast.LENGTH_LONG).show();

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
            HttpPost httppost = new HttpPost(getString(R.string.api_send_image_reg));
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
                Log.e("tag", "THE RESULT "+result);

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


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        imageBitmap = rotateImage(imageBitmap, 270);
        imageBitmap = resize(imageBitmap, 800, 600);
        new SendImageRegTask().execute(imageBitmap);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
