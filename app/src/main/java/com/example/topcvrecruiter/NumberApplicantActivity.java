package com.example.topcvrecruiter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.model.Applicant;

import java.util.List;

public class NumberApplicantActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DashboardApplicantAdapter apllicantAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_number_applicant);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){ return;}
        int applicantCount = getIntent().getIntExtra("applicant_count", 0);
        List<Applicant> applicantList = (List<Applicant>) getIntent().getSerializableExtra("applicantList");

        recyclerView = findViewById(R.id.number_applicant_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apllicantAdapter = new DashboardApplicantAdapter(applicantList);
        recyclerView.setAdapter(apllicantAdapter);
    }
}
