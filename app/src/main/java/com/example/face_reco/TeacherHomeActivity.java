package com.example.face_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeActivity extends AppCompatActivity {
    Button createTestBtn, testsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_home);
        getSupportActionBar().hide();
        createTestBtn = findViewById(R.id.create_btn);
        testsBtn = findViewById(R.id.tests_btn);
        testsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TeacherExamActivity.class);
                startActivity(intent);
            }
        });
        createTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TeacherCreateExamActivity.class);
                startActivity(intent);
            }
        });
    }
}
