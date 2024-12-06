package com.example.topcvrecruiter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.API.ApiNotificationService;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.Model.JobDetails;
import com.example.topcvrecruiter.Model.Notification;
import com.example.topcvrecruiter.Utils.DateTimeUtils;
import com.example.topcvrecruiter.Utils.NotificationUtils;
import java.time.LocalDateTime;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailsActivity extends AppCompatActivity {

    private Button post_button;

    private ImageButton back_button;

    private int id_Recruiter = 3;
    private int user_id;
    private int salary;

    private EditText benefit;
    private EditText numberOfPeople;
    private EditText genderRequire;
    private EditText workingTime;
    private EditText workingMethod;
    private EditText workingPosition;
    private EditText skillRequire;
    private EditText jobDescription;

    private String image;
    private String jobName;
    private String companyName;
    private String experience;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void setClick(){

        post_button.setOnClickListener(view -> postJobAndDetailsToApi(image, jobName, companyName, experience, address, salary));

        back_button.setOnClickListener(view -> finish());
    }

    private void setWidget(){

        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);
        user_id = getIntent().getIntExtra("user_id",0);

        post_button = findViewById(R.id.post_button);
        back_button = findViewById(R.id.back_button);
        jobDescription = findViewById(R.id.et_description);
        skillRequire = findViewById(R.id.et_skillrequire);
        benefit = findViewById(R.id.et_benefit);
        genderRequire = findViewById(R.id.et_gender);
        workingTime = findViewById(R.id.et_working_time);
        workingMethod = findViewById(R.id.et_working_method);
        workingPosition = findViewById(R.id.et_working_position);
        numberOfPeople = findViewById(R.id.et_numberpeople);

        image = getIntent().getStringExtra("image");
        jobName = getIntent().getStringExtra("jobName");
        companyName = getIntent().getStringExtra("companyName");
        experience = getIntent().getStringExtra("experience");
        address = getIntent().getStringExtra("address");
        salary = getIntent().getIntExtra("salary",-1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void postJobAndDetailsToApi(String image, String jobName, String companyName, String experience, String address, int salary) {
        String time = DateTimeUtils.getCurrentTime();

        String contentNotification = "You just created an job!";
        Notification notification = new Notification(
                0,
                contentNotification,
                time,
                user_id
        );
        NotificationUtils.showNotification(this, "You just posted an job!");

        ApiNotificationService.ApiNotificationService.createNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> Toast.makeText(this, "Notification created successfully!", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(this, "An error occurred: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        Job job = new Job(image, jobName, companyName, experience, address, salary, time, id_Recruiter);

        ApiJobService.apiService.postJob(job).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int jobId = response.body().getId();

                    String jobDescription = ((EditText) findViewById(R.id.et_description)).getText().toString().trim();
                    String skillRequire = ((EditText) findViewById(R.id.et_skillrequire)).getText().toString().trim();
                    String benefit = ((EditText) findViewById(R.id.et_benefit)).getText().toString().trim();
                    String genderRequire = ((EditText) findViewById(R.id.et_gender)).getText().toString().trim();
                    String workingTime = ((EditText) findViewById(R.id.et_working_time)).getText().toString().trim();
                    String workingMethod = ((EditText) findViewById(R.id.et_working_method)).getText().toString().trim();
                    String workingPosition = ((EditText) findViewById(R.id.et_working_position)).getText().toString().trim();
                    String numberOfPeople = ((EditText) findViewById(R.id.et_numberpeople)).getText().toString().trim();

                    JobDetails jobDetails = new JobDetails(jobDescription, skillRequire, benefit, genderRequire, workingTime, workingMethod, workingPosition, numberOfPeople, jobId);

                    ApiJobService.apiService.postJobDetails(jobDetails).enqueue(new Callback<JobDetails>() {
                        @Override
                        public void onResponse(Call<JobDetails> call, Response<JobDetails> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(JobDetailsActivity.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                                NotificationUtils.showNotification(JobDetailsActivity.this, "You just posted a job posting !");
                                finish();
                            } else {
                                Toast.makeText(JobDetailsActivity.this, "Failed to post job details!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JobDetails> call, Throwable t) {
                            Log.e("API_ERROR", t.getMessage());
                            Toast.makeText(JobDetailsActivity.this, "An error occurred while posting job details!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(JobDetailsActivity.this, "Failed to post job!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(JobDetailsActivity.this, "An error occurred while posting job!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted to post notifications", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
