package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostingJobActivity extends AppCompatActivity {

    private Button continue_button;

    private ImageButton back_button;

    private EditText etName;
    private EditText etExperience;
    private EditText etAddress;
    private EditText etCompany;
    private EditText etSalary;

    private int recruiter_id;

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

        setWidget();

        setClick();
    }

    private void continueButton(){
        String jobName = etName.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String companyName = etCompany.getText().toString().trim();
        String salaryText = etSalary.getText().toString().trim();

        if (jobName.isEmpty()) {
            Toast.makeText(this, "Please enter job title!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (experience.isEmpty()) {
            Toast.makeText(this, "Please enter experience!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (companyName.isEmpty()) {
            Toast.makeText(this, "Please enter company name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (salaryText.isEmpty()) {
            Toast.makeText(this, "Please enter salary!", Toast.LENGTH_SHORT).show();
            return;
        }

        int salary;
        try {
            salary = Integer.parseInt(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Salary must be an integer", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PostingJobActivity.this, JobDetailsActivity.class);
        intent.putExtra("image", "");
        intent.putExtra("jobName", jobName);
        intent.putExtra("companyName", companyName);
        intent.putExtra("experience", experience);
        intent.putExtra("address", address);
        intent.putExtra("salary", salary);
        intent.putExtra("id_Recruiter", recruiter_id);

        startActivity(intent);
    }

    private void setWidget(){
        recruiter_id = getIntent().getIntExtra("id_Recruiter",0);
        Log.e("JobActivity","ID: " + recruiter_id);
        continue_button = findViewById(R.id.continue_button);
        back_button = findViewById(R.id.back_button);
        etName = findViewById(R.id.et_name);
        etExperience = findViewById(R.id.et_workexperience);
        etAddress = findViewById(R.id.et_address);
        etCompany = findViewById(R.id.et_company);
        etSalary = findViewById(R.id.et_salary);

    }

    private void setClick(){

        continue_button.setOnClickListener(view -> continueButton());

        back_button.setOnClickListener(view -> finish());
    }

}
