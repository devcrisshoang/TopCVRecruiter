package com.example.topcvrecruiter;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ResumeActivity extends AppCompatActivity {

    private ImageButton back_button;

    private ImageView cv_logo;

    private TextView name;
    private TextView job_applying;
    private TextView introduction;
    private TextView experience;
    private TextView email;
    private TextView phone_number;
    private TextView education;
    private TextView skill;
    private TextView certification;

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

    private void setWidget() {
        back_button = findViewById(R.id.back_button);
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
