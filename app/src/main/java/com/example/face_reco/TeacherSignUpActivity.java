package com.example.face_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherSignUpActivity extends AppCompatActivity {
    Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_signup_activity);
        getSupportActionBar().hide();
        regBtn = findViewById(R.id.T_signup);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TeacherLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
