package com.example.face_reco;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getSupportActionBar().hide();
        logo_luncher logo_luncher = new logo_luncher();
        logo_luncher.start();
    }



    private class logo_luncher extends Thread {
        public void run(){


            try{

                sleep(3000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
            splash.this.finish();


        }




    }
}