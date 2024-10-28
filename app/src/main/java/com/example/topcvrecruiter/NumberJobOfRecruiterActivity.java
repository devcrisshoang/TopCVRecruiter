package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DasboardJobAdapter;
import com.example.topcvrecruiter.model.Job;

import java.util.List;

public class NumberJobOfRecruiterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DasboardJobAdapter jobAdapter;
    List<Job> jobsList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_job_of_recruiter);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){ return;}
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("jobsList")) {
            jobsList = (List<Job>) intent.getSerializableExtra("jobsList");
        } else {
            Log.e("NumberJobOfRecruiterActivity", "No job list received");
        }

        recyclerView = findViewById(R.id.number_job_of_recruiter_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobAdapter = new DasboardJobAdapter(jobsList);
        recyclerView.setAdapter(jobAdapter);
    }
}
