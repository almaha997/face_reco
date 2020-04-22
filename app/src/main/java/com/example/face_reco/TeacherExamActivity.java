package com.example.face_reco;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_exam_activity);
        getSupportActionBar().hide();
    }
}
