package com.example.face_reco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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

public class TeacherExamActivity extends AppCompatActivity {
    ListView warningsLv;
    ArrayList<String> warningsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_exam_activity);
        getSupportActionBar().hide();
        warningsLv = findViewById(R.id.warnings_listview);
        warningsList = new ArrayList<>();
        new GetResponseTask().execute();
    }
    private class GetResponseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == true) {
                Log.e("tag", "task finished ");
                warningsList.clear();
            } else {
                //Let user know login has failed
                String warning = "تنبيه: اختبار حسين تم الغائه بسبب احتمالية الغش";
                warningsList.add(warning);
                warningsLv.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, warningsList));


            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getString(R.string.api_get_res));
            String str = null;
            String result = "";
            try {
                HttpResponse response = httpclient.execute(httpGet);
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

}
