package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.model.CV;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApplicantDetailActivity extends AppCompatActivity {
    private TextView jobApplyingTextView;
    private TextView nameTextView;
    private TextView introductionTextView;
    private TextView experienceTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView educationTextView;
    private TextView skillTextView;
    private TextView certificationTextView;
    private Button acceptButton;
    private Button rejectButton;
    private ImageButton backButton;

    private int recruitmentRate = 0;

    private ApiDashboardService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_details);
        apiService = ApiDashboardService.apiDashboardService;

        // Initialize the views
        jobApplyingTextView = findViewById(R.id.name);
        nameTextView = findViewById(R.id.job_applying);
        introductionTextView = findViewById(R.id.introduction);
        experienceTextView = findViewById(R.id.experience);
        emailTextView = findViewById(R.id.email);
        phoneNumberTextView = findViewById(R.id.phone_number);
        educationTextView = findViewById(R.id.education);
        skillTextView = findViewById(R.id.skill);
        certificationTextView = findViewById(R.id.certification);
        acceptButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_button);
        backButton = findViewById(R.id.back_button);

        // Get the applicant ID from the Intent
        int applicantId = getIntent().getIntExtra("applicant_id", -1);

        // Fetch the CV data
        fetchCvForApplicant(applicantId, 1);

        // Set up the back button listener
        backButton.setOnClickListener(v -> {
            finish(); // Close the activity
        });

        // Set up the accept button
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recruitmentRate++; // Increase recruitment rate
                passRecruitmentRateBack();
            }
        });

        // Set up the reject button
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recruitmentRate--; // Decrease recruitment rate
                passRecruitmentRateBack();
            }
        });
    }

    private void passRecruitmentRateBack() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("recruitment_rate", recruitmentRate);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void fetchCvForApplicant(int applicantId, int recruiterId) {
        if (applicantId == -1) {
            Log.e("ApplicantDetailActivity", "Invalid applicant ID");
            return;
        }

        if (apiService == null) {
            Log.e("ApplicantDetailActivity", "API service is not initialized.");
            return;
        }

        apiService.getCvForApplicant(applicantId, recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CV>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CV cv) {
                        if (cv != null) {
                            jobApplyingTextView.setText(cv.getJob_Applying());
                            nameTextView.setText(cv.getApplicant_Name());
                            introductionTextView.setText(cv.getIntroduction());
                            emailTextView.setText(cv.getEmail());
                            phoneNumberTextView.setText(cv.getPhone_Number());
                            educationTextView.setText(cv.getEducation());
                            skillTextView.setText(cv.getSkills());
                            certificationTextView.setText(cv.getCertificate());
                        } else {
                            Log.e("ApplicantDetailActivity", "CV data is null");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ApplicantDetailActivity", "Error fetching CV", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
