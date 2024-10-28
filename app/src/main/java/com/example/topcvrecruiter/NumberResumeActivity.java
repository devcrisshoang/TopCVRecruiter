package com.example.topcvrecruiter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.Adapter.DashboardResumeAdapter;
import com.example.topcvrecruiter.model.Applicant;
import com.example.topcvrecruiter.model.CV;

import java.util.List;

public class NumberResumeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DashboardResumeAdapter resumeAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_number_resume);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){ return;}
        List<CV> resumeList = (List<CV>) getIntent().getSerializableExtra("resumeList");

        recyclerView = findViewById(R.id.number_resume_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resumeAdapter = new DashboardResumeAdapter(resumeList);
        recyclerView.setAdapter(resumeAdapter);
    }
}
