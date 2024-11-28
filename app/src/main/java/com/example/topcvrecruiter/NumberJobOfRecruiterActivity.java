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

    private RecyclerView recyclerView;

    private DasboardJobAdapter jobAdapter;

    private List<Job> jobsList;

    int id_Recruiter;

    private ImageButton backButton;

    private ActivityResultLauncher<Intent> applicantDetailLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWidget();

        setClick();
    }

    private void setClick(){

        backButton.setOnClickListener(v -> finish());
    }

    private void setWidget(){
        setContentView(R.layout.activity_job_of_recruiter);

        backButton = findViewById(R.id.back_button);

        if (getIntent() != null && getIntent().hasExtra("jobsList")) {
            jobsList = (List<Job>) getIntent().getSerializableExtra("jobsList");
        } else {
            Log.e("NumberJobOfRecruiterActivity", "No job list received");
        }

        Map<Integer, Integer> applicantCounts = (Map<Integer, Integer>) getIntent().getSerializableExtra("applicantCounts");
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", 0);

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
