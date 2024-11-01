package com.example.topcvrecruiter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ResumeActivity extends AppCompatActivity {
    private ImageButton back_button, shareButton;
    private Button exportPdfButton;
    private ActivityResultLauncher<Intent> createPdfLauncher;
    private Uri savedPdfUri;

    private ImageView cv_logo;
    private TextView name, job_applying, introduction, experience, email, phone_number, education, skill, certification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
    }
    // Thiết lập các widget trên layout
    private void setWidget() {
        back_button = findViewById(R.id.back_button);
        shareButton = findViewById(R.id.share_button);
        cv_logo = findViewById(R.id.cv_logo);
        name = findViewById(R.id.name);
        job_applying = findViewById(R.id.job_applying);
        introduction = findViewById(R.id.introduction);
        experience = findViewById(R.id.experience);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        education = findViewById(R.id.education);
        skill = findViewById(R.id.skill);
        certification = findViewById(R.id.certification);
    }
}
