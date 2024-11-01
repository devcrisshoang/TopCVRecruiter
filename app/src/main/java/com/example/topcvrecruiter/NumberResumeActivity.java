package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DashboardResumeAdapter;
import com.example.topcvrecruiter.model.CV;

import java.util.List;

public class NumberResumeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DashboardResumeAdapter resumeAdapter;
    List<CV> resumeList;
    private ImageButton backButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_resume); // Đảm bảo tên layout đúng

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) { return; }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("resumeList")) {
            resumeList = (List<CV>) intent.getSerializableExtra("resumeList");
        } else {
            Log.e("NumberResumeActivity", "No resume list received");
        }

        recyclerView = findViewById(R.id.number_resume_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resumeAdapter = new DashboardResumeAdapter(resumeList);
        recyclerView.setAdapter(resumeAdapter);
    }
}
