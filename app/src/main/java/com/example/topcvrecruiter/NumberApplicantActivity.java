package com.example.topcvrecruiter;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.Fragment.DashboardFragment;
import com.example.topcvrecruiter.model.Applicant;

import java.util.List;

public class NumberApplicantActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DashboardApplicantAdapter applicantAdapter;
    private ImageButton backButton;
    private DashboardFragment dashboardFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_applicant); // Sửa chính tả tên layout

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) { return; }

        List<Applicant> applicantList = (List<Applicant>) getIntent().getSerializableExtra("applicantList");

        recyclerView = findViewById(R.id.number_applicant_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicantAdapter = new DashboardApplicantAdapter(this, applicantList);
        recyclerView.setAdapter(applicantAdapter);
    }

}
