package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JobActivity extends AppCompatActivity {
    private Button continue_button;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        continue_button = findViewById(R.id.continue_button);
        back_button = findViewById(R.id.back_button);
        continue_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, JobDetailsActivity.class);
            startActivity(intent);
        });
        back_button.setOnClickListener(view -> {
            finish();
        });
    }



}