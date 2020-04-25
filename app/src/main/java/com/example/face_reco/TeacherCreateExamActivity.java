package com.example.face_reco;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherCreateExamActivity extends AppCompatActivity {
    Button saveBtn , createBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exam);
        getSupportActionBar().hide();


        saveBtn = findViewById(R.id.T_save_exam);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TeacherHomeActivity.class);
                startActivity(intent);
            }
        });


        createBtn = findViewById(R.id.T_add_ques);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDilog();
            }
        });

    }

    public void showDilog(){
        Dialog dialog = new Dialog(TeacherCreateExamActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.permium_version_alert);
        dialog.show();
        Button OK = dialog.findViewById(R.id.Ok_btn);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TeacherHomeActivity.class);
                startActivity(intent);
            }
        });

    }

}