package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiCompanyService;
import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.API.ApiNotificationService;
import com.example.topcvrecruiter.Model.CV;
import com.example.topcvrecruiter.Model.Notification;
import com.example.topcvrecruiter.Utils.DateTimeUtils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;


public class ResumeActivity extends AppCompatActivity {

    private ImageButton back_button;

    private ImageView cv_logo;

    private TextView name;
    private TextView job_applying;
    private TextView introduction;
    private TextView experience;
    private TextView email;
    private TextView phone_number;
    private TextView education;
    private TextView skill;
    private TextView certification;

    private Button recruit_button;
    private Button reject_button;

    private int rateSuccess = 0;
    private int rateFail = 0;
    private int recruiterId;
    private int applicantId;
    private int userId;
    private int applicantUserId;

    private String companyName;
    private String jobName;

    private boolean isAccepted;
    private boolean isRejected;

    private ApiDashboardService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());

        recruit_button.setOnClickListener(v -> {
            updateAcceptanceStatus(recruiterId, applicantId, true);
            finishWithSuccessResult();
        });

        reject_button.setOnClickListener(v -> {
            updateAcceptanceStatus(recruiterId, applicantId, false);
            finishWithFailResult();

        });
    }

    private void setWidget() {
        back_button = findViewById(R.id.back_button);
        cv_logo = findViewById(R.id.cv_logo);
        name = findViewById(R.id.name);
        job_applying = findViewById(R.id.job_applying);
        introduction = findViewById(R.id.introduction);
        experience = findViewById(R.id.experience);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        education = findViewById(R.id.education);
        skill = findViewById(R.id.skill);
        certification = findViewById(R.id.certification);
        recruit_button = findViewById(R.id.recruit_button);
        reject_button = findViewById(R.id.reject_button);
        apiService = ApiDashboardService.apiDashboardService;

        recruiterId = getIntent().getIntExtra("id_Recruiter", -1);
        applicantId = getIntent().getIntExtra("applicant_id", -1);
        userId = getIntent().getIntExtra("userId", -1);
        isAccepted = getIntent().getBooleanExtra("isAccepted", false);
        isRejected = getIntent().getBooleanExtra("isRejected", false);
        applicantUserId = getIntent().getIntExtra("applicantUserId", -1);
        jobName = getIntent().getStringExtra("jobName");

        fetchCvForApplicant(applicantId, recruiterId);
        getCompanyByRecruiterId(recruiterId);
        Log.e("ResumeActivity","companyName: " + companyName);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void getCompanyByRecruiterId(int recruiterId) {
        ApiCompanyService.ApiCompanyService.getCompanyByRecruiterId(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        company -> {
                            companyName = company.getName();
                        },
                        throwable -> Log.e("ResumeActivity", "Error fetching company: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    private void finishWithSuccessResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("rateSuccess", rateSuccess);
        setResult(RESULT_OK, resultIntent);
        String content = "Congratulation !!!, " + companyName + " recruit you in " + jobName + " job";
        String time = DateTimeUtils.getCurrentTime();

        Notification notification = new Notification(
                0,
                content,
                time,
                applicantUserId
        );
        ApiNotificationService.ApiNotificationService.createNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        finish();
    }

    @SuppressLint("CheckResult")
    private void finishWithFailResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("rateFail", rateFail);
        setResult(RESULT_OK, resultIntent);
        String content = "Sorry !!!, " + companyName + " argue that you are not suitable about " + jobName + "job";
        String time = DateTimeUtils.getCurrentTime();

        Notification notification = new Notification(
                0,
                content,
                time,
                applicantUserId
        );
        ApiNotificationService.ApiNotificationService.createNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        finish();
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
            Log.e("ResumeActivity", "Invalid applicant ID");
            return;
        }

        if (apiService == null) {
            Log.e("ResumeActivity", "API service is not initialized.");
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
                            cv_logo.setImageURI(Uri.parse(cv.getImage()));
                            Log.e("ResumeActivity","cv_logo: "+cv.getImage());
                            job_applying.setText(cv.getJob_Applying());
                            name.setText(cv.getApplicant_Name());
                            introduction.setText(cv.getIntroduction());
                            email.setText(cv.getEmail());
                            phone_number.setText(cv.getPhone_Number());
                            education.setText(cv.getEducation());
                            skill.setText(cv.getSkills());
                            certification.setText(cv.getCertificate());
                            experience.setText(cv.getExperience());
                        } else {
                            Log.e("ResumeActivity", "CV data is null");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ResumeActivity", "Error fetching CV", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
