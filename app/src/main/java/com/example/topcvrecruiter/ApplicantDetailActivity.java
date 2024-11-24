package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.topcvrecruiter.API.ApiApplicantService;
import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.CV;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

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
    private ImageButton resumeMessageButton;

    private int recruiterId;
    private int applicantId;
    private int rateSuccess = 0;
    private int rateFail = 0;

    private ApiDashboardService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
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
        acceptButton = findViewById(R.id.recruit_button);
        rejectButton = findViewById(R.id.reject_button);
        resumeMessageButton = findViewById(R.id.resume_message);
        backButton = findViewById(R.id.back_button);

        // Get the applicant ID from the Intent
        recruiterId = getIntent().getIntExtra("id_Recruiter", -1);
        applicantId = getIntent().getIntExtra("applicant_id", -1);
        boolean isAccepted = getIntent().getBooleanExtra("isAccepted", false);
        boolean isRejected = getIntent().getBooleanExtra("isRejected", false);


        // Fetch the CV data
        fetchCvForApplicant(applicantId, recruiterId);

        if (isAccepted) {
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.VISIBLE);
        } else if(isRejected) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.GONE);
        }

        resumeMessageButton.setOnClickListener(v->{
                ApiApplicantService.ApiApplicantService.getApplicantById(applicantId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                applicant -> {
                                    if (applicant != null) {
                                        Intent intent = new Intent(ApplicantDetailActivity.this, MessageActivity.class);
                                        intent.putExtra("applicantId", applicantId);
                                        intent.putExtra("applicantName",applicant.getApplicant_Name());
                                        intent.putExtra("user_id",applicant.getiD_User());
                                        Log.e("ApplicantDetail","ID: "+ applicantId);
                                        Log.e("ApplicantDetail","Name: "+ applicant.getApplicant_Name());
                                        Log.e("ApplicantDetail","user_id: "+ applicant.getiD_User());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                                    Toast.makeText(this, "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                                }
                        );

        });
        // Set up the back button listener
        backButton.setOnClickListener(v -> {
            finish(); // Close the activity
        });

        // Set up the accept button
        acceptButton.setOnClickListener(v -> {
            updateAcceptanceStatus(recruiterId, applicantId, true);
            finishWithSuccessResult();
        });

        // Set up the reject button
        rejectButton.setOnClickListener(v -> {
            updateAcceptanceStatus(recruiterId, applicantId, false);
            finishWithFailResult();

        });
    }

    private void finishWithSuccessResult() {
        // Create an Intent to pass back the result
        Intent resultIntent = new Intent();
        resultIntent.putExtra("rateSuccess", rateSuccess);
        setResult(RESULT_OK, resultIntent); // RESULT_OK indicates a successful result
        finish(); // Close the activity
    }
    private void finishWithFailResult() {
        // Create an Intent to pass back the result
        Intent resultIntent = new Intent();
        resultIntent.putExtra("rateFail", rateFail);
        setResult(RESULT_OK, resultIntent); // RESULT_OK indicates a successful result
        finish(); // Close the activity
    }

    private void updateAcceptanceStatus(int recruiterId, int applicantId, boolean isAccepted) {
        apiService.updateAcceptanceStatus(recruiterId, applicantId, isAccepted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Response<Void> response) {
                        if (isAccepted) {
                            rateSuccess++;
                        } else {
                            rateFail++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
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
