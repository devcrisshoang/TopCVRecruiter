package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiApplicantService;
import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.Model.CV;
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

    private ImageButton backButton;
    private ImageButton resumeMessageButton;

    private int userId;
    private int recruiterId;
    private int applicantId;

    private ApiDashboardService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    @SuppressLint("CheckResult")
    private void contactApplicantButton(){
        ApiApplicantService.ApiApplicantService.getApplicantById(applicantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                Intent intent = new Intent(ApplicantDetailActivity.this, MessageActivity.class);
                                intent.putExtra("applicantNameContact",applicant.getApplicant_Name());
                                intent.putExtra("userIdContact",applicant.getiD_User());
                                intent.putExtra("userIdRecruiter",userId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("ApplicantDetailActivity", "Error fetching recruiter: " + throwable.getMessage());
                        }
                );
    }

    private void setClick(){

        resumeMessageButton.setOnClickListener(v-> contactApplicantButton());

        backButton.setOnClickListener(v -> finish());
    }

    private void setWidget(){
        apiService = ApiDashboardService.apiDashboardService;
        jobApplyingTextView = findViewById(R.id.name);
        nameTextView = findViewById(R.id.job_applying);
        introductionTextView = findViewById(R.id.introduction);
        experienceTextView = findViewById(R.id.experience);
        emailTextView = findViewById(R.id.email);
        phoneNumberTextView = findViewById(R.id.phone_number);
        educationTextView = findViewById(R.id.education);
        skillTextView = findViewById(R.id.skill);
        certificationTextView = findViewById(R.id.certification);
        resumeMessageButton = findViewById(R.id.resume_message);
        backButton = findViewById(R.id.back_button);
        recruiterId = getIntent().getIntExtra("id_Recruiter", 0);
        applicantId = getIntent().getIntExtra("applicant_id", 0);
        userId = getIntent().getIntExtra("userId", 0);
        fetchCvForApplicant(applicantId, recruiterId);
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
                            experienceTextView.setText(cv.getExperience());
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
