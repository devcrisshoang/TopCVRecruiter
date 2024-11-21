package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DasboardJobAdapter;
import com.example.topcvrecruiter.Model.Job;

import java.util.List;
import java.util.Map;

public class NumberJobOfRecruiterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DasboardJobAdapter jobAdapter;
    List<Job> jobsList;
    int id_Recruiter;
    private ImageButton backButton;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_of_recruiter); // Sửa chính tả tên layout

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) { return; }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("jobsList")) {
            jobsList = (List<Job>) intent.getSerializableExtra("jobsList");
        } else {
            Log.e("NumberJobOfRecruiterActivity", "No job list received");
        }
        Map<Integer, Integer> applicantCounts = (Map<Integer, Integer>) intent.getSerializableExtra("applicantCounts");
        id_Recruiter = intent.getIntExtra("id_Recruiter", 0);

        recyclerView = findViewById(R.id.number_job_of_recruiter_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicantDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                    }
                }
        );

        jobAdapter = new DasboardJobAdapter(applicantDetailLauncher, jobsList, applicantCounts, id_Recruiter);
        recyclerView.setAdapter(jobAdapter);

    }
}
